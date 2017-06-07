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
 * Created by Nastala on 22.12.2016.
 */

public class CustomEventAdapter extends BaseAdapter {
    private ArrayList<Events> events;
    private LayoutInflater inflater;

    public CustomEventAdapter(Activity activity, ArrayList<Events> events) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        view = inflater.inflate(R.layout.event_layout, null);
        Events event = events.get(position);

        CircleImageView civEventImage = (CircleImageView) view.findViewById(R.id.civEventImage);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvCity = (TextView) view.findViewById(R.id.tvCity);

        civEventImage.setImageBitmap(event.getImage());
        tvTitle.setText(event.getTitle());
        tvDate.setText(event.getDate());
        tvCity.setText(event.getCity());

        return view;
    }


}
