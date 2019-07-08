package com.example.dontforgettograbthat.utils;

import android.app.Application;

import com.example.dontforgettograbthat.Models.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserClient extends Application {
    private User user = null;

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


}
