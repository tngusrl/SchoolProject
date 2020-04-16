package com.example.managament;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MessageAdapter1 extends RecyclerView.Adapter<MessageAdapter1.MessageViewHolder> {

    private ArrayList<ArrayList<Messages>> chatList;
    ArrayList<String> chat_user_name;
    private String loginID;
    private Context context;

    public MessageAdapter1(ArrayList<ArrayList<Messages>> chatList, ArrayList<String> chat_user_name, String loginID, Context context) {
        this.chatList = chatList;
        this.chat_user_name = chat_user_name;
        this.loginID = loginID;
        this.context = context;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent, false);

        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView state;
        public TextView time;

        public MessageViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Toast.makeText(v.getContext(), pos + "", Toast.LENGTH_LONG).show();

                        String a = chat_user_name.get(pos);

                        Intent i = new Intent(v.getContext(), ChatActivity.class);

                        i.putExtra("User",a);
                        i.putExtra("loginUser",loginID);

                        context.startActivity(i);
                    }
                }
            });

            state = view.findViewById(R.id.Textview1);
            time = view.findViewById(R.id.Textview2);
            messageText = view.findViewById(R.id.Textview3);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter1.MessageViewHolder messageViewHolder, int i) {

        ArrayList<Messages> messagesList1 = chatList.get(i);

        Messages c = messagesList1.get(messagesList1.size() - 1);

        messageViewHolder.state.setText(c.getType());
        messageViewHolder.time.setText(c.getTime());
        messageViewHolder.messageText.setText(c.getMessage());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
