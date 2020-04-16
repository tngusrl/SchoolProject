package com.example.managament;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ExamRequest extends StringRequest {

    final static private String URL = "https://gurwls1307.cafe24.com/ExamRequest.php";

    private Map<String,String> parameters;

    public ExamRequest(String cer, String ep, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("cer",cer);
        parameters.put("ep",ep);
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
