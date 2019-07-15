package com.example.dontforgettograbthat.AddItemActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

import com.example.dontforgettograbthat.Interface.IAddItem;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.UserClient;

import java.util.ArrayList;

public class FIrstItemNameFragment extends android.support.v4.app.Fragment {
    private static final String TAG="AddNameAndQuan";
    private EditText itemName, itemQuantity;
    private IAddItem mInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_quantity, container,false);
        Log.d(TAG, "onCreateView: ");
        referenceViews(view);

        mInterface = (AddItemActivity) getContext();

        itemName.requestFocus();
        itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "onFocusChange: Item Name data sent");
                    mInterface.setItemName(itemName.getText().toString());
               }
            }
        });

        itemName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.d(TAG, "onKey: key code =" + keyCode);
                    itemQuantity.callOnClick();
                }
                return false;
            }
        });

        itemQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!itemQuantity.getText().toString().equals("")){
                        mInterface.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                        Log.d(TAG, "onFocusChange: itemQuantity sending data");

                    }else {
                        mInterface.setQuantity(0);
                        Log.d(TAG, "onFocusChange: itemQuantity sending data ");
                    }
                }
            }
        });
        return view;
    }


    private void referenceViews(View view) {
    itemName= view.findViewById(R.id.etItemName);
    itemQuantity = view.findViewById(R.id.etQuantity);
    }
}