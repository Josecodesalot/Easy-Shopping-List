package com.joseapps.simpleshoppinglist.ActivityFamilyList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.joseapps.simpleshoppinglist.Dialogs.FamilyListDialog;
import com.joseapps.simpleshoppinglist.Interface.RecyclerViewInterface;
import com.joseapps.simpleshoppinglist.Interface.FamilyListInterface;
import com.joseapps.simpleshoppinglist.Login.LoginActivity;
import com.joseapps.simpleshoppinglist.Models.Item;
import com.joseapps.simpleshoppinglist.Models.User;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.Const;
import com.joseapps.simpleshoppinglist.utils.FirebaseMethods;
import com.joseapps.simpleshoppinglist.Adapters.RecyclerViewItems;
import com.joseapps.simpleshoppinglist.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FamilyListActivity extends AppCompatActivity implements RecyclerViewInterface, FamilyListInterface {

    private static final String TAG = "FamilyListActivity";
    private final Context mContext = FamilyListActivity.this;
    public DatabaseReference reference;

    //Constants
    private String REFRESH_CODE="REFRESH_CODE";

    //Widgets
    private RecyclerView recyclerView;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebase;
    private FirebaseDatabase database;

    //vars
    private RecyclerViewItems adapter;
    private User user;

    private ArrayList<Item>items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);
        Log.d(TAG, "onCreate: started");
        user=((UserClient)(getApplicationContext())).getUser();
        database = FirebaseDatabase.getInstance();
        firebase = new FirebaseMethods(mContext);
        items=new ArrayList<>();

        firebaseDataExchangeListener();
        setupFirebaseAuth();
        referenceWidgets();
        firebaseRetrieve();

    }


    private void firebaseRetrieve() {

        Log.d(TAG, "getFamilyNameReference: Rerencing " + user.toString() + "\n" + user.getParent_name());


        if (user.getUser_id()!=null) {

            Log.d(TAG, "firebaseRetrieve: creatiung database and getting a reference");
            DatabaseReference ref = database.getReference().child(Const.FAMILY_ITEM).child(user.getUsername());


            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            if (singleSnapshot != null) {
                                Log.d(TAG, "onDataChange: datais not null sinlgesnapshot isnot null");
                                items.add(singleSnapshot.getValue(Item.class));
                            }
                            for (int i = 0; i < items.size(); i++) {
                                Log.d(TAG, "onDataChange: " + items.get(i).toString());
                            }
                        }
                    }
                    adapter = new RecyclerViewItems(
                            mContext,
                            items);
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
    }
    private void referenceWidgets() {

        recyclerView = findViewById(R.id.list);

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
    }

    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void addToCartList(Item item, int posiion) {
        Log.d(TAG, "addToCartList: calle");
        //This deletes the item from this family list, and adds it to the users personal List
        firebase.addItemToList(item);
        firebase.deleteFamilyListItem(item);
        items.remove(posiion);
        adapter.notifyItemRemoved(posiion);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void delete(int position) {
        Log.d(TAG, "delete: called");
        firebase.deleteFamilyListItem(items.get(position));
        items.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OpenDialog(Item item, int position) {
        FamilyListDialog dialog = FamilyListDialog.newInstance(items.get(position),position);
        dialog.show(getSupportFragmentManager(),"1");
    }
}


