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
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.Interface.CartInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;


public class CartAddItemDialog extends DialogFragment {
    private Context mContext = getActivity();
    private static final String TAG = "CartAddItemDialog";
    private EditText etItemName, etQuantity;
    private ImageView addBtn;
    public Item item;
    CartInterface mInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cart_add_item, container, false);
        Log.d(TAG, "onCreateView: Started");

        mInterface = (CartActivity) getContext();

        ReferenceViews(view);
        RunButton();
        return view;
    }

    private void RunButton() {
        Log.d(TAG, "RunButton: Starting Run");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item = new Item(etItemName.getText().toString(), "default", 1,0.0,"");
                Log.d(TAG, "onClick: Item = " + item.toString());
                //sends Item to CartActivity so that cart activity can send it to Firebase and add it to the recyclerView
                mInterface.addItem(item);
                dismiss();
            }
        });
    }


    private void ReferenceViews(View view) {
        Log.d(TAG, "ReferenceViews: Started");
        addBtn = view.findViewById(R.id.imgAdd);
        etItemName = view.findViewById(R.id.etItemName);
        etQuantity = view.findViewById(R.id.etItemCount);
    }
}
