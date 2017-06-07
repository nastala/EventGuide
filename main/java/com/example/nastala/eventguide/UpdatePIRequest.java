package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 21.12.2016.
 */

public class UpdatePIRequest extends StringRequest {
    private static final String UPDATE_REQUEST_URL = "https://substitutive-sailor.000webhostapp.com/UpdatePI.php";

    private Map<String, String> params;

    public UpdatePIRequest(String username, String encodedImage, Response.Listener<String> listener){
        super(Method.POST, UPDATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("a_username", username);
        params.put("encodedImage", encodedImage);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
