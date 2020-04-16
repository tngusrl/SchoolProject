package com.example.managament;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateViews1 extends StringRequest {

    final static private String URL = "https://gurwls1307.cafe24.com/ViewUpdateRequest1.php";
    private Map<String, String> parameters;

    public UpdateViews1(String idx, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("idx", idx);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
