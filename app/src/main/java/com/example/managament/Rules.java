package com.example.managament;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Rules extends AppCompatActivity {

    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    TextView text7;
    TextView text8;
    TextView text9;
    TextView text10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text8 = findViewById(R.id.text8);
        text9 = findViewById(R.id.text9);
        text10 = findViewById(R.id.text10);

        text5.setText("3. 개발자 책임");
        text6.setText(Html.fromHtml(" - 서비스를 이용함에 있어 발생하는 사건, 사고에 관하여 개발자는 책임을 가지지 않습니다." + "<br />" + "절대로 늦은 밤이나 인적이 드문 곳에서의 직거래를 피하시고, 대낮 학교 내부에서 거래하시는 것을 원칙으로 합니다."));

        text3.setText("2. 메세지");
        text4.setText(Html.fromHtml(" - 메세지는 사용자 개인 간의 사적인 대화이므로 상대방의 기분을 해치지 않도록 주의합니다." + "<br />" + " - 광고, 스팸, 상대방에게 피해를 끼치는 쪽지를 반복적으로 보내는것이 확인될 경우, 서비스 사용에 제재가 부여됩니다. "));

        text1.setText("1. 게시물 관리");
        text2.setText(Html.fromHtml(" - 거래가 완료된 게시물에 대하여 사용자간의 오해가 없도록 빠르게 거래완료를 설정하여 주시기 바랍니다." + "<br />" + " - 대학 수업의 특성 상 1년이 지난 게시글에 대해서는 개발자가 임의로 수정, 삭제할 수 있습니다." + "<br />" + " - 또한 게시물을 등록하고 6개월간 단 한번도 접속하지 않은 경우에도 개발자가 게시물을 임의로 수정, 삭제할 수 있습니다."));

        text7.setText("4. 도서 상태");
        text8.setText(Html.fromHtml(" - 게시글 등록 시 도서 상태에 대해서는 반드시 사실만을 입력하여야 합니다." + "<br />" + " - 허위 정보 (도서 상태, 첨부 이미지) 입력이 적발되었을 시 개발자가 게시글을 삭제, 사용자에게 제재를 가할 수 있습니다."));

        text9.setText("5. 개인 정보");
        text10.setText(Html.fromHtml(" - 개발자는 절대로 사용자의 정보를 메세지로 물어보지 않습니다." + "<br />" + " - 보안을 위해 평소 사용하시는 암호 이외에 다른 암호를 사용해주세요."));

    }
}
