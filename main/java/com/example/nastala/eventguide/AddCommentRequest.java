package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 23.05.2017.
 */

public class AddCommentRequest extends StringRequest {
    private static final String REQUEST_URL = "https://substitutive-sailor.000webhostapp.com/AddComment.php";
    private Map<String, String> params;

    public AddCommentRequest(String event_id, String username, String comment, int recommend, String date, Response.Listener<String> listener){
        super(Method.POST, REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("event_id", event_id + "");
        params.put("username", username);
        params.put("comment", comment);
        params.put("recommend", recommend + "");
        params.put("date", date);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
