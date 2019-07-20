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

import com.example.dontforgettograbthat.ActivityProfile.ChildrenMangementActivity;
import com.example.dontforgettograbthat.Interface.ChildrenManagementInterface;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;

import java.util.ArrayList;

public class RecyclerViewChildrenManagement extends RecyclerView.Adapter<RecyclerViewChildrenManagement.ViewHolder>{
    private static final String TAG = "RecyclerViewChildr";


    private ArrayList<User> mUser ;

    public RecyclerViewChildrenManagement(Context mContext, ArrayList<User> mUser) {
        this.mUser = mUser;
        Log.d(TAG, "RecyclerViewChildrenRequests: " + mUser.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false);
        RecyclerViewChildrenManagement.ViewHolder holder = new RecyclerViewChildrenManagement.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewChildrenManagement.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.tvUserName.setText(mUser.get(position).getUsername());
        holder.tvUserEmail.setText(mUser.get(position).getEmail());

        //this method will hide un used features in the recyclerview

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mInterface.OpenDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvUserName;
        TextView tvUserEmail;
        ConstraintLayout parentLayout;
        ChildrenManagementInterface mInterface;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            parentLayout = itemView.findViewById(R.id.list_root);

            Log.d(TAG, "ViewHolder: ChildRequestActivity");
            mInterface = (ChildrenMangementActivity)  itemView.getContext();

        }
    }
}