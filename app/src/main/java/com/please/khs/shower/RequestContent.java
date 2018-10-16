package com.please.khs.shower;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestContent extends StringRequest {

    final static private String URL = "http://45.32.49.247:8000/contents/";
    private Map<String, String> parameters;

    public RequestContent(int emotion, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("User_f", Integer.toString(emotion));
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
