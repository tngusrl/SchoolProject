package com.example.managament;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    RequestOptions options;
    private Context mContext;
    private List<SellBookDB> mData;

    private List<SellBookDB> unFilteredlist_sell;
    private List<SellBookDB> filteredList_sell;

    private List<BuyBookDB> mData1;

    private List<BuyBookDB> unFilteredlist_buy;
    private List<BuyBookDB> filteredList_buy;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private String division;


    public RecyclerViewAdapter(Context mContext, List lst, String division) {
        this.mContext = mContext;

        if (division.equals("sell")) {
            this.mData = lst;
            unFilteredlist_sell = lst;
            filteredList_sell = lst;
            this.division = division;
        } else if (division.equals("buy")) {
            this.mData1 = lst;
            unFilteredlist_buy = lst;
            filteredList_buy = lst;
            this.division = division;
        }

        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.listview_item1, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.listview_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            populateItemRows((MyViewHolder) holder, position);

        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);

        }
    }

    @Override
    public int getItemCount() {
        return filteredList_sell == null ? filteredList_buy.size() : filteredList_sell.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (filteredList_sell != null) {
            return filteredList_sell.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        } else {
            return filteredList_buy.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(division.equals("buy")){

                    String charString = constraint.toString();
                    if(charString.isEmpty()) {
                        filteredList_buy = unFilteredlist_buy;
                    } else {
                        ArrayList<BuyBookDB> filteringList = new ArrayList<>();
                        for(BuyBookDB name : unFilteredlist_buy) {
                            if(name.getBookname().toLowerCase().contains(charString.toLowerCase())) {
                                filteringList.add(name);
                            }
                        }
                        filteredList_buy = filteringList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList_buy;
                    return filterResults;

                }

                else{

                    String charString = constraint.toString();
                    if(charString.isEmpty()) {
                        filteredList_sell = unFilteredlist_sell;
                    } else {
                        ArrayList<SellBookDB> filteringList = new ArrayList<>();
                        for(SellBookDB name : unFilteredlist_sell) {
                            if(name.getBookname().toLowerCase().contains(charString.toLowerCase())) {
                                filteringList.add(name);
                            }
                        }
                        filteredList_sell = filteringList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList_sell;
                    return filterResults;

                }


            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if(division.equals("sell")){
                    filteredList_sell = (ArrayList<SellBookDB>)results.values;
                    notifyDataSetChanged();
                }

                else {
                    filteredList_buy = (ArrayList<BuyBookDB>)results.values;
                    notifyDataSetChanged();
                }

            }
        };
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7,tv15;
        ImageView BookThumbnail;
        FrameLayout view_container;
        View view;
        TextView visible;
        ImageView sale;

        public MyViewHolder(View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            view = itemView.findViewById(R.id.view);
            visible = itemView.findViewById(R.id.visible);
            tv1 = itemView.findViewById(R.id.textView1); // 도서명
            tv2 = itemView.findViewById(R.id.textView2); // 출판사
            tv3 = itemView.findViewById(R.id.textView3); // 저자명
            tv5 = itemView.findViewById(R.id.textView5); // 판매가
            tv6 = itemView.findViewById(R.id.textView6); // 정가
            tv7 = itemView.findViewById(R.id.textView7); // 퍼센트 100-(판매가/정가).반올림
            tv15 = itemView.findViewById(R.id.textView15); // 조회수

            sale = itemView.findViewById(R.id.sale); // 급매

            BookThumbnail = itemView.findViewById(R.id.imageView1);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(MyViewHolder viewHolder, int position) {

        if (filteredList_sell != null) {

            viewHolder.tv1.setText(filteredList_sell.get(position).getBookname());
            viewHolder.tv2.setText(filteredList_sell.get(position).getPublisher());
            viewHolder.tv3.setText(filteredList_sell.get(position).getAuthor());
            viewHolder.tv5.setText(filteredList_sell.get(position).getWantbookprice());
            viewHolder.tv6.setText(filteredList_sell.get(position).getBookprice());
            viewHolder.tv6.setPaintFlags(viewHolder.tv6.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tv15.setText(filteredList_sell.get(position).getViews() + "회");

            String a = filteredList_sell.get(position).getBookprice();
            String b = filteredList_sell.get(position).getWantbookprice();

            a = a.replace("원", "");
            b = b.replace("원", "");

            double c = Double.parseDouble(a);
            double d = Double.parseDouble(b);

            double e = 100 - (d / c) * 100;

            int i = (int) e;
            viewHolder.tv7.setText("(" + i + "% 할인)");

            Glide.with(mContext).load(filteredList_sell.get(position).getBookCover()).apply(options).into(viewHolder.BookThumbnail);

            if (filteredList_sell.get(position).getTrading().equals("true")) {
                viewHolder.visible.setText("판매완료");
                viewHolder.visible.setVisibility(View.VISIBLE);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAlpha(30);
                viewHolder.view.setBackgroundColor(paint.getColor());
                viewHolder.view.bringToFront();
                viewHolder.visible.bringToFront();
            } else {
                viewHolder.visible.setVisibility(View.GONE);
            }

            if (i >= 70) {

                viewHolder.sale.setVisibility(View.VISIBLE);
            } else {
                viewHolder.sale.setVisibility(View.GONE);
            }


        } else if (filteredList_buy != null) {

            viewHolder.tv1.setText(filteredList_buy.get(position).getBookname());
            viewHolder.tv2.setText(filteredList_buy.get(position).getPublisher());
            viewHolder.tv3.setText(filteredList_buy.get(position).getAuthor());
            viewHolder.tv5.setText(filteredList_buy.get(position).getWantbookprice());
            viewHolder.tv6.setText(filteredList_buy.get(position).getBookprice());
            viewHolder.tv6.setPaintFlags(viewHolder.tv6.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tv15.setText(filteredList_buy.get(position).getViews() + "회");


            String a = filteredList_buy.get(position).getBookprice();
            String b = filteredList_buy.get(position).getWantbookprice();

            a = a.replace("원", "");
            b = b.replace("원", "");

            double c = Double.parseDouble(a);
            double d = Double.parseDouble(b);

            double e = 100 - (d / c) * 100;

            int i = (int) e;
            viewHolder.tv7.setText("(" + i + "% 할인)");

            Glide.with(mContext).load(filteredList_buy.get(position).getFileName()).apply(options).into(viewHolder.BookThumbnail);

            if (filteredList_buy.get(position).getTrading().equals("true")) {
                viewHolder.visible.setText("구매완료");
                viewHolder.visible.setVisibility(View.VISIBLE);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAlpha(30);
                viewHolder.view.setBackgroundColor(paint.getColor());
                viewHolder.view.bringToFront();
                viewHolder.visible.bringToFront();
            } else {
                viewHolder.visible.setVisibility(View.GONE);
            }

            if (i >= 70) {
                viewHolder.sale.setVisibility(View.VISIBLE);
            } else {
                viewHolder.sale.setVisibility(View.GONE);
            }
        }
    }
}
