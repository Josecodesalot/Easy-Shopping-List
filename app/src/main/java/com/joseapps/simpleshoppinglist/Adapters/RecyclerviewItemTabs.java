package com.joseapps.simpleshoppinglist.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.joseapps.simpleshoppinglist.ActivityCart.CartActivity;
import com.joseapps.simpleshoppinglist.Interface.RvTopTabsInterface;
import com.joseapps.simpleshoppinglist.R;

import java.util.ArrayList;

public class RecyclerviewItemTabs extends RecyclerView.Adapter<RecyclerviewItemTabs.ViewHolder> {
    private ArrayList<String>listNames;
    private Context mContext;
    private RvTopTabsInterface mInterface;
    public RecyclerviewItemTabs (Context mContext, ArrayList<String>listNames){
        this.listNames=listNames;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public RecyclerviewItemTabs.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_tabs, viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        mInterface = (CartActivity) viewGroup.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvListname.setText(listNames.get(i));
        viewHolder.tvListname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.setRvData(listNames.get(viewHolder.getAdapterPosition()));
            }
        });

        if (viewHolder.getAdapterPosition()==0){
            viewHolder.tvListname.callOnClick();
        }
    }


    @Override
    public int getItemCount() {
        return listNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvListname;
       ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvListname = itemView.findViewById(R.id.tvListName);
        }
    }

}
