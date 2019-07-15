package com.example.dontforgettograbthat.utils;

import android.support.design.widget.TabLayout;
import android.util.Log;

import com.example.dontforgettograbthat.Models.Item;
import com.google.firebase.database.FirebaseDatabase;


public class nonNull {
    private static final String TAG ="nonNull";

    public static Item item (Item thisItem) {
        Log.d(TAG, "item: " + thisItem.toString());
        if (thisItem.getQuantity() <= 0) {
            thisItem.setQuantity(1);
        }
        if (thisItem.getItem_name() == null) {
            thisItem.setItem_name(" ");
        }
        if (thisItem.getList_name() == null) {
            thisItem.setList_name(" ");
        }
        if (thisItem.getPrice() == null) {
            thisItem.setPrice(0.00);
        }
        if (thisItem.getItemKey() == null) {
            thisItem.setItemKey(FirebaseDatabase.getInstance().getReference().push().getKey());
        }
        return thisItem;
    }

    }

