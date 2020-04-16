package com.example.managament;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatConfirmList extends AppCompatActivity {

    RecyclerView mMessagesList;
    LinearLayoutManager mLinearLayout;

    MessageAdapter1 mAdapter;

    ArrayList<ArrayList<Messages>> chatList = new ArrayList<>();
    ArrayList<String> chat_user_name = new ArrayList<>();

    DatabaseReference mRootRef;

    String mCurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_confirm_list);

        mMessagesList = findViewById(R.id.listview);
        mLinearLayout = new LinearLayoutManager(this);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        SharedPreferences pref = this.getSharedPreferences("login", 0);
        mCurrentUserId = pref.getString("ID", "null");

        mAdapter = new MessageAdapter1(chatList,chat_user_name, mCurrentUserId,this);

        mMessagesList.setAdapter(mAdapter);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        loadMessages();



    }

    private void loadMessages() {

        mRootRef.child("messages").child(mCurrentUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String a = dataSnapshot.getKey();
                ArrayList<Messages> messagesList1 = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Messages messages = postSnapshot.getValue(Messages.class);
                    messagesList1.add(messages);
                }

                chatList.add(messagesList1);
                chat_user_name.add(a);

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
