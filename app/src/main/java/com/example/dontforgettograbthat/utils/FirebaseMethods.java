package com.example.dontforgettograbthat.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private final FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final FirebaseDatabase mFirebaseDatabase;
    private final DatabaseReference myRef;
    private String userID;

    private final Context mContext;


    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void setFamilyitem(Item item, User user){
        if (item.getItemKey()==null||item.getItemKey().isEmpty()){
            item.setItemKey(myRef.push().getKey());
        }
        myRef.child(Const.FAMILY_ITEM).child(user.getParent_name()).child(item.getItemKey()).setValue(item);
    }

    public void deleteItemCart(Item item){
        myRef.child(Const.CART_ITEM).child(userID).child(item.getItemKey()).removeValue();
    }

    public void deleteFamilyListItem(Item item){
        myRef.child(Const.FAMILY_ITEM).child(userID).child(item.getItemKey()).removeValue();
    }


    public void deleteHistory(String itemKey) {
        myRef.child(Const.HISTORY_FIELD).child(userID).child(itemKey).removeValue();
    }

    public void addItemToList(Item item) {
        Log.d(TAG, "addItemToList: " + item.toString());

        if (item.getItemKey().isEmpty()){
            Log.d(TAG, "addItemToList: ");
            String s = mFirebaseDatabase.getReference().push().getKey();
            item.setItemKey(s);
        }

        DatabaseReference ref = mFirebaseDatabase.getReference().child(Const.CART_ITEM).child(userID).child(item.getItemKey());
        Log.d(TAG, "addItemToList: ref = " + ref.toString());
        ref.setValue(item);
    }

    public void additemToHistory(Item item){
        myRef.child(Const.HISTORY_FIELD).child(userID).child(item.getItemKey()).setValue(item);
    }

    public void sendItemToHistory(Item item) {
        //This method moves item from Cart Activity, into History Activity (List)

            //Add Item TO history
            myRef.child(Const.HISTORY_FIELD)
                    .child(userID).child(item.getItemKey())
                    .setValue(item);
            //Move away from Cart
            myRef.child(Const.CART_ITEM).child(userID).child(item.getItemKey()).removeValue();
    }

    public void restoreFromHistoryToCart(Item item) {
        //Add to Cart,
        DatabaseReference ref = myRef.child(Const.CART_ITEM).child(userID).child(item.getItemKey());
        ref.setValue(item);

        //Remove From History
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child(Const.HISTORY_FIELD).child(userID).child(item.getItemKey());
        historyRef.removeValue();
    }

    //---------------------Authentication---------------//

    private void addNewUser(String email, String username, String parentName) {
        User user = new User(mAuth.getCurrentUser().getUid(), email, username, parentName);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user);
    }

    public void registerNewEmail(final String email, String password, final ProgressBar progressBar) {
        Log.d(TAG, "registerNewEmail: this happened");
        final int number;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.getException() instanceof FirebaseAuthWeakPasswordException){
                            Toast.makeText(mContext, "Error, password too weak", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(mContext, "Error, invalid e-mail ", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        //method calls if email is already registered
                        if (task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(mContext, "Error, Email Already Exists", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
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


    private void sendVerificationEmail() {
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

    // CHild Managerment
    public void addNewUser(User user){
        Log.d(TAG, "addNewUser: called");
        myRef.child(Const.USERS_FIELD).child(user.getUser_id()).setValue(user);
    }

    public void sendParentRequest(String parentsUsername, User currentUser) {
        myRef.child(Const.FAMILY_USER_REQUEST).child(parentsUsername).child(currentUser.getUser_id()).setValue(currentUser);
        // Request -> Username -> UID -> User Object //
    }

    /*
        acceptRequest
        PARAMS currentUsername, is the username of the current user, User user, is he username of the child which sent the request
     */
    public void acceptRequest (String currenUsername , User user){
        Log.d(TAG, "acceptRequest: user " + user.toString());
        //The User object user, should be the child request user with the updated ParentName from the current user,
        //the following method will update the child users account
        addNewUser(user);
        //The following will delete the request
        myRef.child(Const.FAMILY_USER_REQUEST).child(currenUsername).child(user.getUser_id()).removeValue();
        //The following will Add User to FamilyListActivity
        // family_user -> (Current User) UID -> ChildUserId --setvalue--> user
        myRef.child(Const.FAMILY_USER).child(userID).child(user.getUser_id()).setValue(user);
    }

    public void sendUserToRequest (User currentUser , User user){
        Log.d(TAG, "sendUserToRequest: called with user " + user.toString());
        //removes child user from family
        myRef.child(Const.FAMILY_USER).child(currentUser.getUser_id()).child(user.getUser_id()).removeValue();
        //updates child users family name
        user.setParent_name("");
        addNewUser(user);
        //send user to requests
        sendParentRequest(currentUser.getUsername(),user);
    }
    public void deleteRequest (String currentUsername , String userKey){
        myRef.child(Const.FAMILY_USER_REQUEST).child(currentUsername).child(userKey).removeValue();
    }
    public void  deleteFamilyMember (User currentUser , User user){
        // deletes child user from current users family node
        myRef.child(Const.FAMILY_USER).child(currentUser.getUser_id()).child(user.getUser_id()).removeValue();
        // updates child users parent setting
        user.setParent_name("");
        addNewUser(user);
    }
}
