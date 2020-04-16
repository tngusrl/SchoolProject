package com.example.managament;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class SellBoardListRequest extends StringRequest {
    final static private String URL = "https://gurwls1307.cafe24.com/SellBoardListRegister.php";
    private Map<String, String> parameters;

    public SellBoardListRequest(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }


}
