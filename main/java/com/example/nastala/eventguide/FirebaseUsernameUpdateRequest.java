package com.example.nastala.eventguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nastala on 22.03.2017.
 */

public class FirebaseUsernameUpdateRequest extends StringRequest {
    private static final String REQUEST_URL = "https://substitutive-sailor.000webhostapp.com/FirebaseUsernameUpdate.php";
    private Map<String, String> params;

    public FirebaseUsernameUpdateRequest(String username, String token, Response.Listener<String> listener){
        super(Method.POST, REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("a_username", username);
        params.put("token", token);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
