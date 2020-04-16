package com.example.managament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class NoticeActivity extends AppCompatActivity {

    TextView tv;
    CheckBox cb;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        tv = (TextView) findViewById(R.id.textView6);
        cb = (CheckBox) findViewById(R.id.checkBox);
        btn = (Button) findViewById(R.id.button);

        btn.setEnabled(false);

        String content = "지금 화면은 공지사항 화면입니다. \n 한밭대학교 서적 거래 서비스를 이용하면서 발생하는 모든 문제에 대해서는 책임지지 않습니다. \n 책 거래시 거짓된 정보를 입력할 경우, 사전공지없이 서비스 사용이 중지될 수 있습니다.";
        tv.setText(content);

        cb.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    btn.setEnabled(true);
                }
                else{
                    btn.setEnabled(false);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(NoticeActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
