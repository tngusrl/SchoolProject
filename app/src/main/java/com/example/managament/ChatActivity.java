package com.example.managament;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity {

    Button btn_send;

    EditText et_msg;

    String userID; // 글등록자
    String loginID; // 로그인 유저

    DatabaseReference mRootRef;
    String mCurrentUserId;
    String mChatUser;

    RecyclerView mMessagesList;

    final List<Messages> messagesList = new ArrayList<>();
    LinearLayoutManager mLinearLayout;
    MessageAdapter mAdapter;

    SimpleDateFormat format1 = new SimpleDateFormat( "MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        userID = i.getExtras().getString("User");
        loginID = i.getExtras().getString("loginUser");

        mMessagesList = findViewById(R.id.message_list);
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mAdapter = new MessageAdapter(messagesList, loginID);

        mMessagesList.setAdapter(mAdapter);

        btn_send = findViewById(R.id.btn_send);
        et_msg = findViewById(R.id.et_msg);



        mRootRef = FirebaseDatabase.getInstance().getReference();

        // 발신자
        mCurrentUserId = i.getExtras().getString("loginUser");

        // 수신자
        mChatUser = i.getExtras().getString("User");

        loadMessages();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();

                et_msg.setText("");
            }
        });

    }

    private void loadMessages() {

        mRootRef.child("messages").child(mCurrentUserId).child(mChatUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Messages messages = dataSnapshot.getValue(Messages.class);

                messagesList.add(messages);
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

    private void sendMessage(){

        Date time = new Date();
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        format1.setTimeZone(tz);
        String curTime = format1.format(time);

        String message = et_msg.getText().toString();

        String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
        String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

        DatabaseReference user_message_push = mRootRef.child("chat")
                .child(mCurrentUserId).child(mChatUser).push();

        String push_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put("message",message);
        messageMap.put("time", curTime);
        messageMap.put("type","보낸 쪽지");
        messageMap.put("from",mCurrentUserId);

        Map messageMap1 = new HashMap();
        messageMap1.put("message",message);
        messageMap1.put("time", curTime);
        messageMap1.put("type","받은 쪽지");
        messageMap1.put("from",mCurrentUserId);

        Map messageUserMap = new HashMap();
        messageUserMap.put(current_user_ref + "/" + push_id,messageMap);
        messageUserMap.put(chat_user_ref + "/" + push_id,messageMap1);

        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

            }
        });
    }


}
