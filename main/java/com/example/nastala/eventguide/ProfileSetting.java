package com.example.nastala.eventguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetting extends AppCompatActivity {

    private String username, name;
    private Bitmap image;
    final int GALLERY_REQUEST = 12122;
    GalleryPhoto galleryPhoto;
    private String selectedPhoto;
    private CircleImageView civProfileImage;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));
        getSupportActionBar().setTitle("Name & Image Customize");

        Intent i = getIntent();
        name = i.getStringExtra("name");
        username = i.getStringExtra("username");
        Log.d("TAG", username);

        civProfileImage = (CircleImageView) findViewById(R.id.civProfileImage);
        Button btnProfileImage = (Button) findViewById(R.id.btnProfileImage);
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        tvName = (TextView) findViewById(R.id.tvName);

        image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + username);
        civProfileImage.setImageBitmap(image);
        tvName.setText(name);

        btnProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSetting.this, UpdateName.class);
                i.putExtra("name", name);
                i.putExtra("username", username);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProfileSetting.this, MainMenu.class);
        i.putExtra("username", username);
        Log.d("TAG", username);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                selectedPhoto = galleryPhoto.getPath();

                try {
                    final ProgressDialog progressDialog = new ProgressDialog(ProfileSetting.this);
                    progressDialog.setMessage("Updating Image...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Bitmap newImage = ImageLoader.init().from(selectedPhoto).requestSize(150, 150).getBitmap();
                    civProfileImage.setImageBitmap(newImage);

                    String encodedImage = ImageBase64.encode(newImage);
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean success = jsonObject.getBoolean("success");

                                if(success){
                                    Toast.makeText(ProfileSetting.this, "Updated Image Successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    Intent i = new Intent(ProfileSetting.this, MainMenu.class);
                                    i.putExtra("username", username);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(ProfileSetting.this, "Failed", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                    };
                    UpdatePIRequest updatePIRequest = new UpdatePIRequest(username, encodedImage, listener);
                    RequestQueue requestQueue = Volley.newRequestQueue(ProfileSetting.this);
                    requestQueue.add(updatePIRequest);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
