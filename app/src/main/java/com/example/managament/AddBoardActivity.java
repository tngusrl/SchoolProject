package com.example.managament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class AddBoardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    Button searchButton;
    EditText search_name;

    String[] item;

    String filed, searchName;

    private ArrayList<BookInformation> list = new ArrayList<>();

    private RecyclerView recyclerView;
    AddSellSearchRecyclerViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);

        spinner = findViewById(R.id.spinner);
        searchButton = findViewById(R.id.search_button);
        search_name = findViewById(R.id.search_name);

        recyclerView = findViewById(R.id.listview5);

        Intent intent = getIntent();

        final String division = intent.getExtras().getString("division");

        try {

            // 스피너 적용
            spinner.setOnItemSelectedListener(this);

            item = new String[]{"도서명", "저자명", "발행처", "발행연도"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    recyclerView.removeAllViews();

                    searchName = search_name.getText().toString(); // 검색어

                    Response.Listener<String> responseLister = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {

                                final JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject a = jsonObject.getJSONObject("Book");

                                    BookInformation book = new BookInformation();

                                    book.setFilename1("https://gurwls1307.cafe24.com/" + a.getString("fileName"));
                                    book.setBookname1(a.getString("bookName"));
                                    book.setAuthor1(a.getString("author"));
                                    book.setPublisher1(a.getString("publisher"));
                                    book.setBookprice1(a.getString("bookPrice"));
                                    book.setPublication(a.getString("publication"));
                                    book.setRating(a.getString("rating"));
                                    book.setIndexImage(a.getString("indexImage"));

                                    list.add(book);
                                }
                                setRvadapter(list, division);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    SearchRequest searchRequest = new SearchRequest(filed, searchName, responseLister); // 스피너 filed, 검색어 searchName
                    RequestQueue queue = Volley.newRequestQueue(AddBoardActivity.this);
                    queue.add(searchRequest);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {

        filed = item[i];
        if (filed.equals("도서명")) {
            filed = "BookName";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setRvadapter(ArrayList<BookInformation> list, String division) {

        recyclerView.setHasFixedSize(true);

        myAdapter = new AddSellSearchRecyclerViewAdapter(this, list, division);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);
    }
}


