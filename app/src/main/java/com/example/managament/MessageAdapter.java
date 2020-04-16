package com.example.managament;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessagesList;
    private String loginID;

    public MessageAdapter(List<Messages> mMessageList, String loginID){
        this.mMessagesList = mMessageList;
        this.loginID = loginID;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent,false);

        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView messageText;
        public TextView state;
        public TextView time;

        public MessageViewHolder(View view){
            super(view);

            state = view.findViewById(R.id.Textview1);
            time = view.findViewById(R.id.Textview2);
            messageText = view.findViewById(R.id.Textview3);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder messageViewHolder, int i) {

        Messages c = mMessagesList.get(i);

        messageViewHolder.state.setText(c.getType());
        messageViewHolder.time.setText(c.getTime());
        messageViewHolder.messageText.setText(c.getMessage());

    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }
}
