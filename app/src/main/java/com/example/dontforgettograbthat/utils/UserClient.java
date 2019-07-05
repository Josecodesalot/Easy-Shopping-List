package com.example.dontforgettograbthat.utils;

import android.app.Application;

import com.example.dontforgettograbthat.Models.User;
import com.google.firebase.database.FirebaseDatabase;

public class UserClient extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);    }

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
