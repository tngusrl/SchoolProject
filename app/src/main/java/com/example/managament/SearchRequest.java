package com.example.managament;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends StringRequest {

    final static private String URL = "https://gurwls1307.cafe24.com/imsi.php";

    private Map<String,String> parameters;

    public SearchRequest(String filed, String searchName, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("filed",filed);
        parameters.put("searchName",searchName);
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
