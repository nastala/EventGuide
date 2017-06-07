package com.example.nastala.eventguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.photoutil.ImageBase64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PastEventCommentActivity extends AppCompatActivity {
    private String encodedImage, username, event_id, title, commentText, encodedProfileImage, commentUsername, date;
    private ListView lvComment;
    private ImageView ivEvent;
    private TextView tvRate, tvTitle, tvRecommandationText;
    private Bitmap image;
    private Button btnRate;
    private Boolean rated;
    private ArrayList<Comments> comments;
    private Comments comment;
    private int rateCode;
    private float average;
    private Bitmap profileImage, rate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_event_comment);

        PastEventCommentActivity.this.setTitle("Comments & Rates");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));

        Intent i = getIntent();
        title = i.getStringExtra("title");
        event_id = i.getStringExtra("event_id");
        username = i.getStringExtra("username");
        encodedImage = i.getStringExtra("image");

        lvComment = (ListView) findViewById(R.id.lvComment);
        ivEvent = (ImageView) findViewById(R.id.ivEvent);
        tvRate = (TextView) findViewById(R.id.tvRate);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnRate = (Button) findViewById(R.id.btnRate);
        tvRecommandationText = (TextView) findViewById(R.id.tvRecommandationText);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        rated = false;
        average = 0.0f;

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyRequest();
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog(PastEventCommentActivity.this);
            }
        });

        clearView();
        volleyRequest();
    }

    public void showCommentDialog(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(PastEventCommentActivity.this);
        View mView = activity.getLayoutInflater().inflate(R.layout.add_comment_layout, null);

        final EditText etComment = (EditText) mView.findViewById(R.id.etComment);
        final RadioButton rbtnYes = (RadioButton) mView.findViewById(R.id.rbtnYes);
        final RadioButton rbtnNo = (RadioButton) mView.findViewById(R.id.rbtnNo);
        Button btnRate = (Button) mView.findViewById(R.id.btnRate);

        builder.setView(mView);
        final AlertDialog dialog = builder.create();

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                int recommend;

                if(rbtnYes.isChecked())
                    recommend = 1;
                else if(rbtnNo.isChecked())
                    recommend = 0;
                else
                    recommend = 1;

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                Toast.makeText(PastEventCommentActivity.this, "Rated event successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                volleyRequest();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                AddCommentRequest request = new AddCommentRequest(event_id, username, comment, recommend, currentDate, listener);
                RequestQueue queue = Volley.newRequestQueue(PastEventCommentActivity.this);
                queue.add(request);
            }
        });

        dialog.show();
    }

    public void clearView(){
        tvTitle.setText("");
        tvRate.setText("");
        ivEvent.setImageBitmap(null);
    }

    public void fillDetails(){
        byte[] decodedImage = Base64.decode(encodedImage.getBytes(), Base64.NO_WRAP);
        image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

        String averageText = String.format("%.1f", average);

        ivEvent.setImageBitmap(image);
        tvTitle.setText(title);
        tvRate.setText(averageText + "%");
        tvRecommandationText.setVisibility(View.VISIBLE);

        if(rated == false)
            btnRate.setVisibility(View.VISIBLE);
        else
            btnRate.setVisibility(View.GONE);
    }

    public void volleyRequest(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        comments = new ArrayList<>();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                comments.clear();
                average = 0.0f;

                try {
                    Log.d("TAG", "ASD");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject eventINF = jsonArray.getJSONObject(i);
                        commentUsername = eventINF.getString("username");
                        Log.d("TAG", commentUsername);
                        commentText = eventINF.getString("comment");
                        Log.d("TAG", commentText);
                        encodedProfileImage = eventINF.getString("profileImage");
                        rateCode = eventINF.getInt("recommend");
                        date = eventINF.getString("date");
                        average += rateCode;

                        Log.d("TAG", "GOD HELP ME!");
                        byte[] decodedImage = Base64.decode(encodedProfileImage.getBytes(), Base64.NO_WRAP);
                        profileImage = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

                        if(rateCode == 1)
                            rate = BitmapFactory.decodeResource(getResources(), R.drawable.yes);
                        else
                            rate = BitmapFactory.decodeResource(getResources(), R.drawable.not);

                        if(commentUsername.equals(username))
                            rated = true;

                        comment = new Comments(commentUsername, commentText, profileImage, rate, date);
                        Log.d("TAG", "Comment Username: " + comment.getUsername());
                        comments.add(comment);
                        Log.d("TAG", comments.get(0).getUsername());

                        adapter = new CustomCommentAdapter(PastEventCommentActivity.this, comments);
                        adapter.notifyDataSetChanged();
                        lvComment.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                average = (average / comments.size() * 100);
                fillDetails();
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        PastEventCommentRequest request = new PastEventCommentRequest(event_id, listener);
        RequestQueue queue = Volley.newRequestQueue(PastEventCommentActivity.this);
        queue.add(request);
    }
}
