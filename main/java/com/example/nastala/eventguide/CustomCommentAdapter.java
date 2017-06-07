package com.example.nastala.eventguide;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nastala on 22.05.2017.
 */

public class CustomCommentAdapter extends BaseAdapter {
    private ArrayList<Comments> comments;
    private LayoutInflater inflater;

    public CustomCommentAdapter(Activity activity, ArrayList<Comments> comments) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.comment_layout, null);
        Comments comment = comments.get(position);

        CircleImageView civProfileImage = (CircleImageView) view.findViewById(R.id.civProfileImage);
        CircleImageView civRate = (CircleImageView) view.findViewById(R.id.civRate);
        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);

        civProfileImage.setImageBitmap(comment.getProfileImage());
        civRate.setImageBitmap(comment.getRate());
        tvUsername.setText(comment.getUsername());
        tvComment.setText(comment.getComment());
        tvDate.setText(comment.getDate());

        return view;
    }
}
