package com.example.dontforgettograbthat.CartActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dontforgettograbthat.Dialogs.AddItemDialog;
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

public class CartActivity extends AppCompatActivity implements DialogInterface {

    private static final String TAG = "CartActivity";
    private Context mContext = CartActivity.this;


    //Constants

    private String REFRESH_CODE="REFRESH_CODE";

    //Widgets
    public RecyclerView recyclerView;
    public ImageView addItemIcon;
    public TextView total;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebase;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //vars
    private ArrayList<String> itemLists;
    private ArrayList<String> itemNames;
    private ArrayList<Double> itemPirces;
    private ArrayList<Long> itemCounts;
    private ArrayList<String> ids;
    private RecyclerViewAdapter adapter;
    public User user;

    //ItemVrars
    private String itemName;
    private Long itemCountLong;

    public String familyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Log.d(TAG, "onCreate: started");

        setupFirebaseAuth();
        database = FirebaseDatabase.getInstance();
        firebase = new FirebaseMethods(mContext);

        if (mAuth.getCurrentUser()!=null) {
            //an listener which prints out the data being exchanged for debugging reasons
            firebaseDataExchangeListener();
            //Widget are referenced and simple onClicks are set up
            referenceWidgets();
            //This method sets up the recyclerView which displays Items in the Cart and their onClickListeners
            //It does this by retrieving the data form Firebase realtimeDatabase
            fromFirebaseToRecyclerView();
            //total count of all items in the recyclerView
            setUpTotal();
            //sets up User Clinent Singleton. This allows the user to User Data from the current user from
            //any activity.
            setUpUserClient();
        }
    }

    private void setUpUserClient(){
        DatabaseReference ref = database.getReference().child("users").child(mAuth.getCurrentUser().getUid());

        Log.d(TAG, "setUpUserClient: ref= " +ref.toString());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = new User (
                dataSnapshot.getValue(User.class).getUser_id(),
                        dataSnapshot.getValue(User.class).getEmail(),
                        dataSnapshot.getValue(User.class).getUsername(),
                        dataSnapshot.getValue(User.class).getFamily_name()
               );

                setUser();
               // Log.d(TAG, "onDataChange: " + ((UserClient)(mContext)).getUser().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void setUser() {
        ((UserClient)(getApplicationContext())).setUser(user);
        Log.d(TAG, "onDataChange: " + user.toString());

    }

    private void setUpTotal() {
        Log.d(TAG, "setUpTotal: started");
        double totes = 0;

        for (int i = 0; i< itemPirces.size(); i++){
            totes += itemPirces.get(i);
            Log.d(TAG, "setUpTotal: iteration " + totes);
        }
        Log.d(TAG, "setUpTotal: setText");
        String s = String.format("%.2f",totes);
        total.setText("Total " + s);
    }

    private void fromFirebaseToRecyclerView() {
        Log.d(TAG, "fromFirebaseToRecyclerView: creatiung database and getitnga reference");
        myRef = database.getReference();
        Query query = myRef.child("items").child(mAuth.getCurrentUser().getUid());
         itemLists = new ArrayList<>();
         itemNames = new ArrayList<>();
         itemPirces = new ArrayList<>();
        itemCounts = new ArrayList<>();
        ids = new ArrayList<>();




        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                    if (singleSnapshot!=null) {
                        ids.add(singleSnapshot.getValue(Item.class).getItemKey());
                        itemNames.add(singleSnapshot.getValue(Item.class).getItem_name());
                        itemLists.add(singleSnapshot.getValue(Item.class).getList_name());
                        itemPirces.add(singleSnapshot.getValue(Item.class).getPrice());
                        itemCounts.add(singleSnapshot.getValue(Item.class).getQuantity());

                        Log.d(TAG, "onDataChange: " + ids.toString());
                    }
                    }
                setUpTotal();


                adapter = new RecyclerViewAdapter(
                        mContext,
                        itemNames,itemLists, itemPirces, itemCounts, ids, familyName);
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
        addItemIcon = findViewById(R.id.addImg);

        addItemIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked AddItemIcon");
                FragmentManager fragmentManager = getSupportFragmentManager();
                AddItemDialog userPopUp = new AddItemDialog();
                userPopUp.show(fragmentManager,"!");

            }
        });

    }

    //------------------------Interface Mthods-----------------------------//


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
        Log.d(TAG, "ItemName: set out " + ItemName);
        itemName = ItemName;
    }

    @Override
    public void ItemCount(long ItemCont) {
        Log.d(TAG, "ItemCount: recieved " + ItemCont);
        itemCountLong = ItemCont;
    }

    @Override
    public void trigger(int trigger) {
        if (trigger ==1){
            Log.d(TAG, "trigger: ItemName = " + itemName);
            Log.d(TAG, "trigger ItemCount = " + itemCountLong);

            firebase.addItemToList(itemName,"defaultList",itemCountLong, 0.00);
            recreate();
        }
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


    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Log.d(TAG, "checkCurrentUser: user is null, starting loginActivity");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }
}

