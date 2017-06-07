package com.example.nastala.eventguide;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nastala on 14.03.2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d("Token", "TOKEN ON REFRESH");
        registerToken(token);
    }

    private void registerToken(String token) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    int id = jsonObject.getInt("id");

                    if(success){
                        Log.d("Token", "TOKEN Registered");
                        Log.d("Token", "TOKEN ID: " + id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        FirebaseTokenRegisterRequest request = new FirebaseTokenRegisterRequest(token, listener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
