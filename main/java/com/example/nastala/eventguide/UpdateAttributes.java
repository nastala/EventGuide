package com.example.nastala.eventguide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateAttributes extends AppCompatActivity {
    private String attributes, username;
    private CheckBox cbMusic, cbTheater, cbCinema, cbStandup, cbGame;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_attributes);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));
        getSupportActionBar().setTitle("Attribute Customize");

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        cbMusic = (CheckBox) findViewById(R.id.cbMusic);
        cbTheater = (CheckBox) findViewById(R.id.cbTheater);
        cbCinema = (CheckBox) findViewById(R.id.cbCinema);
        cbStandup = (CheckBox) findViewById(R.id.cbStandup);
        cbGame = (CheckBox) findViewById(R.id.cbGame);

        Intent i = getIntent();
        username = i.getStringExtra("username");
        attributes = "";


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillAttributes();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success){
                                Intent i = new Intent(UpdateAttributes.this, MainMenu.class);
                                i.putExtra("username", username);
                                startActivity(i);
                            }
                            else
                                Toast.makeText(UpdateAttributes.this, "An Error Occured.", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                UpdateAttributesRequest updateAttributesRequest = new UpdateAttributesRequest(attributes, username, listener);
                RequestQueue requestQueue = Volley.newRequestQueue(UpdateAttributes.this);
                requestQueue.add(updateAttributesRequest);
            }
        });
    }

    public void fillAttributes(){
        if(cbMusic.isChecked())
            attributes += "music,";
        if(cbTheater.isChecked())
            attributes += "theater,";
        if(cbCinema.isChecked())
            attributes += "cinema,";
        if(cbStandup.isChecked())
            attributes += "standup,";
        if(cbGame.isChecked())
            attributes += "game,";
    }
}
