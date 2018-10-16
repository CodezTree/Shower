package com.please.khs.shower;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestEmotionProcess extends StringRequest {

    final static private String URL = "http://45.32.49.247:8000/service/analyze/";
    private Map<String, String> parameters;

    public RequestEmotionProcess(String msg, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("Data", msg);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
