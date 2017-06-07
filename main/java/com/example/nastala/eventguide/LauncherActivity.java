package com.example.nastala.eventguide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.RelativeLayout;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_launcher);
        relativeLayout.setBackgroundResource(R.drawable.eventlogo);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LauncherActivity.this.finish();
    }
}
