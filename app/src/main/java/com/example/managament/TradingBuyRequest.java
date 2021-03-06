package com.example.managament;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TradingBuyRequest extends StringRequest {

    final static private String URL = "https://gurwls1307.cafe24.com/TradingBuyRequest.php";
    private Map<String,String> parameters;

    public TradingBuyRequest(String idx, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("idx",idx);
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
