package com.joseapps.simpleshoppinglist.Dialogs;

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

import com.joseapps.simpleshoppinglist.ActivityCart.CartActivity;
import com.joseapps.simpleshoppinglist.Interface.CartInterface;
import com.joseapps.simpleshoppinglist.Models.Item;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.Const;
import com.joseapps.simpleshoppinglist.utils.NotNull;


public class CartAddItemDialog extends DialogFragment {
    private Context mContext = getActivity();
    private static final String TAG = "CartAddItemDialog";
    private EditText etItemName, etQuantity;
    private ImageView addBtn;
    private Item item;
    private CartInterface mInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cart_add_item, container, false);
        Log.d(TAG, "onCreateView: Started");

        mInterface = (CartActivity) getContext();
        item = new Item();
        item  = NotNull.item(item);
        ReferenceViews(view);
        RunButton();
        return view;
    }

    private void RunButton() {
        Log.d(TAG, "RunButton: Starting Run");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setItem_name(etItemName.getText().toString());

                if (!etQuantity.getText().toString().isEmpty()){
                    long q = Long.parseLong(etQuantity.getText().toString());
                    item.setQuantity(q);
                }

                Log.d(TAG, "onClick: Item = " + item.toString());
                //sends Item to CartActivity so that cart activity can send it to Firebase and add it to the recyclerView
                mInterface.addItem(item, Const.ADDITEM);
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
