package com.example.nastala.eventguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    private EditText edtUsername, edtPassword;
    private TextView txtRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));

        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtRegister = (TextView) findViewById(R.id.txtRegister);

        setBackground();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edtUsername.getText().toString();
                final String password = edtPassword.getText().toString();

                Log.d("TAG", username);
                if(username.equals("admin")){
                    if(password.equals("admin")){
                        Intent iAdmin = new Intent(LoginActivity.this, AdminMainPage.class);
                        startActivity(iAdmin);
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Admin Password is Wrong.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if(success){
                                    String username = jsonObject.getString("username");
                                    Log.d("TAG","Bağlantı tamam");

                                    Intent i = new Intent(LoginActivity.this, MainMenu.class);
                                    i.putExtra("username", username);

                                    progressDialog.dismiss();
                                    LoginActivity.this.startActivity(i);
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Login Failed.")
                                            .setNegativeButton("Retry", null)
                                            .create().show();

                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    Log.d("TAG","Bağlantı deneme");
                    LoginRequest loginRequest = new LoginRequest(username, password, listener);
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    requestQueue.add(loginRequest);
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public void setBackground(){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_login);
        Random rand = new Random();
        int n = rand.nextInt(3);
        switch (n){
            case 0:
                relativeLayout.setBackgroundResource(R.drawable.kapadokyabg);
                edtUsername.setTextColor(Color.parseColor("#000000"));
                edtUsername.setHintTextColor(Color.parseColor("#000000"));
                edtPassword.setTextColor(Color.parseColor("#000000"));
                edtPassword.setHintTextColor(Color.parseColor("#000000"));
                txtRegister.setTextColor(Color.parseColor("#000000"));
                txtRegister.setHintTextColor(Color.parseColor("#000000"));
                break;
            case 1:
                relativeLayout.setBackgroundResource(R.drawable.konserbg);
                btnLogin.setBackgroundColor(Color.parseColor("#CCC2AD"));
                edtUsername.setTextColor(Color.parseColor("#000000"));
                edtUsername.setHintTextColor(Color.parseColor("#000000"));
                edtPassword.setTextColor(Color.parseColor("#000000"));
                edtPassword.setHintTextColor(Color.parseColor("#000000"));
                txtRegister.setTextColor(Color.parseColor("#000000"));
                txtRegister.setHintTextColor(Color.parseColor("#000000"));
                break;
            default:
                relativeLayout.setBackgroundResource(R.drawable.balletbg);
                edtUsername.setTextColor(Color.parseColor("#FFFFFF"));
                edtUsername.setHintTextColor(Color.parseColor("#FFFFFF"));
                edtPassword.setTextColor(Color.parseColor("#FFFFFF"));
                edtPassword.setHintTextColor(Color.parseColor("#FFFFFF"));
                txtRegister.setTextColor(Color.parseColor("#FFFFFF"));
                txtRegister.setHintTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }
}
