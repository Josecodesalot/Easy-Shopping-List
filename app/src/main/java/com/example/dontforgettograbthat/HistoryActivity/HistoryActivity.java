package com.example.dontforgettograbthat.HistoryActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.dontforgettograbthat.Interface.DialogInterface;
import com.example.dontforgettograbthat.Login.LoginActivity;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.RecyclerViewAdapter;
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


public class HistoryActivity extends AppCompatActivity implements DialogInterface {

    private static final String TAG = "AddItemActivity";
    private Context mContext = HistoryActivity.this;
    private final int ACTIVITY_NUM = 1;

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

    private ArrayList<String> itemList;
    private ArrayList<String> itemNames;
    private ArrayList<Double> itemPrice;
    private ArrayList<Long> itemCount ;
    private ArrayList<String> id;
    private RecyclerViewAdapter adapter;
    private String familyName;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate: started");

        database = FirebaseDatabase.getInstance();
        user = new User();
        user = ((UserClient)(getApplicationContext())).getUser();
        familyName = user.getFamily_name();
        Log.d(TAG, "onCreate: family name setting = " + familyName);

        firebaseDataExchangeListener();
        setupFirebaseAuth();
        referenceWidgets();
        firebaseRetrieve();
        setUpTotal();

    }

    private void setUpTotal() {
        Log.d(TAG, "setUpTotal: started");
        double totes = 0;

        for (int i = 0; i< itemPrice.size(); i++){
            totes += itemPrice.get(i);
            Log.d(TAG, "setUpTotal: iteration " + totes);
        }
        Log.d(TAG, "setUpTotal: setText");
        String s = String.format("%.2f",totes);
        total.setText("Total " + s);
    }

    private void firebaseRetrieve() {
        Log.d(TAG, "firebaseRetrieve: creatiung database and getitnga reference");
        myRef = database.getReference();
        Query query = myRef.child("history").child(mAuth.getCurrentUser().getUid());
         itemList = new ArrayList<>();
         itemNames = new ArrayList<>();
         itemPrice = new ArrayList<>();
        itemCount = new ArrayList<>();
        id = new ArrayList<>();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                    if (singleSnapshot!=null) {
                        id.add(singleSnapshot.getValue(Item.class).getItemKey());
                        itemNames.add(singleSnapshot.getValue(Item.class).getItem_name());
                        itemList.add(singleSnapshot.getValue(Item.class).getList_name());
                        itemPrice.add(singleSnapshot.getValue(Item.class).getPrice());
                        itemCount.add(singleSnapshot.getValue(Item.class).getQuantity());

                        Log.d(TAG, "onDataChange: " + id.toString());
                    }
                    }
                setUpTotal();
                adapter = new RecyclerViewAdapter(
                        mContext,
                        itemNames,itemList, itemPrice, itemCount, id,familyName);
                Log.d(TAG, "onDataChange: setUp ReciclerView");
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
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
    public void delete(String delete) {
        if (delete.equals("delete")){
            Log.d(TAG, "delete: recieved" + delete);
            //adapter.notifyDataSetChanged();
            recreate();

        }
    }

    @Override
    public void total(String mTotal) {
        Log.d(TAG, "total: ");

    }

    @Override
    public void ItemName(String ItemName) {

    }

    @Override
    public void ItemCount(long ItemCont) {

    }

    @Override
    public void trigger(int trigger) {

    }
}


