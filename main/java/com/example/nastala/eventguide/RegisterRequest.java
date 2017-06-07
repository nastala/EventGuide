package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 15.12.2016.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_URL = "https://substitutive-sailor.000webhostapp.com/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String username, String password, int age, String city, String encodedImage, Response.Listener<String> listener){
        super(Method.POST, REGISTER_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("a_username", username);
        params.put("password", password);
        params.put("age", age + "");
        params.put("city", city);
        params.put("encodedImage", encodedImage);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
