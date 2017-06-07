package com.example.nastala.eventguide;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nastala on 20.12.2016.
 */

public class CustomAdapter extends BaseAdapter{

    private ArrayList<Settings> settings;
    private LayoutInflater inflater;

    public CustomAdapter(Activity activity, ArrayList<Settings> settings) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.settings = settings;
    }


    @Override
    public int getCount() {
        return settings.size();
    }

    @Override
    public Object getItem(int position) {
        return settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        Settings setting = settings.get(position);

        if(setting.getUsername().equals("") == false){
            view = inflater.inflate(R.layout.profile_layout, null);

            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
            CircleImageView civImage = (CircleImageView) view.findViewById(R.id.civImage);

            civImage.setImageBitmap(setting.getImage());
            tvName.setText(setting.getName());
            tvUsername.setText(setting.getUsername());
        }
        else{
            view = inflater.inflate(R.layout.settings_layout, null);
            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            CircleImageView civImage = (CircleImageView) view.findViewById(R.id.civImage);
            civImage.setImageBitmap(setting.getImage());
            tvName.setText(setting.getName());
        }

        return view;
    }
}
