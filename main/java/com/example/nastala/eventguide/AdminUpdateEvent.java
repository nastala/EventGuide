package com.example.nastala.eventguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Spinner;
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

public class AdminUpdateEvent extends AppCompatActivity {
    private String title, detail, date, city, encodedImage, type, broughtEncodedImage;
    private Bitmap image;
    private CircleImageView civEventImage;
    private TextView etDetail, etDate, etType, etTitle;
    private Button btnUpdateEvent;
    private Spinner spnCity;
    private int cityPosition, event_id;
    private String[] cities;
    private ArrayAdapter<String> dataAdapterForCities;
    private final int GALLERY_REQUEST = 12122;
    private GalleryPhoto galleryPhoto;
    private String selectedPhoto;
    private Boolean checkChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_event);

        AdminUpdateEvent.this.setTitle("ADMIN EVENT UPDATE");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));

        checkChanged = false;

        Intent i = getIntent();
        title = i.getStringExtra("title");
        detail = i.getStringExtra("detail");
        date = i.getStringExtra("date");
        city = i.getStringExtra("city");
        type = i.getStringExtra("type");
        event_id = Integer.parseInt(i.getStringExtra("event_id"));
        broughtEncodedImage = i.getStringExtra("image");

        galleryPhoto = new GalleryPhoto(getApplicationContext());

        byte[] decodedImage = Base64.decode(broughtEncodedImage.getBytes(), Base64.NO_WRAP);
        image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

        btnUpdateEvent = (Button) findViewById(R.id.btnUpdateEvent);
        etTitle = (TextView) findViewById(R.id.etTitle);
        etDetail = (TextView) findViewById(R.id.etDetail);
        etDate = (TextView) findViewById(R.id.etDate);
        spnCity = (Spinner) findViewById(R.id.spnCity);
        etType = (TextView) findViewById(R.id.etType);
        civEventImage = (CircleImageView) findViewById(R.id.civEventImage);

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

        fillDetails();

        civEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

        btnUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEditTexts() || checkChanged){
                    Log.d("TAG", checkEditTexts() + "" + checkChanged);
                    Log.d("TAG", "Admin Changed Something");

                    if(!(image.isRecycled()))
                        encodedImage = ImageBase64.encode(image);
                    title = etTitle.getText().toString();
                    detail = etDetail.getText().toString();
                    date = etDate.getText().toString();
                    city = spnCity.getSelectedItem().toString();
                    type = etType.getText().toString();

                    Log.d("TAG", title);
                    Log.d("TAG", detail);
                    Log.d("TAG", encodedImage);
                    Log.d("TAG", date);
                    Log.d("TAG", city);
                    Log.d("TAG", type);
                    Log.d("TAG", event_id + "");

                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean success = jsonObject.getBoolean("success");

                                if(success){
                                    Toast.makeText(AdminUpdateEvent.this, "Event Updated Successfully.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    AdminUpdateEventRequest request = new AdminUpdateEventRequest(event_id, title, detail, date, city, type, encodedImage,
                            listener);
                    RequestQueue queue = Volley.newRequestQueue(AdminUpdateEvent.this);
                    queue.add(request);
                }
                else
                    Toast.makeText(AdminUpdateEvent.this, "YOU DID NOT CHANGE ANYTHING", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkEditTexts(){
        if(etTitle.getText().toString().equals(title) && etDetail.getText().toString().equals(detail) &&
                etDate.getText().toString().equals(date) && etType.getText().toString().equals(type) &&
                spnCity.getSelectedItem().toString().equals(city))
            return false;
        else
            return true;
    }

    public void fillDetails(){
        findCityPosition();
        etTitle.setText(title);
        etDetail.setText(detail);
        etDate.setText(date);
        etType.setText(type);
        civEventImage.setImageBitmap(image);
        spnCity.setSelection(cityPosition);
    }

    public void findCityPosition(){
        for(int i = 0; i < spnCity.getCount(); i++){
            if(spnCity.getItemAtPosition(i).toString().equals(city)){
                cityPosition = i;
                Log.d("TAG", "POSITION = " + i);
                break;
            }
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
                    checkChanged = true;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

