package com.example.dontforgettograbthat.ActivityProfile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dontforgettograbthat.Dialogs.AcceptOrRejectRequestDialog;
import com.example.dontforgettograbthat.Interface.ChildrenRequestInterface;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.RecyclerViewChildrenRequests;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity implements ChildrenRequestInterface {

    private static final String TAG = "RequestItemsActivity";
    private Context mContext = RequestActivity.this;

    //  UTILS
    //RecyclerView Addapter
    RecyclerViewChildrenRequests adapter;
    //FirebaseMethods
    FirebaseMethods firebase;
    //Vars
    ArrayList<User> users = new ArrayList<>();
    User currentUser;
    //Widgets
    RecyclerView recyclerView;
    AcceptOrRejectRequestDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        firebase = new FirebaseMethods(mContext);
        setUpUserList();
    }

    private void setUpUserList() {
        Log.d(TAG, "setUpUserList: ");
        currentUser = ((UserClient)(getApplicationContext())).getUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request").child(currentUser.getUsername());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singlesnapshot: dataSnapshot.getChildren()){
                    users.add(singlesnapshot.getValue(User.class));
                }
                for (int i = 0; i< users.size(); i ++){
                    Log.d(TAG, "onDataChange: users = " + users.get(i).toString());
                }
                setUpRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewChildrenRequests(
                mContext,
                users);
        Log.d(TAG, "onCreate: setUp ReciclerView");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void accept(User user) {
        user.setParent_name(currentUser.getUsername());
        firebase.acceptRequest(currentUser.getUsername(),user);
        recreate();
    }

    @Override
    public void reject(User user) {
        firebase.deleteRequest(currentUser.getUsername(),user.getUser_id());
        recreate();
    }


    @Override
    public void dialog(int position) {
        Log.d(TAG, "dialog: triggered " + users.get(position).getParent_name());
        dialog = AcceptOrRejectRequestDialog.newInstance(users.get(position));
        dialog.show(getSupportFragmentManager(),"1");
    }
}
