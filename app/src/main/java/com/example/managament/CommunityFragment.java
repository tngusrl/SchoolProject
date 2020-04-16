package com.example.managament;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class CommunityFragment extends Fragment {

    Button logoutButton; // 로그아웃
    Button mypostButton; // 내글 보기

    Button chatConfirm; // 나의 채팅
    Button Rules; // 이용 규칙

    ImageButton btn1; // 학교 홈
    ImageButton btn2; // 공지사항
    ImageButton btn3; // 전화번호
    ImageButton btn4; // 셔틀버스
    ImageButton btn5; // 도서관
    ImageButton btn6; // 열람실

    // 로그인 SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // 자동로그인 SharedPreferences
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community, null);

        pref = this.getActivity().getSharedPreferences("login", 0);
        editor = pref.edit();

        pref1 = this.getActivity().getSharedPreferences("autologin", 0);
        editor1 = pref1.edit();

        logoutButton = view.findViewById(R.id.logoutButton);
        mypostButton = view.findViewById(R.id.mypostButton);
        chatConfirm = view.findViewById(R.id.chatConfirm);
        Rules = view.findViewById(R.id.Rules);

        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        btn4 = view.findViewById(R.id.btn4);
        btn5 = view.findViewById(R.id.btn5);
        btn6 = view.findViewById(R.id.btn6);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),webView.class);
                i.putExtra("url","http://m.hanbat.ac.kr/html/kr/");
                startActivity(i);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),webView.class);
                i.putExtra("url","http://m.hanbat.ac.kr/html/kr/board/board.html");
                startActivity(i);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),webView.class);
                i.putExtra("url","http://m.hanbat.ac.kr/html/kr/campus/campus_03.html");
                startActivity(i);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),webView.class);
                i.putExtra("url","http://m.hanbat.ac.kr/html/kr/campus/campus.html");
                startActivity(i);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),webView.class);
                i.putExtra("url","http://mlib.hanbat.ac.kr/");
                startActivity(i);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),webView.class);
                i.putExtra("url","https://smart.hanbat.ac.kr/mobile/MA/roomListWeb.php");
                startActivity(i);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.clear();
                editor.commit();

                editor1.clear();
                editor1.commit();

                Intent i = new Intent(getActivity(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

        mypostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MyPost.class);
                startActivity(i);
            }
        });

        chatConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ChatConfirmList.class);
                startActivity(i);
            }
        });


        Rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Rules.class);
                startActivity(i);
            }
        });




        return view;
    }
}

