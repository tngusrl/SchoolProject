package com.example.managament;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class SellBoardDetail extends AppCompatActivity {

    TextView t1, t2, t3, t4, t5, t6, t7, t8;
    ImageView ivvv1, ivvv2;

    TextView a1, a2, a3, a4, a5, a6, a7, a8, a9;

    Button b1, b3;

    Button preview;

    String date;
    String loginID;

    String userID; // 글작성자

    int a;
    String b;

    String state;

    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_board_detail);

        SharedPreferences pref = getSharedPreferences("login", 0);

        loginID = pref.getString("ID", "null");

        t1 = findViewById(R.id.bookname); // 도서명
        t2 = findViewById(R.id.author); // 저자명
        t3 = findViewById(R.id.publisher); // 출판사
        t4 = findViewById(R.id.bookprice); // 가격
        t5 = findViewById(R.id.uptime); // 등록일
        t6 = findViewById(R.id.userid); // 판매자 아이디
        t7 = findViewById(R.id.wantbookprice); // 원하는 판매가
        t8 = findViewById(R.id.memo); // 메모

        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
        a5 = findViewById(R.id.a5);
        a6 = findViewById(R.id.a6);
        a7 = findViewById(R.id.a7);
        a8 = findViewById(R.id.a8);
        a9 = findViewById(R.id.a9);

        b1 = findViewById(R.id.chatBt); // 채팅
        b3 = findViewById(R.id.sellfinish); // 판매완료

        preview = findViewById(R.id.Preview); // 미리보기

        ivvv1 = findViewById(R.id.image1111);
        ivvv2 = findViewById(R.id.image2222);

        ratingBar = findViewById(R.id.ratingBar);



        Intent i = getIntent();

        final SellBookDB sellBookDB = (SellBookDB) i.getSerializableExtra("class");

        ratingBar.setRating(Float.parseFloat(sellBookDB.getRating()));

        date = sellBookDB.getUptime();
        date = date.substring(0, date.indexOf(" "));

        a = sellBookDB.getIdx();
        b = Integer.toString(a);

        state = sellBookDB.getBookstate();

        userID = sellBookDB.getUserID();

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Preview.class);
                intent.putExtra("indexImage",sellBookDB.getIndexImage());
                startActivity(intent);
            }
        });

        if (state.contains("1")) {
            a1.setTextColor(Color.BLACK);
        }


        if (state.contains("2")) {
            a2.setTextColor(Color.BLACK);
        }


        if (state.contains("3")) {
            a3.setTextColor(Color.BLACK);
        }


        if (state.contains("4")) {
            a4.setTextColor(Color.BLACK);
        }


        else {
            a5.setTextColor(Color.BLACK);
        }


        if (state.contains("6")) {
            a6.setTextColor(Color.BLACK);
        }


       else {
            a7.setTextColor(Color.BLACK);
        }


        if (state.contains("8")) {
            a8.setTextColor(Color.BLACK);
        }


        else {
            a9.setTextColor(Color.BLACK);
        }

        t1.setText(sellBookDB.getBookname());
        t2.setText(sellBookDB.getAuthor());
        t3.setText(sellBookDB.getPublisher());
        t4.setText(sellBookDB.getBookprice());
        t4.setPaintFlags(t4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        t5.setText(date);
        t6.setText(sellBookDB.getUserID());
        t7.setText(sellBookDB.getWantbookprice());
        t8.setText(sellBookDB.getMemo());


        Glide.with(getApplicationContext()).load("https://gurwls1307.cafe24.com/mobile/uploads/" + sellBookDB.getFileName1()).into(ivvv1);
        Glide.with(getApplicationContext()).load("https://gurwls1307.cafe24.com/mobile/uploads/" + sellBookDB.getFileName2()).into(ivvv2);

        if (loginID.equals(sellBookDB.getUserID())) {
            b1.setVisibility(View.GONE);

            if (sellBookDB.getTrading().equals("true")) {
                b3.setVisibility(View.GONE);
            }


            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Response.Listener<String> responseLister = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if (success) {

                                    Intent i = new Intent(getApplicationContext(), MyPost.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }

                            } catch (Exception e) {

                            }
                        }
                    };

                    TradingSellRequest tradingSellRequest = new TradingSellRequest(b, responseLister);
                    RequestQueue queue = Volley.newRequestQueue(SellBoardDetail.this);
                    queue.add(tradingSellRequest);

                }
            });

        } else {
            b3.setVisibility(View.GONE);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SellBoardDetail.this,ChatActivity.class);

                i.putExtra("User",userID);
                i.putExtra("loginUser",loginID);
                startActivity(i);

            }
        });

    }
}
