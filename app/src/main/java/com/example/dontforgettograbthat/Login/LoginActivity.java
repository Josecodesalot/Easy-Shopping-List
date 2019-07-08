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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;

    //widgets
    private EditText mEmail, mPassword;
    private Button mSubmitBtn;
    private TextView mCreateAccount;


    //var
    private String email, password;

    //constants
    private Context mContext = LoginActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupFirebaseAuth();
        setUpWidgets();
        init();
        getIncomingIntent();


        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void init(){

        //initialize the button for logging in

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to log in.");

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if(isStringNull(email) || isStringNull(password)){
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }else{

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail:failed", task.getException());

                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        try{
                                            if (user.isEmailVerified()){
                                                Intent intent = new Intent(mContext, CartActivity.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(mContext, "Email is not verified \n check your email inbox.", Toast.LENGTH_SHORT).show();
                                                mAuth.signOut();
                                            }
                                        }catch(NullPointerException e){
                                            Log.d(TAG, "OnComplete: Null pointerException" + e.getMessage());

                                        }
                                    }

                                    // ...
                                }
                            });
                }

            }
        });


         /*
         If the user is logged in then navigate to HomeActivity and call 'finish()'
          */
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(mContext, CartActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean isStringNull(String string){
        Log.d(TAG, "Checking if string is null");
        if (string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    private void setUpWidgets(){
        Log.d(TAG, "setUpWidgets: started");
        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);
        mSubmitBtn = findViewById(R.id.btnSubmit);
        mCreateAccount = findViewById(R.id.tvCreateAccount);

    }
    private void getIncomingIntent(){
        Intent intent = getIntent();

        if(intent.hasExtra(getString(R.string.extra_email_verification))){
            Log.d(TAG, "getIncomingIntent: received incoming intent from  Register Activity");
            Toast.makeText(mContext, intent.getStringExtra(getString(R.string.extra_email_verification))
                    , Toast.LENGTH_SHORT).show();
        }
    }
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //check if the user is logged in

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
        Intent intent = getIntent();
        if (intent!=null){
            Bundle bundle= intent.getExtras();
            if (bundle!=null){
                if (bundle.get("key").equals("emailsent")){
                    Toast.makeText(mContext, "Verification Email Sent, Please Check Email", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
