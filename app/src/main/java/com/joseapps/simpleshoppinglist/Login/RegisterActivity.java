package com.joseapps.simpleshoppinglist.Login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity

{

    private static final String TAG = "RegisterActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;

    //widgets
    private EditText mEmail;
    private EditText mPassword;
    private EditText mUsername;
    private Button submitBtn;
    private ProgressBar progressBar;

    //Constants
    final private Context mContext = RegisterActivity.this;

    //vars
    private String email, password;
    private  boolean emailExists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emailExists= false;
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
        submitBtn = findViewById(R.id.btnSendToFamilyList);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void submitButtonLogic(){
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();

                password = mPassword.getText().toString();

                singUp();
            }
        });
    }

    private void singUp (){
        Log.d(TAG, "singUp: started");
        if(inputsArentEmpty(email, password)&&!emailExists){
            Log.d(TAG, "singUp: shold call firebase methods");
            progressBar.setVisibility(View.VISIBLE);
            // loadingPleaseWait.setVisibility(View.VISIBLE);
            firebaseMethods.registerNewEmail(email, password, progressBar);
            mAuth.signOut();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("key","emailsent");

        }else{
            Log.d(TAG, "singUp: failed");
        }
    }




    private boolean inputsArentEmpty(String email, String password){
        Log.d(TAG, "inputsArentEmpty: checking inputs for null values.");
        if(email.equals("")  || password.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
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
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference();

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
