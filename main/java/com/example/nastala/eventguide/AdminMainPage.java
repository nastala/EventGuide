package com.example.nastala.eventguide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));
        this.setTitle("EVENT GUIDE ADMIN PANEL");

        Button btnAddEvent = (Button) findViewById(R.id.btnAddEvent);
        Button btnUpdateEvent = (Button) findViewById(R.id.btnUpdateEvent);

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminMainPage.this, AdminAddNewEvent.class);
                startActivity(i);
            }
        });

        btnUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminMainPage.this, AdminUpdateEventDisplay.class);
                startActivity(i);
            }
        });
    }
}
