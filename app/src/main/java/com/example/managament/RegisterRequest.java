package com.example.managament;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL = "https://gurwls1307.cafe24.com/HSUserRegister.php";
    private Map<String,String> parameters;

    public RegisterRequest(String userID, String userPassword, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userPassword",userPassword);
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
