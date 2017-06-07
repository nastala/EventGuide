package com.example.nastala.eventguide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateName extends AppCompatActivity {

    private EditText etName;
    private Button btnUpdateName;
    private String name, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));
        getSupportActionBar().setTitle("Name Customize");

        etName = (EditText) findViewById(R.id.etName);
        btnUpdateName = (Button) findViewById(R.id.btnUpdateName);

        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                username = i.getStringExtra("username");

                name = etName.getText().toString();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success){
                                Toast.makeText(UpdateName.this, "Updated Name Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(UpdateName.this, ProfileSetting.class);
                                i.putExtra("username", username);
                                i.putExtra("name", name);
                                startActivity(i);
                            }
                            else
                                Toast.makeText(UpdateName.this, "An Error Occured.", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                UpdateNameRequest updateNameRequest = new UpdateNameRequest(name, username, listener);
                RequestQueue requestQueue = Volley.newRequestQueue(UpdateName.this);
                requestQueue.add(updateNameRequest);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateName.this.finish();
    }
}
