package com.example.managament;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {

    String price;
    String memo;
    String state;

    EditText wantPrice;
    EditText et1;

    RadioGroup radioGroup;

    String loginID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SharedPreferences pref = this.getSharedPreferences("login", 0);

        loginID = pref.getString("ID", "null");

        wantPrice = findViewById(R.id.wantPrice); // 원하는 구매가
        et1 = findViewById(R.id.memo); // 메모

        radioGroup = findViewById(R.id.radiogroup);

        Button boardRegisterButton = findViewById(R.id.boardRegisterButton);

        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        final String 도서명 = getIntent().getExtras().getString("도서명");
        final String 저자명 = getIntent().getExtras().getString("저자명");
        final String 출판사 = getIntent().getExtras().getString("출판사");
        final String 정가 = getIntent().getExtras().getString("정가");
        final String 도서표지 = getIntent().getExtras().getString("도서표지");

        boardRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price = wantPrice.getText().toString();
                memo = et1.getText().toString();

                Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                Intent intent = new Intent(MainActivity2.this, BoardListActivity.class);
                                startActivity(intent);

                            }

                        } catch (Exception e) {

                        }
                    }

                };

                BuyBoardRequest buyBoardRequest = new BuyBoardRequest(loginID, 도서표지, 도서명, 저자명, 출판사, 정가, price, memo, state, responseListener1);
                RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
                queue.add(buyBoardRequest);


            }
        });
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if(checkedId == R.id.radio1){

                state = "상";

            }


            else if(checkedId == R.id.radio2){

                state = "중";

            }


            else if(checkedId == R.id.radio3){

                state = "하";

            }

        }
    };
}
