package com.example.dontforgettograbthat.AddItemActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.Toast;
import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.Dialogs.WebViewDialogueFragment;
import com.example.dontforgettograbthat.Interface.IAddItem;
import com.example.dontforgettograbthat.Login.LoginActivity;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.Const;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.SectionsPagerAdapter;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity implements IAddItem {

    private static final String TAG = "AddItemActivity";
    private Context mContext = AddItemActivity.this;

    //Widgets
    WebViewDialogueFragment webDialog;

    //vars
    private ArrayList<String> url;
    private Bundle webBundle;
    private Item item;
    private User user;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseMethods firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Log.d(TAG, "onCreate: started");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        setupFirebaseAuth();
        firebaseDataExchangeListener();
        firebase = new FirebaseMethods(mContext);

        webDialog = new WebViewDialogueFragment();
        webBundle = new Bundle();

        user = new User();
        user = ((UserClient)(getApplicationContext())).getUser();
        Log.d(TAG, "onCreate: user = " + user.toString());
        item = new Item();

        setUrlArray();
        setUpViewPager();
    }

    @Override
    public void setItemName(String itemName) {
        item.setItem_name(itemName);
        Log.d(TAG, "setItemName: Recieved  " + itemName);
        if (item.getItem_name() != null) {
            Log.d(TAG, "setItemName: attempting to use sendData interface to send fragment " + item.getItem_name());
            webBundle.putString("key", itemName);
            if (!itemName.equals("")) {
                loadWebViewCache();
            }
        }
    }
    //              Interface           //
    @Override
    public void setQuantity(int quantity) {
        item.setQuantity(quantity);

        Log.d(TAG, "setQuantity: recieved  " +quantity);
    }

    @Override
    public void setlistName(String listName) {
        item.setList_name(listName);

        Log.d(TAG, "setlistName: recived  " + listName);
    }

    @Override
    public void setPrice(double itemPrice) {
        item.setPrice(itemPrice);

        Log.d(TAG, "setPrice: recived  " + itemPrice);
    }

    @Override
    public void openBrowser(int browser) {
        if (browser == Const.WALMART){
            webBundle.putString("key", item.getItem_name());
            webBundle.putString("url", url.get(Const.WALMART));
        }
        if (browser == Const.COSTCO){
            webBundle.putString("key", item.getItem_name());
            webBundle.putString("url", url.get(Const.COSTCO));
        }
        if (browser == Const.ZHERS){
            webBundle.putString("key", item.getItem_name());
            webBundle.putString("url", url.get(Const.ZHERS));
        }
        if (browser == Const.METRO){
            webBundle.putString("key", item.getItem_name());
            webBundle.putString("url", url.get(Const.METRO));
        }
        webDialog.setArguments(webBundle);
        webDialog.show(getSupportFragmentManager(), "1");
    }

    @Override
    public void addItemToList() {
        firebase.addItemToList(item);
        Intent intent = new Intent(mContext, CartActivity.class);
        intent.putExtra("REFRESH_CODE", "REFRESH_CODE");
        startActivity(intent);
    }

    @Override
    public void addItemToFamilyList() {
        if (user.getParent_name().equals("")){
            Toast.makeText(mContext, "No Parent found, please click the user icon, then profile settings, then send your parent a request, in order to user this feature your parent has to accept this request", Toast.LENGTH_SHORT).show();
        }else{

        }
    }

    private void setUrlArray(){
        url= new ArrayList<>();

        url.add("https://www.walmart.ca/search/");
        url.add("https://www.costco.ca/CatalogSearch?dept=All&keyword=");
        url.add("https://www.zehrs.ca/search/?search-bar=");
        url.add("https://www.metro.ca/en/search?filter=");
        url.add("https://www.google.com/search?q=");
    }

    private void loadWebViewCache() {
        Log.d(TAG, "loadWebViewCache: LOADING LOADING LOADING");
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




