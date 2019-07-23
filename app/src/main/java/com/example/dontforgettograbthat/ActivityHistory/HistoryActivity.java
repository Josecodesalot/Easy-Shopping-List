package com.example.dontforgettograbthat.ActivityHistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.dontforgettograbthat.Dialogs.HistoryDialog;
import com.example.dontforgettograbthat.Interface.HistoryInterface;
import com.example.dontforgettograbthat.Interface.RecyclerViewInterface;
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


public class HistoryActivity extends AppCompatActivity implements RecyclerViewInterface, HistoryInterface {

    private static final String TAG = "AddItemActivity";
    private final Context mContext = HistoryActivity.this;

    //Constants

    //Widgets
    private RecyclerView recyclerView;

    private TextView total;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebase;

    private FirebaseDatabase database;

    //vars
    private ArrayList<Item> items;
    private RecyclerViewItems adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate: started");

        firebase = new FirebaseMethods(mContext);
        items = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        user = new User();
        user = ((UserClient)(getApplicationContext())).getUser();
        String familyName = user.getParent_name();
        Log.d(TAG, "onCreate: family name setting = " + familyName);

        firebaseDataExchangeListener();
        setupFirebaseAuth();
        referenceWidgets();
        firebaseRetrieve();
        setUpTotal();

    }

    private void setUpTotal() {
        Log.d(TAG, "setUpTotal: started");
        double totalPrice = 0;

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                totalPrice += items.get(i).getPrice();
                Log.d(TAG, "setUpTotal: iteration " + totalPrice);
            }
            Log.d(TAG, "setUpTotal: setText");
            String s = String.format("%.2f", totalPrice);
            total.setText("Total " + s);
        }
    }
    private void firebaseRetrieve() {
        Log.d(TAG, "firebaseRetrieve: creatiung database and getitnga reference");
        DatabaseReference myRef = database.getReference();
        Query query = myRef.child("history").child(user.getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    if (singleSnapshot!=null) {
                        items.add(singleSnapshot.getValue(Item.class));
                    }
                }
                setUpTotal();

                adapter = new RecyclerViewItems(mContext, items);
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
    }

    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    //--------- Override Methods From Interface ---------//

    @Override
    public void OpenDialog(int position) {
        HistoryDialog dialog = HistoryDialog.newInstance(items.get(position), position);
        dialog.show(getSupportFragmentManager(),"history");
    }

    @Override
    public void restoreToCart(Item item, int position) {
        firebase.restoreFromHistoryToCart(item);
        items.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteFromHistory(Item item, int position) {
        firebase.deleteHistory(item.getItemKey());
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setChanges(Item item, int position) {
        firebase.additemToHistory(item);
        items.set(position,item);
        adapter.notifyItemChanged(position,item);
        adapter.notifyDataSetChanged();
    }
}


