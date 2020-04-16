package com.example.managament;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MyPost extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private MyPostBuy menu1Fragment = new MyPostBuy();
    private MyPostSell menu2Fragment = new MyPostSell();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, menu1Fragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){

                    case R.id.navigation_sell:{
                        transaction.replace(R.id.main_frame,menu1Fragment).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.navigation_buy:{
                        transaction.replace(R.id.main_frame,menu2Fragment).commitAllowingStateLoss();
                        break;
                    }
                }


                return true;
            }
        });



    }
}
