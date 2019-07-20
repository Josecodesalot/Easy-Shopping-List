package com.example.dontforgettograbthat.utils;

import com.example.dontforgettograbthat.Models.Item;

public class NotNull {
    public static Item item(Item item){
        if (item.getPrice()==null){
            item.setPrice(0.0);
        }
        if (item.getItem_name()==null){
            item.setItemKey("");
        }
        if (item.getItemKey()==null){
            item.setItemKey("");
        }
        if (item.getList_name()==null){
            item.setList_name("");
        }
        return item;
    }
}
