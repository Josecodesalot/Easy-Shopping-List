package com.example.dontforgettograbthat.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dontforgettograbthat.HistoryActivity.HistoryActivity;
import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.Dialogs.HistoryDialog;
import com.example.dontforgettograbthat.Dialogs.ItemDialog;
import com.example.dontforgettograbthat.Dialogs.RequestDialog;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.Request.RequestActivity;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mItemNames = new ArrayList<>();
    private ArrayList<String> mItemListName = new ArrayList<>();
    private ArrayList<Double> mItemPrice;
    private ArrayList<Long> mItemCount;
    private ArrayList<String> mId;
    private Context mContext;
    private FragmentManager manager;
    private String familyName;



    public RecyclerViewAdapter(Context context,
                               ArrayList<String> itemNames,
                               ArrayList<String> listNames,
                               ArrayList<Double> itemPrice,
                               ArrayList<Long> itemCount,
                               ArrayList<String> id, String famName) {
        mItemNames = itemNames;
        mItemListName = listNames;
        mItemPrice = itemPrice;
        mContext = context;
        mItemCount = itemCount;
        mId = id;
        familyName = famName;
        Log.d(TAG, "RecyclerViewAdapter: family name = " + famName + " " + familyName);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.tvItemListName.setText(mItemListName.get(position));
        holder.tvItemName.setText(mItemNames.get(position));
        holder.tvPrice.setText("$" + mItemPrice.get(position));
        holder.tvItemCount.setText("x" + mItemCount.get(position));
        holder.tvItemListName.setBackground(getDrawable(mItemListName.get(position)));





        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putString("tvItemName", mItemNames.get(position));
                args.putString("tvListName", mItemListName.get(position));
                args.putLong("tvQuantity", mItemCount.get(position));
                args.putDouble("tvPrice",  mItemPrice.get(position));
                args.putString("id", mId.get(position));
                args.putString("familyName", familyName);


                if (mContext instanceof CartActivity) {
                    manager = ((AppCompatActivity)mContext ).getSupportFragmentManager();
                    ItemDialog alert =  new ItemDialog();
                    alert.setArguments(args);
                    assert manager != null;
                    alert.show(manager, "1");
                }
                if (mContext instanceof HistoryActivity) {
                    Log.d(TAG, "onClick: History Activity ");
                    manager = ((AppCompatActivity)mContext ).getSupportFragmentManager();
                    HistoryDialog alert =  new HistoryDialog();
                    alert.setArguments(args);
                    assert manager != null;
                    alert.show(manager, "2");
                }
                if (mContext instanceof RequestActivity) {
                    Log.d(TAG, "onClick: History Activity ");
                    manager = ((AppCompatActivity)mContext ).getSupportFragmentManager();
                    RequestDialog alert =  new RequestDialog();
                    alert.setArguments(args);
                    assert manager != null;
                    alert.show(manager, "3");
                }
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
        return mItemNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemName;
        TextView tvItemListName;
        TextView tvPrice;
        TextView tvItemCount;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemListName = itemView.findViewById(R.id.tvListName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            parentLayout = itemView.findViewById(R.id.list_root);
            tvItemCount =  itemView.findViewById(R.id.tvItemCount);
        }
    }
}
