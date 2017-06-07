package com.example.nastala.eventguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AdminAddNewEvent extends AppCompatActivity {
    private String title, detail, date, city, type;
    private Bitmap image;
    private final int GALLERY_REQUEST = 12122;
    private GalleryPhoto galleryPhoto;
    private String selectedPhoto, encodedImage;
    private CircleImageView civEventImage;
    private boolean imageChanged;
    private String[] cities;
    private ArrayAdapter<String> dataAdapterForCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_event);

        AdminAddNewEvent.this.setTitle("ADMIN EVENT ADDITION");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));

        Button btnAddNewEvent = (Button) findViewById(R.id.btnAddNewEvent);
        final EditText etTitle = (EditText) findViewById(R.id.etTitle);
        final EditText etDetail = (EditText) findViewById(R.id.etDetail);
        final EditText etType = (EditText) findViewById(R.id.etType);
        final EditText etDate = (EditText) findViewById(R.id.etDate);
        final Spinner spnCity = (Spinner) findViewById(R.id.spnCity);

        civEventImage = (CircleImageView) findViewById(R.id.civEventImage);
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        imageChanged = false;

        cities = new String[]{"İstanbul", "Ankara", "İzmir", "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray", "Amasya",
                "Antalya", "Ardahan", "Artvin", "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt", "Bilecik", "Bingöl", "Bitlis",
                "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan",
                "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Iğdır", "Isparta", "Kahramanmaraş", "Karabük",
                "Karaman", "Kars", "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir", "Kilis", "Kocaeli", "Konya", "Kütahya",
                "Malatya", "Manisa", "Mardin", "Mersin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun",
                "Siirt", "Sinop", "Sivas", "Şırnak", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yalova", "Yozgat",
                "Zonguldak"};

        dataAdapterForCities = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        spnCity.setAdapter(dataAdapterForCities);

        civEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

        btnAddNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = etTitle.getText().toString();
                detail = etDetail.getText().toString();
                date = etDate.getText().toString();
                city = spnCity.getSelectedItem().toString();
                type = etType.getText().toString();

                if(checkEditTexts() && imageChanged){
                    Log.d("ENCODED", encodedImage);

                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean success = jsonObject.getBoolean("success");
                                String message_status = jsonObject.getString("message_status");
                                Log.d("TAG", message_status);

                                if(success){
                                    Toast.makeText(AdminAddNewEvent.this, "New Event is Added", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    AdminAddNewEventRequest request = new AdminAddNewEventRequest(title, detail, date, city, type, encodedImage, listener);
                    RequestQueue queue = Volley.newRequestQueue(AdminAddNewEvent.this);
                    queue.add(request);
                }
                else
                    Toast.makeText(AdminAddNewEvent.this, "Please fill every field. Don't forget the image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkEditTexts(){
        if(title.equals("") || detail.equals("") || date.equals("") || city.equals("") || type.equals("")){
            Log.d("TAG", "There is an empty field");
            return false;
        }
        else {
            Log.d("Tag", "Every field is filled");
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                selectedPhoto = galleryPhoto.getPath();

                try {
                    image = ImageLoader.init().from(selectedPhoto).requestSize(150, 150).getBitmap();
                    civEventImage.setImageBitmap(image);
                    encodedImage = ImageBase64.encode(image);

                    imageChanged = true;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
