package com.example.nastala.eventguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.interfaces.StaticImageLoader;

import java.util.ArrayList;
import java.util.Dictionary;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_CITY = "city";
    private static final String ARG_AGE = "age";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_PASSWORD = "password";

    // TODO: Rename and change types of parameters
    private String name;
    private String username;
    private String city;
    private String password;
    private int age;
    private Bitmap image;
    private ArrayList<Settings> settings;


    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(Profile profile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, profile.getName());
        args.putString(ARG_USERNAME, profile.getUsername());
        args.putString(ARG_CITY, profile.getCity());
        args.putInt(ARG_AGE, profile.getAge());
        args.putParcelable(ARG_IMAGE, profile.getProfileImage());
        args.putString(ARG_PASSWORD, profile.getPassword());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getActivity().setTitle("Profile Settings");

            name = getArguments().getString(ARG_NAME);
            username = getArguments().getString(ARG_USERNAME);
            city = getArguments().getString(ARG_CITY);
            age = getArguments().getInt(ARG_AGE);
            image = getArguments().getParcelable(ARG_IMAGE);
            password = getArguments().getString(ARG_PASSWORD);
            Log.d("TAG", password);

            settings = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        settings.add(new Settings(image, name, username));
        settings.add(new Settings(BitmapFactory.decodeResource(getResources(), R.drawable.password), "Password"));
        settings.add(new Settings(BitmapFactory.decodeResource(getResources(), R.drawable.attributes), "Attributes"));
        settings.add(new Settings(BitmapFactory.decodeResource(getResources(), R.drawable.cities), "City"));

        ListView lvSettings = (ListView) view.findViewById(R.id.lvSettings);

        CustomAdapter adapterSettings = new CustomAdapter(ProfileFragment.this.getActivity(), settings);
        lvSettings.setAdapter(adapterSettings);

        lvSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent i = new Intent(ProfileFragment.this.getActivity(), ProfileSetting.class);
                    i.putExtra("name", name);
                    i.putExtra("username", username);
                    startActivity(i);

                } else if(position == 1){
                    Intent i = new Intent(ProfileFragment.this.getActivity(), UpdatePassword.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    startActivity(i);

                } else if(position == 2){
                    Intent i = new Intent(ProfileFragment.this.getActivity(), UpdateAttributes.class);
                    i.putExtra("username", username);
                    startActivity(i);

                } else if(position == 3){
                    Intent i = new Intent(ProfileFragment.this.getActivity(), UpdateCity.class);
                    i.putExtra("username", username);
                    startActivity(i);

                }
            }
        });

        return view;
    }

}
