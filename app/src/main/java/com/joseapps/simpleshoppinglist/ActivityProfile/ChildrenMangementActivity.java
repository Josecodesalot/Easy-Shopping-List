package com.joseapps.simpleshoppinglist.ActivityProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.joseapps.simpleshoppinglist.Dialogs.ChildrenManagementDialog;
import com.joseapps.simpleshoppinglist.Interface.ChildrenManagementInterface;
import com.joseapps.simpleshoppinglist.Login.LoginActivity;
import com.joseapps.simpleshoppinglist.Models.User;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.Const;
import com.joseapps.simpleshoppinglist.utils.FirebaseMethods;
import com.joseapps.simpleshoppinglist.utils.RecyclerViewChildrenManagement;

import com.joseapps.simpleshoppinglist.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChildrenMangementActivity extends AppCompatActivity implements ChildrenManagementInterface {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebase;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private User userCurrent;
    //Vars
    private ArrayList<User> users ;

    private static final String TAG = "ChildrenMangement";
    private final Context mContext = ChildrenMangementActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_children);

        setupFirebaseAuth();
        database = FirebaseDatabase.getInstance();
        firebase = new FirebaseMethods(mContext);
        firebaseDataExchangeListener();
        userCurrent = new User();
        users = new ArrayList<>();
        setUpUserList();

    }

    private void setUpUserList() {
        Log.d(TAG, "setUpUserList: ");
        userCurrent = ((UserClient)(getApplicationContext())).getUser();
        DatabaseReference ref = database.getReference().child(Const.FAMILY_USER).child(userCurrent.getUser_id());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: called");
                for (DataSnapshot singlesnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: " + singlesnapshot.toString());
                    users.add(singlesnapshot.getValue(User.class));
                }

                setUpRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setUpRecyclerView() {
        //Widgets
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewChildrenManagement adapter = new RecyclerViewChildrenManagement(
                mContext,
                users);
        Log.d(TAG, "onCreate: setUp ReciclerView");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    //Firebase Code
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //check if the userCurrent is logged in
                checkCurrentUser(user);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    private void firebaseDataExchangeListener(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: data has changed" + dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started get extras");
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }


    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if userCurrent is logged in.");

        if(user == null){
            Log.d(TAG, "checkCurrentUser: userCurrent is null, starting loginActivity");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void OpenDialog(int position) {
        ChildrenManagementDialog dialog = ChildrenManagementDialog.newInstance(users.get(position));
        dialog.show(getSupportFragmentManager(),"");
    }

    @Override
    public void Delete(User user) {
        // FirebaseDeleteUserFromChildren
        firebase.deleteFamilyMember(userCurrent,user);
        recreate();
    }

    @Override
    public void SendBack(User user) {
        // Firebase Delete Child and Add child To Requests
        firebase.sendUserToRequest(userCurrent, user);
        recreate();
    }
}
