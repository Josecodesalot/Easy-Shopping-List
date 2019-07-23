package com.example.dontforgettograbthat.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dontforgettograbthat.ActivityFamilyList.FamilyListActivity;
import com.example.dontforgettograbthat.ActivityCart.CartActivity;
import com.example.dontforgettograbthat.ActivityHistory.HistoryActivity;
import com.example.dontforgettograbthat.Interface.RecyclerViewInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;


import java.util.ArrayList;

public class RecyclerViewItems extends RecyclerView.Adapter<RecyclerViewItems.ViewHolder>{
    private static final String TAG = "RecyclerViewItems";

    private final ArrayList<Item> items;
    private final Context mContext;
    private RecyclerViewInterface mInterface;

    public RecyclerViewItems(Context context,
                             ArrayList<Item> items) {
        Log.d(TAG, "RecyclerViewItems: called");
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
        if (mContext instanceof FamilyListActivity) {
            Log.d(TAG, "onCreateViewHolder: FamilyListActivity");
            mInterface = (FamilyListActivity) parent.getContext();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.tvPrice.setVisibility(View.VISIBLE);
        holder.tvItemListName.setText(items.get(position).getList_name());
        holder.tvItemName.setText(items.get(position).getItem_name());
        String price = "$" + items.get(position).getPrice();
        holder.tvPrice.setText(price);

        //hide the recyclerView Features
        if (items.get(position).getPrice()!=null) {
            if (items.get(position).getPrice() == 0.0) {
                holder.tvPrice.setVisibility(View.GONE);
            }else {
                String quanitity = "x" + items.get(position).getQuantity();
                holder.tvItemQuantity.setText(quanitity);
                holder.tvItemQuantity.setVisibility(View.VISIBLE);
            }
        }


        if (items.get(position).getQuantity()==1){
            holder.tvItemQuantity.setVisibility(View.GONE);
        }else{
            holder.tvItemQuantity.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "onBindViewHolder: " + items.get(0).toString());
        //holder.tvItemListName.setBackground(GetListDrawable.getDrawable(items.get(position).getList_name(),mContext));

        //this method will hide un used features in the recyclerview

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: positon = " + position);
                mInterface.OpenDialog(position);
            }

        });

    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: itemsize " + items.size());
        items.trimToSize();
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView tvItemName;
        final TextView tvItemListName;
        final TextView tvPrice;
        final TextView tvItemQuantity;
        final ConstraintLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemListName = itemView.findViewById(R.id.tvListName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            parentLayout = itemView.findViewById(R.id.list_root);
            tvItemQuantity =  itemView.findViewById(R.id.tvItemCount);
        }
    }

}
