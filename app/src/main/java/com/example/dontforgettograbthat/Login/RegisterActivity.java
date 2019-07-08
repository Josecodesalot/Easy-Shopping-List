package com.example.dontforgettograbthat.Login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;

    //widgets
    private EditText mEmail;
    private EditText mPassword;
    private EditText mUsername;
    private Button submitBtn;

    //Constants
    final private Context mContext = RegisterActivity.this;

    //vars
    private String email, username, password, familyName;
    private Boolean allowUserToRegister, emailExists, usernameExists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allowUserToRegister=false;
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started");
        firebaseMethods = new FirebaseMethods(mContext);
        setupFirebaseAuth();
        firebaseDataExchangeListener();
        referenceWidgets();
        submitButtonLogic();


    }

    private void referenceWidgets(){
        Log.d(TAG, "referenceWidgets: started");
        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);
        mUsername = findViewById(R.id.etUsername);
        submitBtn = findViewById(R.id.btnSubmit);
    }

    private void submitButtonLogic(){
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();
                username = mUsername.getText().toString().toLowerCase();
                password = mPassword.getText().toString();

                emailExists(email);
                usernameExists(username);
            }
        });
    }

    private void singUp (){
        Log.d(TAG, "singUp: started");
        if(inputsArentEmpty(email, username, password)&&allowUserToRegister&&!emailExists){
            Log.d(TAG, "singUp: shold call firebase methods");
            ////mProgressBar.setVisibility(View.VISIBLE);
            // loadingPleaseWait.setVisibility(View.VISIBLE);
            firebaseMethods.registerNewEmail( email, password, username);
            mAuth.signOut();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("key","emailsent");

        }
    }

    private void usernameExists(final String username){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usernameExists = firebaseMethods.checkIfUsernameExists(username, dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        singUp();
    }
    private void emailExists(final String email){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailExists = firebaseMethods.emailExists(email,dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean inputsArentEmpty(String email, String username, String password){
        Log.d(TAG, "inputsArentEmpty: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (firebaseMethods.checkIfUsernameExists(mUsername.getText().toString().toLowerCase(), dataSnapshot)){
                        Toast.makeText(mContext, "Username is taken, please choose a new one.", Toast.LENGTH_SHORT).show();
                        allowUserToRegister =false;
                    }else{
                        allowUserToRegister =true;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }
     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

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

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
