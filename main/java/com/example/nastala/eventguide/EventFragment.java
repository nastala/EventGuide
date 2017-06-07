package com.example.nastala.eventguide;


import android.app.Activity;
import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {
    private static final String ARG_USERNAME = "username";
    private String username;


    private String title, detail, date, city, encodedImage, currentDate, type;
    private int event_id, participantNumber;
    private Bitmap image;
    private Events event;
    private ListView lvEvent;
    private CustomEventAdapter customEventAdapter;
    private ProgressDialog progressDialog;
    private Boolean success;
    private TextView tvParticipate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvParticipantNumber;

    private ArrayList<Events> events;

    public EventFragment() {
        // Required empty public constructor

    }

    public static EventFragment newInstance(String username){
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        if(getArguments() != null){
            getActivity().setTitle("Events");

            username = getArguments().getString(ARG_USERNAME);
        }
        success = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        progressDialog = new ProgressDialog(EventFragment.this.getContext());
        progressDialog.setMessage("Configuring events. This progress may take a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        lvEvent = (ListView) view.findViewById(R.id.lvEvent);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        lvEvent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });


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

        volleyRequest();

        return view;
    }

    public void volleyRequest(){
        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        events = new ArrayList<>();

        final Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG", "ASD");
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

                        customEventAdapter = new CustomEventAdapter(EventFragment.this.getActivity(), events);
                        customEventAdapter.notifyDataSetChanged();
                        lvEvent.setAdapter(customEventAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }

        };
        EventRequest eventRequest = new EventRequest(currentDate, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(EventFragment.this.getActivity());
        requestQueue.add(eventRequest);

        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(events.get(position), EventFragment.this.getActivity());
            }
        });
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
        RequestQueue queue = Volley.newRequestQueue(EventFragment.this.getActivity());
        queue.add(participantNumberRequest);
    }

    public void showDialog(Events event, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(EventFragment.this.getActivity());
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

        //checkParticipant(username, event.getEvent_id());
    }

    public void checkParticipant(String username, int eventID){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getBoolean("success");
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
        RequestQueue requestQueue = Volley.newRequestQueue(EventFragment.this.getActivity());
        requestQueue.add(checkParticipateRequest);
    }



}
