package com.please.khs.shower.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserNameRequest extends StringRequest {
    final static private String URL = "http://45.32.49.247:8000/service/edit_nickname/";
    private Map<String, String> parameters;

    public UserNameRequest(String Email, String Nickname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("Email", Email);
        parameters.put("Nickname", Nickname);

    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
