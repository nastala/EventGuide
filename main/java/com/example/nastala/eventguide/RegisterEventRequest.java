package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 27.12.2016.
 */

public class RegisterEventRequest extends StringRequest {
    private static final String REQUEST_URL = "https://substitutive-sailor.000webhostapp.com/RegisterEvent.php";
    private Map<String, String> params;

    public  RegisterEventRequest(int event_id, String username, Response.Listener<String> listener){
        super(Method.POST, REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("event_id", event_id + "");
        params.put("a_username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
