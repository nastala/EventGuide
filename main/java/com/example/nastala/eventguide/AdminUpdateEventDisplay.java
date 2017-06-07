package com.example.nastala.eventguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class AdminUpdateEventDisplay extends AppCompatActivity {
    private String title, detail, date, city, encodedImage, currentDate, type, encodeSelectedImage;
    private int event_id;
    private Bitmap image;
    private Events event;
    private ListView lvEvent;
    private CustomEventAdapter customEventAdapter;
    private ArrayList<Events> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_event_display);

        AdminUpdateEventDisplay.this.setTitle("ADMIN EVENT UPDATE DISPLAY");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));

        lvEvent = (ListView) findViewById(R.id.lvEvent);

        volleyRequest();
    }

    public void volleyRequest() {
        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        events = new ArrayList<>();

        final Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG", "ASD");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject eventINF = jsonArray.getJSONObject(i);
                        event_id = eventINF.getInt("event_id");
                        title = eventINF.getString("title");
                        detail = eventINF.getString("detail");
                        date = eventINF.getString("date");
                        city = eventINF.getString("city");
                        type = eventINF.getString("type");

                        encodedImage = eventINF.getString("image");
                        byte[] decodedImage = Base64.decode(encodedImage.getBytes(), Base64.NO_WRAP);
                        image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

                        event = new Events(event_id, title, detail, date, city, image);
                        events.add(event);

                        customEventAdapter = new CustomEventAdapter(AdminUpdateEventDisplay.this, events);
                        customEventAdapter.notifyDataSetChanged();
                        lvEvent.setAdapter(customEventAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        EventRequest eventRequest = new EventRequest(currentDate, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(AdminUpdateEventDisplay.this);
        requestQueue.add(eventRequest);

        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap selectedImage = events.get(position).getImage();
                if(!(selectedImage.isRecycled()))
                    encodeSelectedImage = ImageBase64.encode(selectedImage);

                Intent i = new Intent(AdminUpdateEventDisplay.this, AdminUpdateEvent.class);

                i.putExtra("event_id", events.get(position).getEvent_id() + "");
                i.putExtra("title", events.get(position).getTitle());
                i.putExtra("detail", events.get(position).getDetail());
                i.putExtra("date", events.get(position).getDate());
                i.putExtra("image", encodeSelectedImage);
                i.putExtra("type", type);
                i.putExtra("city", events.get(position).getCity());
                Log.d("TAG", event_id + "");

                startActivity(i);
            }
        });
    }
}
