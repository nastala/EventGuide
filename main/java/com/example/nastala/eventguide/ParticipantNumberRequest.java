package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 13.03.2017.
 */

public class ParticipantNumberRequest extends StringRequest {
    private static final String REQUEST_URL = "https://substitutive-sailor.000webhostapp.com/ParticipantNumber.php";
    private Map<String, String> params;

    public ParticipantNumberRequest(int event_id, Response.Listener<String> listener){
        super(Method.POST, REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("event_id", event_id + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
