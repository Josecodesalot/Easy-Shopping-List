package com.example.dontforgettograbthat.ActivityProfile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.dontforgettograbthat.Dialogs.AddFamilyNameDialog;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileInfoActivity extends AppCompatActivity {

    private static final String TAG = "ProfileInfoActivity";
    Context mContext = ProfileInfoActivity.this;

    //Widgets
    private TextView email;
    private EditText username;
    private Button  submit, parentNameButton;
    private ImageView information;

    //vars
    private User user;
    private String sUsername, sParentUsername;
    private Boolean allowUsername, allowParentName;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebase;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        Log.d(TAG, "onCreate: Started");;

        allowUsername = false;

        setupFirebaseAuth();
        database = FirebaseDatabase.getInstance();
        firebase = new FirebaseMethods(mContext);
        firebaseDataExchangeListener();
        user = new User();
        user = ((UserClient)(getApplicationContext())).getUser();
        if (user!=null) {
            Log.d(TAG, "onCreate: user " + user.toString());
        }

        setUpWidgets();
        submitButtonLogic();

    }

    private void submitButtonLogic() {
        Log.d(TAG, "submitButtonLogic: ");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sUsername = username.getText().toString().toLowerCase();

                if (sUsername.equals("")) {
                    Toast.makeText(mContext, "Please Type your desired Username", Toast.LENGTH_SHORT).show();
                }else {
                    Query qry = database.getReference().child("users").orderByChild("username").equalTo(sUsername);

                    qry.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Toast.makeText(mContext, "Username Is Taken", Toast.LENGTH_SHORT).show();
                            }else{
                                beginTransaction();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }


    private void beginTransaction() {
        Log.d(TAG, "beginTransaction: ");

        Log.d(TAG, "beginTransaction: allow username");
        User user = new User();
        user.setEmail(mAuth.getCurrentUser().getEmail());
        user.setUser_id(mAuth.getCurrentUser().getUid());
        user.setUsername(sUsername);

        if (this.user!=null){
            user.setParent_name(this.user.getParent_name());
        }else{
            user.setParent_name("");
        }

        //update user singleton

        ((UserClient)(getApplicationContext())).setUser(user);

        Toast.makeText(mContext, "User Set", Toast.LENGTH_SHORT).show();
        firebase.addNewUser(user);

    }


    private void setUpWidgets() {
        email = findViewById(R.id.tvEmail);
        email.setText(mAuth.getCurrentUser().getEmail());
        username = findViewById(R.id.etUserName);
        parentNameButton = findViewById(R.id.btnSetUpParent);
        information = findViewById(R.id.imgInfo);
        submit = findViewById(R.id.btnSubmit);
         parentNameButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AddFamilyNameDialog dialog = new AddFamilyNameDialog();
                 dialog.show(getSupportFragmentManager(),"");
             }
         });
        if (user!=null){

            username.setText(user.getUsername());

        }

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"fix this",Toast.LENGTH_LONG).show();
            }
        });
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
