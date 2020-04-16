package com.example.managament;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AddSellSearchRecyclerViewAdapter extends RecyclerView.Adapter<AddSellSearchRecyclerViewAdapter.CustomViewHolder>  {

    private ArrayList<BookInformation> mData;
    private Context mContext;
    RequestOptions options;
    String division;

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView 도서명, 출판사, 저자명, 출판일, 정가;
        ImageView BookThumbnail;
        FrameLayout view_container;

        public CustomViewHolder(View view){
            super(view);
            view_container = view.findViewById(R.id.container);
            도서명 = view.findViewById(R.id.textView1);
            출판사 = view.findViewById(R.id.textView2);
            저자명 = view.findViewById(R.id.textView3);
            출판일 = view.findViewById(R.id.textView4);
            정가 = view.findViewById(R.id.textView5);
            BookThumbnail = view.findViewById(R.id.imageView1);
        }
    }

    public AddSellSearchRecyclerViewAdapter(Context mContext, ArrayList<BookInformation> list, String division){
        this.mData = list;
        this.mContext = mContext;
        this.division = division;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_item, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(division.equals("buy")) {
                    Intent i = new Intent(mContext, MainActivity2.class);
                    i.putExtra("도서명", mData.get(viewHolder.getAdapterPosition()).getBookname1());
                    i.putExtra("저자명", mData.get(viewHolder.getAdapterPosition()).getAuthor1());
                    i.putExtra("출판사", mData.get(viewHolder.getAdapterPosition()).getPublisher1());
                    i.putExtra("정가", mData.get(viewHolder.getAdapterPosition()).getBookprice1());
                    i.putExtra("도서표지", mData.get(viewHolder.getAdapterPosition()).getFilename1());
                    i.putExtra("출간일", mData.get(viewHolder.getAdapterPosition()).getPublication());
//                    i.putExtra("별점",mData.get(viewHolder.getAdapterPosition()).getRating());
//                    i.putExtra("목차이미지",mData.get(viewHolder.getAdapterPosition()).getIndexImage());
                    mContext.startActivity(i);
                }

                else if(division.equals("sell")) {
                    Intent i = new Intent(mContext, MainActivity.class);
                    i.putExtra("도서명", mData.get(viewHolder.getAdapterPosition()).getBookname1());
                    i.putExtra("저자명", mData.get(viewHolder.getAdapterPosition()).getAuthor1());
                    i.putExtra("출판사", mData.get(viewHolder.getAdapterPosition()).getPublisher1());
                    i.putExtra("정가", mData.get(viewHolder.getAdapterPosition()).getBookprice1());
                    i.putExtra("도서표지", mData.get(viewHolder.getAdapterPosition()).getFilename1());
                    i.putExtra("출간일", mData.get(viewHolder.getAdapterPosition()).getPublication());
                    i.putExtra("별점",mData.get(viewHolder.getAdapterPosition()).getRating());
                    i.putExtra("목차이미지",mData.get(viewHolder.getAdapterPosition()).getIndexImage());
                    mContext.startActivity(i);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int position){

        viewHolder.도서명.setText(mData.get(position).getBookname1());
        viewHolder.출판사.setText(mData.get(position).getPublisher1());
        viewHolder.저자명.setText(mData.get(position).getAuthor1());
        viewHolder.정가.setText(mData.get(position).getBookprice1());
        viewHolder.출판일.setText(mData.get(position).getPublication());
        Glide.with(mContext).load(mData.get(position).getFilename1()).apply(options).into(viewHolder.BookThumbnail);
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

}
