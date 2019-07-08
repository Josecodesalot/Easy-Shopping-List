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

import com.example.dontforgettograbthat.ActivityProfile.RequestActivity;
import com.example.dontforgettograbthat.Interface.AcceptDeleteOrHoldInterface;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;

import java.util.ArrayList;

public class RequestRvAdapter extends RecyclerView.Adapter<RequestRvAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";


    private ArrayList<User> mUser = new ArrayList<>();
    public RequestRvAdapter(Context mcontext, ArrayList<User> mUser) {
        this.mUser = mUser;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_user, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.tvUserName.setText(mUser.get(position).getUsername());
        holder.tvUserEmail.setText(mUser.get(position).getUsername());


        //this method will hide un used features in the recyclerview




        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Open up a Dialog do that you can either add or delete this person.
                holder.mInterface.dialog(position);
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
        AcceptDeleteOrHoldInterface mInterface;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            parentLayout = itemView.findViewById(R.id.list_root);
            mInterface = (RequestActivity)  itemView.getContext();
        }
    }
}