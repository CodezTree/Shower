package com.please.khs.shower.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "http://45.32.49.247:8000/service/register/";
    private Map<String, String> parameters;

    public RegisterRequest(String Email, String Passwords, String Passwords_chk, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("Email", Email);
        parameters.put("Password", Passwords);
        parameters.put("Password_chk", Passwords_chk);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
