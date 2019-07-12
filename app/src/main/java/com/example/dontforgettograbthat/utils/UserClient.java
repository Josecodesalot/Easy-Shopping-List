package com.example.dontforgettograbthat.utils;

import android.app.Application;

import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserClient extends Application {
    private User user = null;
    private Item item = null;

    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
    public void setItem(Item item){
        this.item=item;
    }

    public Item getItem() {
        return item;
    }

    public void initializeItem(){
        this.item.setPrice(0.0);
        this.item.setItemKey("");
        this.item.setItem_name("");
        this.item.setList_name("");
        this.item.setQuantity(1);
    }
}
