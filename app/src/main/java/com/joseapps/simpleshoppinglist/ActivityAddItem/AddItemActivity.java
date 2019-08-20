package com.joseapps.simpleshoppinglist.ActivityAddItem;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.joseapps.simpleshoppinglist.Interface.AddItemInterface;
import com.joseapps.simpleshoppinglist.Login.LoginActivity;
import com.joseapps.simpleshoppinglist.Models.Item;
import com.joseapps.simpleshoppinglist.Models.User;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.Const;
import com.joseapps.simpleshoppinglist.utils.FirebaseMethods;
import com.joseapps.simpleshoppinglist.Adapters.SectionsPagerAdapter;
import com.joseapps.simpleshoppinglist.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity implements AddItemInterface {

    private static final String TAG = "AddItemActivity";
    private final Context mContext = AddItemActivity.this;

    //Widgets
    private ViewPager viewPager;
    private SectionsPagerAdapter adapter;
    //vars
    private ArrayList<String> url;
    private Bundle webBundle;
    private Item item;
    private User user;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = mFirebaseDatabase.getReference();
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

        webBundle = new Bundle();
        user = new User();
        user = ((UserClient)(getApplicationContext())).getUser();
        item = new Item("","default_list",1,0.0,"");

        setUrlArray();
        setUpViewPager();
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            if (bundle.get(Const.WEBVIEWCODE)!=null){
                Log.d(TAG, "onCreate: args Code = " + bundle.getString(Const.WEBVIEWCODE));
                Log.d(TAG, "onCreate: itemname = " + bundle.getString(Const.ITEMNAME));
                Log.d(TAG, "onCreate: new price = " + bundle.getString(Const.sMetro));
                adapter.getItem(0).setArguments(bundle);
                adapter.getItem(1).setArguments(bundle);
                adapter.getItem(0).onResume();
                adapter.getItem(1).onResume();
                viewPager.setCurrentItem(1);
            }
        }
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
    public void openBrowser(int browser, String warlmart, String costco, String zhers, String metro) {
        webBundle.putString(Const.ITEMNAME, item.getItem_name());

        if (browser == Const.WALMART){
            webBundle.putString(Const.URL, url.get(Const.WALMART));
            webBundle.putString(Const.WHICHLIST,Const.sWalmart);
        }
        if (browser == Const.COSTCO){
            webBundle.putString(Const.URL, url.get(Const.COSTCO));
            webBundle.putString(Const.WHICHLIST,Const.sCostco);
        }
        if (browser == Const.ZHERS){
            webBundle.putString(Const.URL, url.get(Const.ZHERS));
            webBundle.putString(Const.WHICHLIST,Const.sZhers);
        }
        if (browser == Const.METRO){
            webBundle.putString(Const.URL, url.get(Const.METRO));
            webBundle.putString(Const.WHICHLIST,Const.sMetro);
        }

        webBundle.putString(Const.sWalmart,warlmart);
        webBundle.putString(Const.sCostco,costco);
        webBundle.putString(Const.sZhers,zhers);
        webBundle.putString(Const.sMetro,metro);


        Intent intent = new Intent (mContext, WebViewSearchActivity.class);
        intent.putExtras(webBundle);
        startActivity(intent);
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

        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FIrstItemNameFragment());
        adapter.addFragment(new SecondBrowserPriceAndListName());
        adapter.addFragment(new ThirdAddItemToListOrFamilyList());
        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_add);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_price);
    }

    @Override
    public void addItemToList() {
        Log.d(TAG, "addItemToList: called");
        item.setItemKey(myRef.push().getKey());
        firebase.addItemToList(item);
        reset();
    }

    @Override
    public void addItemToFamilyList() {
        Log.d(TAG, "addItemToFamilyList: called");
        if (user.getParent_name().equals("")){
            Toast.makeText(mContext, "No Parent found, please click the user icon, then profile settings, then send your parent a request, in order to user this feature your parent has to accept this request", Toast.LENGTH_SHORT).show();
        }else{
            firebase.setFamilyitem(item,user);
           reset();
        }
    }

    @Override
    public void setPage(int i) {
        viewPager.setCurrentItem(i);
    }

    //------------ End Of Interface ----------------/

    private void reset(){
        Bundle bundle = new Bundle();
        bundle.putString(Const.REFRESH,Const.REFRESH);
        adapter.getItem(0).setArguments(bundle);
        adapter.getItem(1).setArguments(bundle);
        adapter.getItem(0).onResume();
        adapter.getItem(1).onResume();
        viewPager.setCurrentItem(0);
        Toast.makeText(mContext, "Item Added", Toast.LENGTH_SHORT).show();
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



    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Log.d(TAG, "checkCurrentUser: user is null");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }


}




