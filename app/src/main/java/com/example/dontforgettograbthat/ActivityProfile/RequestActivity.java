package com.example.dontforgettograbthat.ActivityProfile;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dontforgettograbthat.Dialogs.AcceptOrRejectRequestDialog;
import com.example.dontforgettograbthat.Interface.AcceptDeleteOrHoldInterface;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.RecyclerViewAdapter;
import com.example.dontforgettograbthat.utils.RequestRvAdapter;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity implements AcceptDeleteOrHoldInterface {

    private static final String TAG = "RequestActivity";
    private Context mContext = RequestActivity.this;

    //  UTILS
    //RecyclerView Addapter
    RequestRvAdapter adapter;
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request").child(currentUser.getFamily_name());
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
        adapter = new RequestRvAdapter(
                mContext,
                users);
        Log.d(TAG, "onCreate: setUp ReciclerView");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void accept(int position) {
        Log.d(TAG, "accept: called for user " + users.get(position).toString());
        firebase.acceptRequest(currentUser.getFamily_name(), users.get(position));
        recreate();
    }

    @Override
    public void reject(int position) {
        Log.d(TAG, "reject: called");
        firebase.deleteRequest(currentUser.getFamily_name(),users.get(position));
        recreate();
    }

    @Override
    public void hold(int position) {
        Log.d(TAG, "hold: called");
        recreate();
    }

    @Override
    public void dialog(int position) {
        Log.d(TAG, "dialog: triggered " + users.get(position).getFamily_name());
        Bundle bundle = new Bundle();
        bundle.putString("id",users.get(position).getUser_id());
        bundle.putString("username", users.get(position).getUsername());
        bundle.putInt("position",position);
        dialog = new AcceptOrRejectRequestDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(),"1");
    }
}
