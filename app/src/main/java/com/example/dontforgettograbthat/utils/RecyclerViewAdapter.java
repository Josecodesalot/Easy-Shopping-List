package com.example.dontforgettograbthat.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dontforgettograbthat.ActivityRequestItems.RequestItemsActivity;
import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.HistoryActivity.HistoryActivity;
import com.example.dontforgettograbthat.Interface.RecyclerViewInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Item> items;
    private Context mContext;
    private FragmentManager manager;
    private String familyName;
    RecyclerViewInterface mInterface;


    public RecyclerViewAdapter(Context context,
                               ArrayList<Item> items) {
        Log.d(TAG, "RecyclerViewAdapter: called");
       this.items=items;
        mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);

        if (mContext instanceof CartActivity) {
            Log.d(TAG, "onCreateViewHolder: CartActivity");
            mInterface = (CartActivity) parent.getContext();
        }
        if (mContext instanceof HistoryActivity) {
            Log.d(TAG, "onCreateViewHolder: HistoryActvity");
            mInterface = (HistoryActivity) parent.getContext();
        }
        if (mContext instanceof RequestItemsActivity) {
            Log.d(TAG, "onCreateViewHolder: RequestItemsActivity");
            mInterface = (RequestItemsActivity) parent.getContext();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.tvItemListName.setText(items.get(position).getList_name());
        holder.tvItemName.setText(items.get(position).getItem_name());
        String price = "$" + items.get(position).getPrice();
        holder.tvPrice.setText(price);

        //hide the recyclerView Features
        if (items.get(position).getPrice()==0.0){
            holder.tvPrice.setVisibility(View.GONE);
        }
        String quanitity = "x" + items.get(position).getQuantity();
        holder.tvItemQuantity.setText(quanitity);

        if (items.get(position).getQuantity()==1){
            holder.tvItemQuantity.setVisibility(View.GONE);
        }

        holder.tvItemListName.setBackground(getDrawable(items.get(position).getItem_name()));

        //this method will hide un used features in the recyclerview




        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mInterface.OpenDialog(position);
            }

        });

    }



    private Drawable getDrawable (String tvListName){
        Drawable color;
        switch (tvListName){
            case "Walmart":
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_walmart);
                        break;
            case "Costco" :
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_costco);
                break;
            case "Zhers" :
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_zhers);
                break;

            case "Metro" :
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_metro);
                break;
            default :
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_custom);
                break;
        }
        return color;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemName;
        TextView tvItemListName;
        TextView tvPrice;
        TextView tvItemQuantity;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemListName = itemView.findViewById(R.id.tvListName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            parentLayout = itemView.findViewById(R.id.list_root);
            tvItemQuantity =  itemView.findViewById(R.id.tvItemCount);
        }
    }
}
