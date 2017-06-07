package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 22.12.2016.
 */

public class UpdateNameRequest extends StringRequest {

    private static final String REQUEST_URL = "https://substitutive-sailor.000webhostapp.com/UpdateName.php";
    private Map<String, String > params;

    public UpdateNameRequest(String name, String username, Response.Listener<String> listener){
        super(Method.POST, REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("name", name);
        params.put("a_username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
