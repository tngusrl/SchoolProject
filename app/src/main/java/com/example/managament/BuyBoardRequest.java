package com.example.managament;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BuyBoardRequest extends StringRequest {

    final static private String URL = "https://gurwls1307.cafe24.com/BuyBoardRegister.php";
    private Map<String, String> parameters;

    public BuyBoardRequest(String userID, String fileName, String bookname, String author, String publisher, String bookprice, String wantbookprice, String memo, String bookstate, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID); // 구매자 등록아이디
        parameters.put("fileName", fileName); // 도서표지
        parameters.put("bookname", bookname); // 도서명
        parameters.put("author", author); // 저자명
        parameters.put("publisher", publisher); // 출판사
        parameters.put("bookprice", bookprice); // 도서정가
        parameters.put("wantbookprice", wantbookprice); // 구매희망가
        parameters.put("memo", memo); // 메모
        parameters.put("bookstate", bookstate); // 도서상태
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
