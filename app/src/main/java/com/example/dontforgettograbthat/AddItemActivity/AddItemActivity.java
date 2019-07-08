package com.example.dontforgettograbthat.AddItemActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.Interface.IAddItem;
import com.example.dontforgettograbthat.Login.LoginActivity;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.SectionsPagerAdapter;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;

public class AddItemActivity extends AppCompatActivity implements IAddItem {

    private static final String TAG = "AddItemActivity";
    private Context mContext = AddItemActivity.this;

    //constants
    private final int ACTIVITY_NUM = 2;

    //vars
    SelectedBundle selectedBundle;
    private String mItemName, mListName;
    private int mItemQuantity;
    private Double mItemPrice;
    private String mEmail;
    private String familyName;
    private User user;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseMethods firebase;

    public static DecimalFormat d2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Log.d(TAG, "onCreate: started");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        setupFirebaseAuth();
        firebaseDataExchangeListener();
        firebase = new FirebaseMethods(mContext);

        user =  new User();
        user = ((UserClient)(getApplicationContext())).getUser();
        familyName = user.getParent_name();

        mItemPrice = 0.0;
        mItemName = " ";
        mItemQuantity = 1;
        mListName = "Default List";

        setUpViewPager();
        mItemName = " ";
        mEmail = " ";

    }

    public interface SelectedBundle {
        void onBundleSelect(Bundle bundle);
    }

    public void setOnBundleSelected(SelectedBundle selectedBundle) {
        this.selectedBundle = selectedBundle;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent  data) {
        Bundle bundle = new Bundle();
        selectedBundle.onBundleSelect(bundle);
    }


    @Override
    public void setItemName(String itemName) {
        mItemName = itemName;
        Log.d(TAG, "setItemName: Recieved  " + itemName);
        if (mItemName != null) {
            Log.d(TAG, "setItemName: attempting to use sendData interface to send fragment " + mItemName);
            Bundle bundle = new Bundle();
            bundle.putString("key", itemName);
            selectedBundle.onBundleSelect(bundle);
        }
    }

    //              Interface           //

    @Override
    public void setQuantity(int quantity) {
        mItemQuantity = quantity;
        Log.d(TAG, "setQuantity: recieved  " +quantity);
    }

    @Override
    public void setlistName(String listName) {
        mListName= listName;
        Log.d(TAG, "setlistName: recived  " + listName);
    }

    @Override
    public void setPrice(double itemPrice) {
        mItemPrice=itemPrice;
        Log.d(TAG, "setPrice: recived  " + itemPrice);
    }

    @Override
    public void triggers(int trigger) {
        if (trigger == 1) {
            Log.d(TAG, "triggers: started");

            if (!mItemName.equals("") || mItemQuantity != 0) {
                Log.d(TAG, "triggers: everything should be in order attemoting to load data into firebase");
                String i = d2.format(mItemPrice);
                mItemPrice = Double.parseDouble(i);

                firebase.addItemToList(mItemName, mListName, mItemQuantity, mItemPrice);
                Intent intent = new Intent(mContext, CartActivity.class);
                intent.putExtra("REFRESH_CODE", "REFRESH_CODE");
                startActivity(intent);
            } else {
                Toast.makeText(mContext, "There must be a name and a quantity, please swipe back and fill those areas", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "triggers: item quanitity is 0 or name is empty toast executed");
                loadWebViewCache();
            }

        }
        if (trigger == 2) {
            Log.d(TAG, "triggers: 2 sending request");
            firebase.sendRequest(mItemName, mListName, mItemQuantity, mItemPrice, user.getParent_name());
        }
    }

    private void loadWebViewCache() {
        String u = "http://";

    }


    @Override
    public void setEmail(String email) {
        Log.d(TAG, "setEmail: started");
        mEmail = email;
    }


    /*
                Sets Up the View Pager In the activity;
     */
    
    private void setUpViewPager() {
        Log.d(TAG, "setUpViewPager: attempting to setup ViewPager");
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddNameAndQuantityFragment());
        adapter.addFragment(new SearchAndChooseProviderFragment());
        adapter.addFragment(new AddToListOrRequestFragment());
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_add);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_price);
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
        Log.d(TAG, "onStart: started");
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
            Log.d(TAG, "checkCurrentUser: user is null");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }



}




