package com.joseapps.simpleshoppinglist.ActivityCart;

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

import com.joseapps.simpleshoppinglist.ActivityProfile.ProfileInfoActivity;
import com.joseapps.simpleshoppinglist.Adapters.RecyclerviewItemTabs;
import com.joseapps.simpleshoppinglist.Dialogs.CartAddItemDialog;
import com.joseapps.simpleshoppinglist.Dialogs.CartListDialog;
import com.joseapps.simpleshoppinglist.Interface.CartInterface;
import com.joseapps.simpleshoppinglist.Interface.RecyclerViewInterface;
import com.joseapps.simpleshoppinglist.Interface.RvTopTabsInterface;
import com.joseapps.simpleshoppinglist.Login.LoginActivity;
import com.joseapps.simpleshoppinglist.Models.Item;
import com.joseapps.simpleshoppinglist.Models.User;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.Const;
import com.joseapps.simpleshoppinglist.utils.FirebaseMethods;
import com.joseapps.simpleshoppinglist.utils.ItemListHelper;
import com.joseapps.simpleshoppinglist.Adapters.RecyclerViewItems;
import com.joseapps.simpleshoppinglist.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity implements RecyclerViewInterface, CartInterface, RvTopTabsInterface {

    private static final String TAG = "CartActivity";
    private final Context mContext = CartActivity.this;

    //Widgets
    private RecyclerView recyclerView;
    private RecyclerView rvItemTabsView;
    private ImageView addItemIcon;
    private TextView total;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebase = new FirebaseMethods(mContext);
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    //vars

    private HashMap<String, ArrayList<Item>> itemsMapByListName;
    private ArrayList<Item> booleanList;
    //adapters
    private RecyclerViewItems adapter;
    private RecyclerviewItemTabs rvItemTabsAdapter;

    //Util
    private ItemListHelper itemListHelper;

    //model class
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Log.d(TAG, "onCreate: started");
        user = new User();
        setupFirebaseAuth();
        database = FirebaseDatabase.getInstance();
        firebase = new FirebaseMethods(mContext);

        if (mAuth.getCurrentUser()!=null) {

            //an listener which prints out the data being exchanged for debugging reasons
            firebaseDataExchangeListener();
            //sets up User Clinent Singleton. This allows the user to User Data from the current user from
            //any activity.
            setUpUserClient();
            //Widget are referenced and simple onClicks are set up
            referenceWidgets();
            //TODO white comment as to how below works
            setUpItemsFromFirebase();
        }
    }

    private void setUpUserClient(){
        DatabaseReference ref = database.getReference().child("users").child(mAuth.getCurrentUser().getUid());

        Log.d(TAG, "setUpUserClient: ref= " +ref.toString());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    user.setUser_id(mAuth.getCurrentUser().getUid());
                    setUser();
                }else {
                    Intent intent = new Intent(mContext, ProfileInfoActivity.class);
                    intent.putExtra("key","setUpUsername");
                    startActivity(intent);
                }
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

    private void setUpTotal(ArrayList<Item> items) {
        Log.d(TAG, "setUpTotal: started " + items.toString() );
        double totalPrice = 0.0;

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                Log.d(TAG, "setUpTotal: iteraton " + i);
                if (items.get(i).getPrice()!=null) {
                    double itemPrice = items.get(i).getPrice() * items.get(i).getQuantity();
                    totalPrice += itemPrice;
                    Log.d(TAG, "setUpTotal: iteration " + totalPrice);
                }
            }
            Log.d(TAG, "setUpTotal: setText");
            String s = String.format("%.2f", totalPrice);
            total.setText("Total " + s);
        }
    }

    private void setUpItemsFromFirebase() {
        final ArrayList<Item> items;
        Log.d(TAG, "setUpItemsFromFirebase: creatiung database and getitnga reference");
        Log.d(TAG, "setUpItemsFromFirebase: user = " + user.getUser_id());
        Query refrence = database.getReference().child(Const.CART_ITEM).child(mAuth.getCurrentUser().getUid()).orderByChild(Const.LISTNAME);
        Log.d(TAG, "setUpItemsFromFirebase: ref " + refrence.toString());

        items = new ArrayList<>();

        refrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Called");
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: singleSnapSHot = " +dataSnapshot.toString());
                        try {
                            items.add(singleSnapshot.getValue(Item.class));

                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: exeption " + e.getMessage());
                        }
                    }
                }
                if (items != null) {
                    setUpTotal(items);
                }

                Log.d(TAG, "onDataChange: items = " + items);

                itemListHelper = new ItemListHelper(items);
                itemsMapByListName = itemListHelper.getItemMapByListName();
                Log.d(TAG, "onDataChange: hash = " +itemsMapByListName.toString());
                setUpListItemRecyclerView();
                setUpTopTabRecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void setUpListItemRecyclerView(){
        adapter = new RecyclerViewItems(
                mContext,
                new ArrayList<Item>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void setUpTopTabRecyclerView(){
        rvItemTabsAdapter = new RecyclerviewItemTabs(mContext, itemListHelper.getListNames());
        rvItemTabsView.setAdapter(rvItemTabsAdapter);

    }
    private void referenceWidgets() {

        rvItemTabsView = findViewById(R.id.rvTabs);
        recyclerView = findViewById(R.id.list);
        total = findViewById(R.id.tvTotal);
        addItemIcon = findViewById(R.id.addImg);


        addItemIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked AddItemIcon");
                FragmentManager fragmentManager = getSupportFragmentManager();
                CartAddItemDialog userPopUp = new CartAddItemDialog();
                userPopUp.show(fragmentManager,"!");

            }
        });

    }

    //------------------------Interface Mthods-----------------------------//


    @Override
    public void OpenDialog(Item item, int position) {

        CartListDialog dialog = CartListDialog.newInstance(item, position);
        dialog.show(getSupportFragmentManager(),"tag");

    }
    @Override
    public void addToHistory(Item item, int position, String originalListName) {

        Log.d(TAG, "addToHistory: " + item.toString());
        firebase.sendItemToHistory(item);

        itemsMapByListName.get(originalListName).remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();

        resetTopTabRecyclerView();

    }
    @Override
    public void delete(Item item, int position, String originalListName) {
        firebase.deleteItemCart(itemsMapByListName.get(originalListName).get(position));
        adapter.notifyDataSetChanged();
        itemsMapByListName.get(originalListName).remove(position);
        resetTopTabRecyclerView();
        Log.d(TAG, "delete: adapter size = " +adapter.getItemCount());

    }
    @Override
    public void addItem(final Item item, int position, String originalListName) {
        Log.d(TAG, "addItem: called with item = " + item.toString());
        //this will get called if the item is added from the add new item button[+]

        if (position==Const.ADDITEM){
            addToLocalMap(item);
            firebase.addItemToList(item);
            adapter.notifyDataSetChanged();
            resetTopTabRecyclerView();

        }else{
            itemsMapByListName.get(originalListName).set(position,item);
            firebase.addItemToList(item);
            adapter.notifyItemChanged(position,item);
            adapter.notifyDataSetChanged();
            resetTopTabRecyclerView();
        }

    }

    @Override
    public void setChanges(Item item, int position, String originalListName) {
        Log.d(TAG, "setChanges: item " + item.toString() + " position  = " +position);
        firebase.addItemToList(item);
        setChangesToLocalMap(item, position, originalListName);
        adapter.notifyItemChanged(position,item);
        adapter.notifyDataSetChanged();
        resetTopTabRecyclerView();

    }

    public void addToLocalMap(Item item){
        if (itemsMapByListName.get(item.getList_name())!=null){
            itemsMapByListName.get(item.getList_name()).add(item);
        }else{
            ArrayList <Item> ar = new ArrayList<>();
            ar.add(item);
            itemsMapByListName.put(item.getList_name(),ar);
        }
    }

    public void setChangesToLocalMap(Item item, int position, String originalItemListName){
        itemsMapByListName.get(originalItemListName).remove(position);

        if (itemsMapByListName.get(item.getList_name())!=null){
            itemsMapByListName.get(item.getList_name()).add(item);
        }else{
            ArrayList <Item> ar = new ArrayList<>();
            ar.add(item);
            itemsMapByListName.put(item.getList_name(),ar);
        }
    }

    public void resetTopTabRecyclerView(){
        Log.d(TAG, "resetTopTabRecyclerView: ");
        rvItemTabsAdapter =  new RecyclerviewItemTabs(mContext, new ArrayList<>(itemsMapByListName.keySet()));
        rvItemTabsView.setAdapter(rvItemTabsAdapter);
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
        Log.d(TAG, "onStart: started authStateListnerCreated");


        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
        //setUpItemsFromFirebase();

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

    @Override
    public void setRvData(String listName) {
        adapter = new RecyclerViewItems(mContext, itemsMapByListName.get(listName));
        recyclerView.setAdapter(adapter);

    }
}


