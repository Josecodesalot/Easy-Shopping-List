package com.example.dontforgettograbthat.ActivityRequestItems;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.dontforgettograbthat.Interface.RecyclerViewInterface;
import com.example.dontforgettograbthat.Interface.RequestItemsActivityInterface;
import com.example.dontforgettograbthat.Login.LoginActivity;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.RecyclerViewItems;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestItemsActivity extends AppCompatActivity implements RecyclerViewInterface, RequestItemsActivityInterface {

    private static final String TAG = "AddItemActivity";
    private Context mContext = RequestItemsActivity.this;
    public DatabaseReference reference;

    //Constants
    private String REFRESH_CODE="REFRESH_CODE";

    //Widgets
    public RecyclerView recyclerView;

    public TextView total;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebase;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //vars
    private RecyclerViewItems adapter;
    private User user;

    private ArrayList<Item>items;

    public String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate: started");
        user=((UserClient)(getApplicationContext())).getUser();
        database = FirebaseDatabase.getInstance();

        firebaseDataExchangeListener();
        setupFirebaseAuth();
        referenceWidgets();
        //This method gets the
        firebaseRetrieve();

    }

    private void setUpTotal() {
        Log.d(TAG, "setUpTotal: started");
        double totes = 0;

        for (int i = 0; i< items.size(); i++){
            totes += items.get(i).getPrice();
            Log.d(TAG, "setUpTotal: iteration " + totes);
        }
        Log.d(TAG, "setUpTotal: setText");
        String s = String.format("%.2f",totes);
        total.setText("Total " + s);
    }

    private void firebaseRetrieve() {

        Log.d(TAG, "getFamilyNameReference: Rerencing " + user.toString() + "\n" + user.getParent_name());


        if (user_id!=null) {

            Log.d(TAG, "firebaseRetrieve: creatiung database and getting a reference");
            myRef = database.getReference();
            Query query = myRef.child("requests").child(user.getUsername());


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            if (singleSnapshot != null) {
                                items.add(singleSnapshot.getValue(Item.class));
                            }
                            for (int i = 0; i < items.size(); i++) {
                                Log.d(TAG, "onDataChange: " + items.get(i).toString());
                            }


                            setUpTotal();
                            adapter = new RecyclerViewItems(
                                    mContext,
                                    items);
                            Log.d(TAG, "onDataChange: setUp ReciclerView");
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query cancelled.");
                }
            });
        } else{
            Log.d(TAG, "firebaseRetrieve: error retreving familyName");
        }
    }
    private void referenceWidgets() {

        recyclerView = findViewById(R.id.list);

        total = findViewById(R.id.tvTotal);

    }



    //----------------------------Firebase Code-----------------------------------

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
        Bundle bundle = getIntent().getExtras();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: started");
        //TODO create a code so that this only recreates itself when recieving a custom code.
        // recreate();

    }

    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void OpenDialog(int position) {

    }

    @Override
    public void addToCartList(Item item) {

    }

    @Override
    public void deleteFromRequestedItems(String ItemKey) {

    }
}


