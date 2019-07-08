package com.example.dontforgettograbthat.ActivityProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.dontforgettograbthat.Login.LoginActivity;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private Context mContext = ProfileActivity.this;

    //WIdgets
    private TextView username, email;
    private EditText parentName;
    private Button signoutBtn, setParent, requestsBtn ;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebase;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //vars
    private User user;
    private Boolean allowParentname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: Started");
        allowParentname = false;
        setupFirebaseAuth();
        database = FirebaseDatabase.getInstance();
        firebase = new FirebaseMethods(mContext);
        firebaseDataExchangeListener();
        user = new User();
        user = ((UserClient)(getApplicationContext())).getUser();
        referenceWidgets();

    }

    public void referenceWidgets (){

        username = findViewById(R.id.tvUserName);
        email = findViewById(R.id.tvLogin);
        parentName = findViewById(R.id.etUsername);
        //signoutBtn has an onclick which creates a Signout
        signoutBtn = findViewById(R.id.btnSignout);
        //setParent is a button that sends a request to the family defined in etFamilyName if that family exists
        //the request will give the family manager the choice to allow items into their Main ShoppingCart
        setParent = findViewById(R.id.btnSetParent);

        requestsBtn = findViewById(R.id.btnRequests);

        username.setText(user.getUsername());
        email.setText(user.getUsername());
        if (user.getParent_name().equals("")){
            parentName.setText("");
            parentName.setHint("Type in your parents username");
        }else {
            parentName.setText(user.getParent_name());
        }

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.logoutAndClearStack();
            }
        });


        setParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String familyName = parentName.getText().toString().toLowerCase();

                DatabaseReference ref = database.getReference().child("users");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (firebase.parentNameExists(user, familyName, dataSnapshot)) {
                                allowParentname =true;
                                requestAndToast();
                            }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        
                    }
                });
                
                if (allowParentname = allowParentname == null){
                    Log.d(TAG, "onClick: allowFamilyNameRequests = false" );
                    Toast.makeText(mContext, "False", Toast.LENGTH_SHORT).show();
                }


            }
        });

        requestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RequestActivity.class);
                startActivity(intent);
            }
        });

    }

    private void requestAndToast() {

        if (allowParentname){
            firebase.sendParentRequest(parentName.getText().toString(), user.getUser_id(), user);
            Toast.makeText(mContext, "Family Request Sent", Toast.LENGTH_SHORT).show();
        }else{
            Log.d(TAG, "onClick: allow familyNameRequest is false");
            Toast.makeText(mContext, "The family name entered doesn't exist. Please Double Check With The Parent", Toast.LENGTH_SHORT).show();
        }

    }

    //Firebase Code
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
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
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Log.d(TAG, "checkCurrentUser: user is null, starting loginActivity");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }
}
