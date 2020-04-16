package com.example.managament;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;

public class Preview extends AppCompatActivity {

    pageAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Intent intent = getIntent();

        String indexImage = intent.getExtras().getString("indexImage");

        viewPager = findViewById(R.id.view);
        adapter = new pageAdapter(this, indexImage);

        viewPager.setAdapter(adapter);
    }
}
