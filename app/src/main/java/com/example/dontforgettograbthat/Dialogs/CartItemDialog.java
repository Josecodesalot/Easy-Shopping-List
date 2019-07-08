package com.example.dontforgettograbthat.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.Interface.CartInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;

public class CartItemDialog extends DialogFragment {

    private static final String TAG = "CartItemDialog";
    private Button btnDeleteFromCartList, btnAddToHistory;
    private TextView tvItemName, tvListname, tvPrice;
    CartInterface mInterface;
    Item item;

    public static CartItemDialog newInstance(Item item) {
        CartItemDialog frag = new CartItemDialog();
        frag.setItems(item);
        return frag;
    }

    public void setItems(Item item) {
        this.item=item;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_item, container, false);
        referenceWidgets(view);
        mInterface = (CartActivity) getContext();

            tvListname.setText(item.getList_name());
            tvItemName.setText(item.getItem_name());
            tvPrice.setText(String.format("%s", item.getPrice()));

        btnAddToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.addToHistory(item);
                mInterface.delete(item.getItemKey());
                dismiss();
            }
        });

        btnDeleteFromCartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: should delete key " + item.getItemKey());
                mInterface.delete(item.getItemKey());
                dismiss();
            }
        });
        return view;
    }

    public void referenceWidgets(View view){
        tvItemName = view.findViewById(R.id.tvItemName);
        tvListname = view.findViewById(R.id.tvListName);
        tvPrice =  view.findViewById(R.id.tvPrice);
        btnDeleteFromCartList = view.findViewById(R.id.btnDeleteFromList);
        btnAddToHistory = view.findViewById(R.id.btnBought);
    }
}
