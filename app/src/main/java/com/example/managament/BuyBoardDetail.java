package com.example.managament;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class BuyBoardDetail extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11;

    Button b1,b3;

    String date;
    String loginID; // 로그인 유저

    String userID; // 글작성자

    int a;
    String b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_board_detail);

        SharedPreferences pref = getSharedPreferences("login", 0);

        loginID = pref.getString("ID", "null");

        tv1 = findViewById(R.id.bookname);
        tv2 = findViewById(R.id.author);
        tv3 = findViewById(R.id.publisher);
        tv4 = findViewById(R.id.bookprice);
        tv5 = findViewById(R.id.wantbookprice);
        tv6 = findViewById(R.id.userid);
        tv7 = findViewById(R.id.uptime);
        tv8 = findViewById(R.id.memo);
        tv9 = findViewById(R.id.state1);
        tv10 = findViewById(R.id.state2);
        tv11 = findViewById(R.id.state3);

        b1 = findViewById(R.id.chatBt); // 채팅
        b3 = findViewById(R.id.buyfinish); // 구매완료

        Intent i = getIntent();

        BuyBookDB buyBookDB = (BuyBookDB) i.getSerializableExtra("class");

        date = buyBookDB.getUptime();
        date = date.substring(0,date.indexOf(" "));

        a = buyBookDB.getIdx();
        b = Integer.toString(a);

        tv1.setText(buyBookDB.getBookname());
        tv2.setText(buyBookDB.getAuthor());
        tv3.setText(buyBookDB.getPublisher());
        tv4.setText(buyBookDB.getBookprice());
        tv4.setPaintFlags(tv4.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        tv5.setText(buyBookDB.getWantbookprice());
        tv6.setText(buyBookDB.getUserID());
        tv7.setText(date);
        tv8.setText(buyBookDB.getMemo());

        userID = buyBookDB.getUserID();

        if (buyBookDB.getBookstate().equals("상")) {

            tv9.setTextColor(Color.BLACK);

        } else if (buyBookDB.getBookstate().equals("중")) {

            tv10.setTextColor(Color.BLACK);

        } else if (buyBookDB.getBookstate().equals("하")) {

            tv11.setTextColor(Color.BLACK);

        }


        if(loginID.equals(buyBookDB.getUserID())){
            b1.setVisibility(View.GONE);

            if(buyBookDB.getTrading().equals("true")){
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

                                    Intent i = new Intent(getApplicationContext(),MyPost.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }

                            } catch (Exception e) {

                            }
                        }
                    };

                    TradingBuyRequest tradingBuyRequest = new TradingBuyRequest(b, responseLister);
                    RequestQueue queue = Volley.newRequestQueue(BuyBoardDetail.this);
                    queue.add(tradingBuyRequest);

                }
            });
        }

        else {
            b3.setVisibility(View.GONE);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(BuyBoardDetail.this,ChatActivity.class);

                i.putExtra("User",userID);
                i.putExtra("loginUser",loginID);
                startActivity(i);

            }
        });


    }
}
