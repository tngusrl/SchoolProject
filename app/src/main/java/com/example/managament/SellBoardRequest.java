package com.example.managament;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SellBoardRequest extends StringRequest {
    final static private String URL = "https://gurwls1307.cafe24.com/SellBoardRegister.php";
    private Map<String, String> parameters;

    public SellBoardRequest(String userID, String covername, String filename1, String filename2, String bookname, String author, String publisher, String bookprice, String wantbookprice, String bookstate, String memo, String rating, String indexImage, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("covername", covername);
        parameters.put("filename1", filename1);
        parameters.put("filename2", filename2);
        parameters.put("bookname", bookname);
        parameters.put("author", author);
        parameters.put("publisher", publisher);
        parameters.put("bookprice", bookprice);
        parameters.put("wantbookprice", wantbookprice);
        parameters.put("bookstate", bookstate);
        parameters.put("memo", memo);
        parameters.put("rating", rating);
        parameters.put("indexImage", indexImage);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
