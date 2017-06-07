package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 21.05.2017.
 */

public class SearchResultRequest extends StringRequest {
    private static final String REQUEST_URL = "https://substitutive-sailor.000webhostapp.com/SearchResult.php";
    private Map<String, String> params;

    public SearchResultRequest(String query, String date, Response.Listener<String> listener){
        super(Method.POST, REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("query", query);
        params.put("date", date);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
