package com.example.nastala.eventguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchResultActivity extends AppCompatActivity {
    private ListView lvEvent;
    private int event_id, participantNumber;
    private String title, detail, date, city, type, encodedImage, query;
    private Bitmap image;
    private Events event;
    private ArrayList<Events> events;
    private CustomEventAdapter customEventAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvParticipate, tvParticipantNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        SearchResultActivity.this.setTitle("Search Results");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));

        Intent i = getIntent();
        query = i.getStringExtra("query");

        events = new ArrayList<>();
        lvEvent = (ListView) findViewById(R.id.lvEvent);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        lvEvent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });

        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(events.get(position), SearchResultActivity.this);
            }
        });

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

        volleyRequest();
    }

    public void volleyRequest(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    events.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject eventINF = jsonArray.getJSONObject(i);
                        event_id = eventINF.getInt("event_id");
                        title = eventINF.getString("title");
                        detail = eventINF.getString("detail");
                        date = eventINF.getString("date");
                        city = eventINF.getString("city");
                        type = eventINF.getString("type");
                        encodedImage = eventINF.getString("image");
                        Log.d("TAG", "Event finded! Title: " + title);

                        byte[] decodedImage = Base64.decode(encodedImage.getBytes(), Base64.NO_WRAP);
                        image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

                        event = new Events(event_id, title, detail, date, city, image);
                        events.add(event);

                        customEventAdapter = new CustomEventAdapter(SearchResultActivity.this, events);
                        customEventAdapter.notifyDataSetChanged();
                        lvEvent.setAdapter(customEventAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);

                if(events.isEmpty())
                    Toast.makeText(SearchResultActivity.this,
                            "Sorry! No event matched", Toast.LENGTH_SHORT).show();
            }
        };

        Log.d("TAG", query + " " + currentDate);
        SearchResultRequest request = new SearchResultRequest(query, currentDate, listener);
        RequestQueue queue = Volley.newRequestQueue(SearchResultActivity.this);
        queue.add(request);
    }

    public void showDialog(Events event, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultActivity.this);
        View mView = activity.getLayoutInflater().inflate(R.layout.event_detail_layout, null);

        TextView tvTitle = (TextView) mView.findViewById(R.id.tvTitle);
        TextView tvDetail = (TextView) mView.findViewById(R.id.tvDetail);
        TextView tvCity = (TextView) mView.findViewById(R.id.tvCity);
        TextView tvDate = (TextView) mView.findViewById(R.id.tvDate);
        ImageView ivEventImage = (ImageView) mView.findViewById(R.id.ivEventImage);
        tvParticipate = (TextView) mView.findViewById(R.id.tvParticipate);
        tvParticipantNumber = (TextView) mView.findViewById(R.id.tvParticipantNumber);

        tvTitle.setText(event.getTitle());
        tvDetail.setText(event.getDetail());
        tvCity.setText(event.getCity());
        tvDate.setText(event.getDate());

        ivEventImage.setImageBitmap(event.getImage());

        getParticipantNumber(event.getEvent_id());

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getParticipantNumber(int event_id){
        Log.d("event_id", event_id + "");
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    participantNumber = jsonObject.getInt("participantNumber");
                    Log.d("event_id", participantNumber + "");
                    tvParticipantNumber.setText(participantNumber + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ParticipantNumberRequest participantNumberRequest = new ParticipantNumberRequest(event_id, listener);
        RequestQueue queue = Volley.newRequestQueue(SearchResultActivity.this);
        queue.add(participantNumberRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query2) {
                query = query2;
                volleyRequest();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
