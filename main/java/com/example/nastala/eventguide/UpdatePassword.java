package com.example.nastala.eventguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePassword extends AppCompatActivity {
    private String password, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));
        getSupportActionBar().setTitle("Password Customize");

        Intent i = getIntent();
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");

        Log.d("TAG", password);
        Log.d("TAG", username);

        final EditText edtCurrentPassword = (EditText) findViewById(R.id.edtCurrentPassword);
        final EditText edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String currentPassword = edtCurrentPassword.getText().toString();
                final String newPassword = edtNewPassword.getText().toString();

                if(currentPassword.equals(password)){
                    if(newPassword.equals("") == false){
                        Response.Listener<String> listener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Boolean success = jsonObject.getBoolean("success");

                                    if(success){
                                        Toast.makeText(UpdatePassword.this, "Updated Password Successfully",
                                                Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(UpdatePassword.this, MainMenu.class);
                                        i.putExtra("username", username);
                                        startActivity(i);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        UpdatePasswordRequest updatePassword = new UpdatePasswordRequest(username, newPassword, listener);
                        RequestQueue requestQueue = Volley.newRequestQueue(UpdatePassword.this);
                        requestQueue.add(updatePassword);
                    }else
                        Toast.makeText(UpdatePassword.this, "Fill New Password Field.",
                                Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(UpdatePassword.this, "Entered Wrong Password.",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }
}
