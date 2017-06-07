package com.example.nastala.eventguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String name, username, city, encodedImage, password, attributes;
    private Bitmap image;
    private int age;
    private TextView tvName, tvUsername;
    private CircleImageView ivProfile;
    private ProgressDialog progressDialog;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(MainMenu.this);
        progressDialog.setMessage("Setting up Profile Details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hview = navigationView.getHeaderView(0);
        tvName = (TextView) hview.findViewById(R.id.tvName);
        tvUsername = (TextView) hview.findViewById(R.id.tvUsername);
        ivProfile = (CircleImageView) hview.findViewById(R.id.civProfile);

        navigationView.setNavigationItemSelectedListener(this);

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", token);

        takeDetails();

        EventFragment eventFragment = EventFragment.newInstance(username);
        Log.d("TAG", username);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativelayout_for_fragment, eventFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void firebaseUsernameUpdate(String username, String token){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");

                    if(success){
                        Log.d("TOKEN", "USERNAME UPDATED");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        FirebaseUsernameUpdateRequest firebaseUsernameUpdateRequest = new FirebaseUsernameUpdateRequest(username, token, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(MainMenu.this);
        requestQueue.add(firebaseUsernameUpdateRequest);
    }

    public void takeDetails(){
        Intent i = getIntent();
        username = i.getStringExtra("username");
        firebaseUsernameUpdate(username, token);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    Log.d("TAG", "WTF");

                    if(success){
                        name = jsonObject.getString("name");
                        city = jsonObject.getString("city");
                        encodedImage = jsonObject.getString("encodedImage");
                        attributes = jsonObject.getString("attributes");
                        age = jsonObject.getInt("age");
                        password = jsonObject.getString("password");
                        Log.d("TAG", "Response ÇALIŞIYOR");
                        tvName.setText(name);
                        tvUsername.setText(username);

                        byte[] decodedImage = Base64.decode(encodedImage.getBytes(), Base64.NO_WRAP);
                        image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + username;
                        saveBitmap(image, path);

                        image = BitmapFactory.decodeFile(path);
                        ivProfile.setImageBitmap(Bitmap.createScaledBitmap(image, 120, 120, false));
                    }
                    else
                        Log.d("TAG", "Response GG");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        };

        TakeDetailsRequest takeDetailsRequest = new TakeDetailsRequest(username, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(MainMenu.this);
        requestQueue.add(takeDetailsRequest);
    }

    private void saveBitmap(Bitmap bitmap,String path){
        if(bitmap!=null){
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(MainMenu.this, SearchResultActivity.class);
                i.putExtra("query", query);
                startActivity(i);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_events) {
            EventFragment eventFragment = EventFragment.newInstance(username);
            Log.d("TAG", username);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.relativelayout_for_fragment, eventFragment).commit();
            
        } else if (id == R.id.nav_myevents) {
            Profile profile = new Profile(image, name, username, city, age, password, attributes);
            MyEventFragment myEventFragment = MyEventFragment.newInstance(profile);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.relativelayout_for_fragment, myEventFragment).commit();

        } else if (id == R.id.nav_profile) {
            Profile profile = new Profile(image, name, username, city, age, password, attributes);
            ProfileFragment profileFragment = ProfileFragment.newInstance(profile);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.relativelayout_for_fragment, profileFragment).commit();

        } else if (id == R.id.nav_pastevents) {
            PastEventFragment pastEventFragment = PastEventFragment.newInstance(username);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.relativelayout_for_fragment, pastEventFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
