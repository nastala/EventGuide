package com.example.nastala.eventguide;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilteredEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilteredEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "username";
    private static final String ARG_USERCITY = "userCity";
    private static final String ARG_ATTRIBUTES = "attributes";

    // TODO: Rename and change types of parameters
    private String username;
    private String userCity;
    private String[] attributes;
    private int event_id, participantNumber;
    private String title, detail, date, city, type, encodedImage;
    private Bitmap image;
    private Events event;
    private ArrayList<Events> events;
    private ListView lvMyEvent;
    private CustomEventAdapter customEventAdapter;
    private TextView tvParticipate, tvParticipantNumber;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;

    public FilteredEventFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FilteredEventFragment newInstance(String username, String userCity, String[] attributes) {
        FilteredEventFragment fragment = new FilteredEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_USERCITY, userCity);
        args.putStringArray(ARG_ATTRIBUTES, attributes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            userCity = getArguments().getString(ARG_USERCITY);
            attributes = getArguments().getStringArray(ARG_ATTRIBUTES);

            events = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_filtered_event, container, false);

        lvMyEvent = (ListView) view.findViewById(R.id.lvMyEvent);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        progressDialog = new ProgressDialog(FilteredEventFragment.this.getActivity());
        progressDialog.setMessage("Events are filtering. Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        volleyRequest();

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

        return view;
    }

    public void volleyRequest(){

        events.clear();

        Log.d("TAG", username + " ");

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

                        for (int j = 0; j < attributes.length; j++){
                            if(userCity.equals(city) && attributes[j].equals(type)){
                                event = new Events(event_id, title, detail, date, city, image);
                                events.add(event);
                                Log.d("BULDUK", event.getTitle());
                                break;
                            }

                            if(events != null) {
                                customEventAdapter = new CustomEventAdapter(FilteredEventFragment.this.getActivity(), events);
                                customEventAdapter.notifyDataSetChanged();
                                lvMyEvent.setAdapter(null);
                                lvMyEvent.setAdapter(customEventAdapter);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();

                if(events.isEmpty())
                    Toast.makeText(FilteredEventFragment.this.getActivity(),
                            "Sorry! We have no events available for you. Check it later.", Toast.LENGTH_SHORT).show();
            }
        };

        EventRequest eventRequest = new EventRequest(currentDate, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(FilteredEventFragment.this.getActivity());
        requestQueue.add(eventRequest);

        lvMyEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(events.get(position), FilteredEventFragment.this.getActivity());
            }
        });
    }

    public void showDialog(final Events event, Activity activity){
        final AlertDialog.Builder builder = new AlertDialog.Builder(FilteredEventFragment.this.getActivity());
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

        ivEventImage.setImageBitmap(event.getImage());

        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        checkParticipant(username, event.getEvent_id());

        tvParticipate.setOnClickListener(new View.OnClickListener() {
            ProgressDialog progressDialog = new ProgressDialog(FilteredEventFragment.this.getActivity());
            @Override
            public void onClick(View v) {
                if(tvParticipate.getText().equals("Participate")) {
                    progressDialog.setMessage("Registering...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(FilteredEventFragment.this.getActivity(), "Registered Event Successfully",
                                            Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(FilteredEventFragment.this.getActivity(), "Registered Event Failed",
                                            Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();
                        }
                    };

                    RegisterEventRequest registerEventRequest = new RegisterEventRequest(event.getEvent_id(), username, listener);
                    RequestQueue requestQueue = Volley.newRequestQueue(FilteredEventFragment.this.getActivity());
                    requestQueue.add(registerEventRequest);
                }
                else if(tvParticipate.getText().equals("You have already registered for this event.")){
                    progressDialog.setMessage("Removing Event...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean success = jsonObject.getBoolean("success");

                                if(success){
                                    Toast.makeText(FilteredEventFragment.this.getActivity(), "Removed Event Successfully",
                                            Toast.LENGTH_SHORT).show();
                                }else
                                    Toast.makeText(FilteredEventFragment.this.getActivity(), "Failed.",
                                            Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();
                        }
                    };

                    RemoveEventRequest removeEventRequest = new RemoveEventRequest(event.getEvent_id(), username, listener);
                    RequestQueue requestQueue = Volley.newRequestQueue(FilteredEventFragment.this.getActivity());
                    requestQueue.add(removeEventRequest);
                    Log.d("TAG", event.getEvent_id() + "");
                    Log.d("TAG", username);
                }

                dialog.dismiss();
            }
        });
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
        RequestQueue requestQueue = Volley.newRequestQueue(FilteredEventFragment.this.getActivity());
        requestQueue.add(checkParticipateRequest);
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
        RequestQueue queue = Volley.newRequestQueue(FilteredEventFragment.this.getActivity());
        queue.add(participantNumberRequest);
    }
}
