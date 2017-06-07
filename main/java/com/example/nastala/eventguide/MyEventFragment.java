package com.example.nastala.eventguide;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
 * Use the {@link MyEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "username";
    private static final String ARG_CITY = "city";
    private static final String ARG_ATTRIBUTES = "attributes";

    // TODO: Rename and change types of parameters
    private String username;
    private String userCity;
    private String[] attributes;
    private ViewPager viewPager;
    private SwipeAdapter swipeAdapter;

    public MyEventFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyEventFragment newInstance(Profile profile) {
        MyEventFragment fragment = new MyEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, profile.getUsername());
        args.putString(ARG_CITY, profile.getCity());
        args.putString(ARG_ATTRIBUTES, profile.getAttributes());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getActivity().setTitle("My Events");

            viewPager = null;

            username = getArguments().getString(ARG_USERNAME);
            userCity = getArguments().getString(ARG_CITY);
            attributes = getArguments().getString(ARG_ATTRIBUTES).split(",");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_my_event, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        FragmentManager fm = this.getChildFragmentManager();
        swipeAdapter = new SwipeAdapter(fm, username, userCity, attributes);
        swipeAdapter.notifyDataSetChanged();
        viewPager.setAdapter(swipeAdapter);

        return view;
    }


}
