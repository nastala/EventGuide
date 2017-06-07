package com.example.nastala.eventguide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateCity extends AppCompatActivity {

    private final String[] city = {"İstanbul", "Ankara", "İzmir", "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray", "Amasya",
            "Antalya", "Ardahan", "Artvin", "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt", "Bilecik", "Bingöl", "Bitlis",
            "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan",
            "Erzurum", "Eskişehir" ,"Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Iğdır", "Isparta", "Kahramanmaraş", "Karabük",
            "Karaman", "Kars", "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir", "Kilis", "Kocaeli", "Konya", "Kütahya",
            "Malatya", "Manisa", "Mardin", "Mersin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun",
            "Siirt", "Sinop", "Sivas", "Şırnak", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yalova", "Yozgat",
            "Zonguldak"};
    private String username;
    private ArrayAdapter<String> dataAdapterForCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_city);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));
        getSupportActionBar().setTitle("City Customize");

        final Spinner spnCity = (Spinner) findViewById(R.id.spnCity);
        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);

        Intent i = getIntent();
        username = i.getStringExtra("username");

        dataAdapterForCity = new ArrayAdapter<String>(UpdateCity.this, android.R.layout.simple_spinner_item, city);
        spnCity.setAdapter(dataAdapterForCity);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCity = spnCity.getSelectedItem().toString();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success = jsonObject.getBoolean("success");

                            if(success){
                                Toast.makeText(UpdateCity.this, "Updated City Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(UpdateCity.this, MainMenu.class);
                                i.putExtra("username", username);
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                UpdateCityRequest updateCityRequest = new UpdateCityRequest(selectedCity, username, listener);
                RequestQueue requestQueue = Volley.newRequestQueue(UpdateCity.this);
                requestQueue.add(updateCityRequest);
            }
        });
    }
}
