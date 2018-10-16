package com.please.khs.shower;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;

public class LoginRequest extends StringRequest {
    final static private String URL = "http://45.32.49.247:8000/service/login/";
    private Map<String, String> parameters;

    public LoginRequest(String email, String Passwords, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("Email", email);
        parameters.put("Password", Passwords);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
