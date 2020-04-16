package com.example.managament;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuyFragment extends Fragment implements TextWatcher {

    private String URL = "https://gurwls1307.cafe24.com/BuyBoardListRegister.php";
    private JsonArrayRequest jsonArrayRequest;
    private RequestQueue requestQueue;
    private List<BuyBookDB> list = new ArrayList<>();
    private List<BuyBookDB> list1 = new ArrayList<>();
    private RecyclerView recyclerView;
    boolean isLoading = false;
    RecyclerViewAdapter myAdapter;

    EditText editText;
    Button btn;

    String idx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buy, null);

        recyclerView = view.findViewById(R.id.listview1);

        btn = view.findViewById(R.id.button2);

        editText = view.findViewById(R.id.edittext);
        editText.addTextChangedListener(this);

        jsoncall();

        initScrollListener();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddBoardActivity.class);
                intent.putExtra("division","buy");
                startActivity(intent);
            }
        });

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

                    idx =  Integer.toString(SBD.getIdx());

                    Response.Listener<String> responseLister = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                            } catch (Exception e) {

                            }
                        }
                    };

                    UpdateViews tradingBuyRequest = new UpdateViews(idx, responseLister);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(tradingBuyRequest);

                    Intent i = new Intent(getActivity(),BuyBoardDetail.class);
                    i.putExtra("class",SBD);
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
                int y=list1.size();
                int z=list.size();
                if (!isLoading) {
                    if (linearLayoutManager != null &&  x == y - 1 && x != z-1) {
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

                if(list.size() < nextLimit)
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

    public void jsoncall(){

        jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        jsonObject = jsonObject.getJSONObject("Book");

                        BuyBookDB book = new BuyBookDB();

                        book.setIdx(jsonObject.getInt("idx"));
                        book.setUserID(jsonObject.getString("userId"));
                        book.setFileName(jsonObject.getString( "fileName"));
                        book.setBookname(jsonObject.getString("bookName"));
                        book.setAuthor(jsonObject.getString("author"));
                        book.setPublisher(jsonObject.getString("publisher"));
                        book.setBookprice(jsonObject.getString("bookPrice"));
                        book.setWantbookprice(jsonObject.getString("wantbookprice"));
                        book.setMemo(jsonObject.getString("memo"));
                        book.setBookstate(jsonObject.getString("bookstate"));
                        book.setTrading(jsonObject.getString("trading"));
                        book.setUptime(jsonObject.getString("uptime"));
                        book.setViews(jsonObject.getInt("views"));

                        list.add(book);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                }

                if(response.length()<10){
                    for(int i=0;i<response.length();i++){
                        list1.add(list.get(i));
                    }
                }

                else {
                    for (int i = 0; i < 10; i++) {
                        list1.add(list.get(i));
                    }
                }

                setRvadapter(list1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void setRvadapter(List<BuyBookDB> list){

        recyclerView.setHasFixedSize(true);

        myAdapter = new RecyclerViewAdapter(getActivity(),list,"buy");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        myAdapter.getFilter().filter(s);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

