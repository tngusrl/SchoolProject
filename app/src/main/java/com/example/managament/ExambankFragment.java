package com.example.managament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExambankFragment extends Fragment {

    Button ans1, ans2, ans3, ans4; // 보기 1,2,3,4
    TextView ans1_text, ans2_text, ans3_text, ans4_text;

    TextView 해설;

    ImageView exam_image; // 문제 이미지

    TextView exam; // 문제

    ExamRequest examRequest;

    Button 다음;

    int count = 0;
    private ArrayList<ExamInfo> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exambank, null);

        ans1 = view.findViewById(R.id.ans1_btn);
        ans2 = view.findViewById(R.id.ans2_btn);
        ans3 = view.findViewById(R.id.ans3_btn);
        ans4 = view.findViewById(R.id.ans4_btn);

        ans1_text = view.findViewById(R.id.ans1);
        ans2_text = view.findViewById(R.id.ans2);
        ans3_text = view.findViewById(R.id.ans3);
        ans4_text = view.findViewById(R.id.ans4);

        해설 = view.findViewById(R.id.해설);

        exam_image = view.findViewById(R.id.question_image);
        exam_image.setVisibility(View.GONE);

        exam = view.findViewById(R.id.qquestion);

        다음 = view.findViewById(R.id.다음);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject a = jsonObject.getJSONObject("Book");

                        ExamInfo book = new ExamInfo();

                        book.setCertificate(a.getString("certificate"));
                        book.setExam(a.getString("exam"));
                        book.setExam_image(a.getString("exam_image"));
                        book.setOne(a.getString("one"));
                        book.setTwo(a.getString("two"));
                        book.setThree(a.getString("three"));
                        book.setFour(a.getString("four"));
                        book.setAnswer(a.getString("answer"));
                        list.add(book);

                    }

                    exam.setText(list.get(0).exam);
                    ans1_text.setText(list.get(0).one);
                    ans2_text.setText(list.get(0).two);
                    ans3_text.setText(list.get(0).three);
                    ans4_text.setText(list.get(0).four);

                    해설.setText(list.get(0).answer);


                } catch (Exception e) {

                }
            }

        };

        examRequest = new ExamRequest("정보처리기사", "1", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(examRequest);


        다음.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;

                exam.setText(list.get(count).exam);
                ans1_text.setText(list.get(count).one);
                ans2_text.setText(list.get(count).two);
                ans3_text.setText(list.get(count).three);
                ans4_text.setText(list.get(count).four);

                if (list.get(count).answer != null)
                    해설.setText(list.get(count).answer);

                if (list.get(count).exam_image != null) {
                    exam_image.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load("https://gurwls1307.cafe24.com/" + list.get(count).exam_image).into(exam_image);
                }
            }
        });



        return view;
    }
}
