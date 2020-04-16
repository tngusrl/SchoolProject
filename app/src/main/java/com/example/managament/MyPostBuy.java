package com.example.managament;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyPostBuy extends Fragment {

    private List<BuyBookDB> list = new ArrayList<>();
    private List<BuyBookDB> list1 = new ArrayList<>();
    private RecyclerView recyclerView;
    boolean isLoading = false;
    RecyclerViewAdapter myAdapter;
    String loginID; // 현재 로그인되어있는 아이디
    int count;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_post_buy, container, false);
        recyclerView = view.findViewById(R.id.listview3);

        recyclerView.removeAllViewsInLayout();
        SharedPreferences pref = this.getActivity().getSharedPreferences("login", 0);

        loginID = pref.getString("ID", "null");

        list.clear();
        list1.clear();

        jsoncall();

        initScrollListener();

        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && gestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildAdapterPosition(child);
                    BuyBookDB SBD = list1.get(position);

                    Intent i = new Intent(getActivity(), BuyBoardDetail.class);
                    i.putExtra("class", SBD);
                    startActivity(i);

                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        return view;
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int x = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int y = list1.size();
                int z = list.size();
                if (!isLoading) {
                    if (linearLayoutManager != null && x == y - 1 && x != z - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        list1.add(null);
        myAdapter.notifyItemInserted(list.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list1.remove(list1.size() - 1);
                int scrollPosition = list1.size();
                myAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                if (list.size() < nextLimit)
                    nextLimit = list.size();

                while (currentSize < nextLimit) {
                    list1.add(list.get(currentSize));
                    currentSize++;
                }

                myAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    public void jsoncall() {

        Response.Listener<String> responseLister = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);
                    count = array.length();

                } catch (Exception e) {

                }
                JSONObject jsonObject;

                for (int i = 0; i < count; i++) {

                    try {

                        JSONArray array = new JSONArray(response);
                        jsonObject = array.getJSONObject(i);
                        jsonObject = jsonObject.getJSONObject("Book");

                        BuyBookDB book = new BuyBookDB();

                        book.setIdx(jsonObject.getInt("idx"));
                        book.setUserID(jsonObject.getString("userId"));
                        book.setFileName(jsonObject.getString("fileName"));
                        book.setBookname(jsonObject.getString("bookName"));
                        book.setAuthor(jsonObject.getString("author"));
                        book.setPublisher(jsonObject.getString("publisher"));
                        book.setBookprice(jsonObject.getString("bookPrice"));
                        book.setWantbookprice(jsonObject.getString("wantbookprice"));
                        book.setBookstate(jsonObject.getString("bookstate"));
                        book.setMemo(jsonObject.getString("memo"));
                        book.setTrading(jsonObject.getString("trading"));
                        book.setUptime(jsonObject.getString("uptime"));
                        list.add(book);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                }

                if (count < 10) {
                    for (int i = 0; i < count; i++) {
                        list1.add(list.get(i));
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        list1.add(list.get(i));
                    }
                }

                setRvadapter(list1);

            }
        };

        MyPostBuyRequest myPostBuyRequest = new MyPostBuyRequest(loginID, responseLister);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(myPostBuyRequest);
    }

    public void setRvadapter(List<BuyBookDB> list) {

        recyclerView.setHasFixedSize(true);

        myAdapter = new RecyclerViewAdapter(getActivity(), list, "buy");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(myAdapter);
    }

}
