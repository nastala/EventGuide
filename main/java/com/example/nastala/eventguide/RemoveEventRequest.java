package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 25.01.2017.
 */

public class RemoveEventRequest extends StringRequest {
    private static final String REQUEST_URL = "https://substitutive-sailor.000webhostapp.com/RemoveEvent.php";
    private Map<String, String> params;

    public RemoveEventRequest(int event_id, String username, Response.Listener<String> listener){
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
