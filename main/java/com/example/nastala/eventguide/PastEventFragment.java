package com.example.nastala.eventguide;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "username";

    // TODO: Rename and change types of parameters
    private String username;
    private int event_id, participantNumber;
    private String title, detail, date, city, type, encodedImage;
    private Bitmap image, dialogImage;
    private Events event;
    private ArrayList<Events> events;
    private ListView lvPastEvent;
    private CustomEventAdapter customEventAdapter;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvParticipantNumber, tvParticipate;

    public PastEventFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PastEventFragment newInstance(String username) {
        PastEventFragment fragment = new PastEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            getActivity().setTitle("Past Events");

            events = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_past_event, container, false);

        lvPastEvent = (ListView) view.findViewById(R.id.lvPastEvent);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        progressDialog = new ProgressDialog(PastEventFragment.this.getActivity());
        progressDialog.setMessage("Past events are loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyRequest();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        dialogImage = null;

        volleyRequest();

        return view;
    }

    public void volleyRequest(){
        events.clear();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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

                        byte[] decodedImage = Base64.decode(encodedImage.getBytes(), Base64.NO_WRAP);
                        image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

                        event = new Events(event_id, title, detail, date, city, image);
                        events.add(event);
                        logEvents();
                    }

                    if(events != null){
                        customEventAdapter = new CustomEventAdapter(PastEventFragment.this.getActivity(), events);
                        customEventAdapter.notifyDataSetChanged();
                        lvPastEvent.setAdapter(customEventAdapter);
                    } else
                        Toast.makeText(PastEventFragment.this.getActivity(), "You have not registered for previous events.",
                                Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        PastEventRequest pastEventRequest = new PastEventRequest(username, currentDate, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(PastEventFragment.this.getActivity());
        requestQueue.add(pastEventRequest);

        lvPastEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(events.get(position), PastEventFragment.this.getActivity());
            }
        });
    }

    public void showDialog(final Events event, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(PastEventFragment.this.getActivity());
        View mView = activity.getLayoutInflater().inflate(R.layout.event_detail_layout, null);

        TextView tvTitle = (TextView) mView.findViewById(R.id.tvTitle);
        TextView tvDetail = (TextView) mView.findViewById(R.id.tvDetail);
        TextView tvCity = (TextView) mView.findViewById(R.id.tvCity);
        TextView tvDate = (TextView) mView.findViewById(R.id.tvDate);
        ImageView ivEventImage = (ImageView) mView.findViewById(R.id.ivEventImage);
        tvParticipate = (TextView) mView.findViewById(R.id.tvParticipate);
        tvParticipantNumber = (TextView) mView.findViewById(R.id.tvParticipantNumber);

        getParticipantNumber(event.getEvent_id());

        tvTitle.setText(event.getTitle());
        tvDetail.setText(event.getDetail());
        tvCity.setText(event.getCity());
        tvDate.setText(event.getDate());

        tvParticipate.setText("Click here to see comments & votes");

        if(!(event.getImage().isRecycled()))
            dialogImage = event.getImage().copy(event.getImage().getConfig(), false);
        ivEventImage.setImageBitmap(dialogImage);

        builder.setView(mView);
        final AlertDialog dialog = builder.create();

        tvParticipate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PastEventFragment.this.getActivity(), PastEventCommentActivity.class);
                i.putExtra("event_id", event.getEvent_id() + "");
                i.putExtra("username", username);
                i.putExtra("title", event.getTitle());
                i.putExtra("image", ImageBase64.encode(dialogImage));
                dialog.dismiss();
                startActivity(i);
            }
        });

        dialog.show();
    }

    public void checkParticipant(String username, int eventID){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    Log.d("TAGResponse", success.toString());
                    if(success)
                        tvParticipate.setText("You have already registered for this event.");
                    else tvParticipate.setText("Participate");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        CheckParticipateRequest checkParticipateRequest = new CheckParticipateRequest(username, eventID, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(PastEventFragment.this.getActivity());
        requestQueue.add(checkParticipateRequest);
    }

    public void logEvents(){
        for(int i = 0; i < events.size(); i++){
            Log.d("Event Tag", i + "Position" + events.get(i).getTitle());
        }
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
        RequestQueue queue = Volley.newRequestQueue(PastEventFragment.this.getActivity());
        queue.add(participantNumberRequest);
    }
}
