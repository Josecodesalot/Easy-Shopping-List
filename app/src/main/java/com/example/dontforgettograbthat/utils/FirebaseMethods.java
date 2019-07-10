package com.example.dontforgettograbthat.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.example.dontforgettograbthat.Login.LoginActivity;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    private Context mContext;


    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void deleteHistory(String itemKey) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("history").child(userID).child(itemKey);
        ref.removeValue();
    }

    public void deleteRequestedItem(String itemKey, String familyName) {
        if (userID != null) {
            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("requests").child(familyName).child(itemKey);
            dr.removeValue();
        } else {
            loginToast();
        }
    }

    private void loginToast() {
        Toast.makeText(mContext, "Please Signin to activate this feature", Toast.LENGTH_SHORT).show();
    }

    public void addItemToList(Item item) {
        Log.d(TAG, "addItemToList: ");

        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "addItemToList: get current UserCalled an was not null");

            String id = myRef.push().getKey();
            myRef.child("items")
                    .child(mAuth.getCurrentUser().getUid()).child(id)
                    .setValue(item);
        }
        else {
            Log.d(TAG, "addItemToList: ");
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();
        }
    }

    public void addItemToList(String mItemName, String mListName, long mItemQuantity, double mItemPrice) {
        Item item = new Item(mItemName,mListName,mItemQuantity,mItemPrice,"");

        if (mAuth.getCurrentUser() != null) {
            String id = myRef.push().getKey();
            item.setItemKey(id);
            myRef.child("items").child(userID).setValue(item);
        }else {
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();
        }
    }
    public void addItemToListForTheFirstTime (Item item){
        Log.d(TAG, "addItemToListForTheFirstTime: started");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items").child(userID);
        item.setItemKey(ref.push().getKey());
        Log.d(TAG, "addItemToListForTheFirstTime: item " + item.toString() );
        ref.child(item.getItemKey()).setValue(item);
    }

    public void sendRequest(String mItemName,String mListName, long mItemQuantity, Double mItemPrice,
                            String familyName) {

        Item item = new Item(mItemName, mListName, mItemQuantity, mItemPrice,"");
        Log.d(TAG, "sendRequest: started");

        if (mAuth.getCurrentUser() != null) {
            item.setItemKey(myRef.push().getKey());
            myRef.getRoot().child("requests").child(familyName).child(item.getItemKey()).setValue(item);

        } else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();
    }

    public void addItemToHistory(Item item) {
        if (mAuth.getCurrentUser() != null) {

            myRef.child("history")
                    .child(mAuth.getCurrentUser().getUid()).child(item.getItemKey())
                    .setValue(item);
        } else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();
    }

    public void restoreItem(Item item) {
        //Add to Cart,
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items").child(userID).child(item.getItemKey());
        ref.setValue(item);

        //Remove From History
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("history").child(userID).child(item.getItemKey());
        historyRef.removeValue();
    }

    //---------------------Authentication---------------//

    public void addNewUser(String email, String username, String parentName) {
        User user = new User(mAuth.getCurrentUser().getUid(), email, username, parentName);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user);
    }

    public void registerNewEmail(final String email, String password) {
        Log.d(TAG, "registerNewEmail: this happened");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());


                        if (task.getException() instanceof FirebaseAuthWeakPasswordException){
                            Toast.makeText(mContext, "Error, password too weak", Toast.LENGTH_SHORT).show();
                        }
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(mContext, "Error, invalid e-mail ", Toast.LENGTH_SHORT).show();
                        }
                        //method calls if email is already registered
                        if (task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(mContext, "Error, Email Already Exists", Toast.LENGTH_SHORT).show();
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {

                            //add userInfo
                            addNewUser(email, "","");
                            //send verificaton email
                            sendVerificationEmail();
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }
                    }
                });
    }


    public void sendVerificationEmail() {
        Log.d(TAG, "sendVerificationEmail: ");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                logoutAndClearStack();
                            } else {
                                Toast.makeText(mContext, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void logoutAndClearStack() {
        mAuth.signOut();
        Log.d(TAG, "onAuthStateChanged: navigating back to login screen.");
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(mContext.getString(R.string.extra_email_verification),
                mContext.getString(R.string.verification_email));
        mContext.startActivity(intent);
    }

    public void sendParentRequest(String parentsUsername, User user) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child("request").child(parentsUsername).child(user.getUser_id());
        ref.setValue(user);
    }


    public void addNewUser(User user){
        Log.d(TAG, "addNewUser: called");
        DatabaseReference ref = mFirebaseDatabase.getReference().child("users").child(user.getUser_id());
        ref.setValue(user);
    }



    public void acceptRequest (String currenUsername , User user){
        //The User object user, should be the child request user with the updated ParentName from the current user,
        //the following method will update the child users account
        addNewUser(user);
        //The following will delete the request
        DatabaseReference ref = mFirebaseDatabase.getReference().child("requests").child(currenUsername).child(user.getUser_id());
        ref.removeValue();
        //The following will Add User to FamilyList
        DatabaseReference thisref = mFirebaseDatabase.getReference().child("family").child(userID).child(user.getUser_id());
        thisref.setValue(user);
    }

    //TODO below 3 methods are still under construction
    public void sendUserToRequest (String currentUsername , User user){
        //update child users parent
        DatabaseReference ref = mFirebaseDatabase.getReference().child("users").child(user.getUser_id());
        user.setParent_name("");
        ref.setValue(user);
        //send user to requests
        sendParentRequest(currentUsername,user);
    }
    public void deleteRequest (String currentUsername , String userKey){
        DatabaseReference ref = mFirebaseDatabase.getReference().child("requests").child(currentUsername).child(userKey);
        ref.removeValue();
    }
    public void  deleteFamilyMember (String currentUsername , User user){
        DatabaseReference ref = mFirebaseDatabase.getReference().child("requests").child(currentUsername).child(user.getUser_id());
        ref.setValue(user);
    }
}
