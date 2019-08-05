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
    private ArrayList<Boolean> listBool = new ArrayList<>();
    private Context mContext;
    private RvTopTabsInterface mInterface;
    private int selectedPosition;


    public RecyclerviewItemTabs (Context mContext, ArrayList<String>listNames){
        this.listNames=listNames;
        this.mContext=mContext;
        setUpBoolean();
    }

    public  int getSelectedPosition(){
        return selectedPosition;
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
        setUpBoolean();
        viewHolder.tvListname.setText(listNames.get(i));
        viewHolder.tvListname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.setRvData(listNames.get(viewHolder.getAdapterPosition()));
                selectedPosition = viewHolder.getAdapterPosition();
                //setopTabDrawable(viewHolder);
            }
        });
     //   setopTabDrawable(viewHolder);

        if (viewHolder.getAdapterPosition() == 0) {
            viewHolder.tvListname.callOnClick();
        }
    }

    private void setopTabDrawable(ViewHolder viewHolder){
        if (viewHolder.getAdapterPosition() == selectedPosition){
            viewHolder.tvListname.setBackgroundResource(R.drawable.background_rounded_borered_border);
        }else{
            viewHolder.tvListname.setBackgroundResource(R.drawable.background_rounded_gwhite);
        }
    }


    public void setUpBoolean(){
        for (int i=0; i<listNames.size(); i++){
            listBool.add(false);
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
