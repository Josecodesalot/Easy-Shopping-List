package com.example.dontforgettograbthat.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.FormatFlagsConversionMismatchException;


public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private User user;

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




    public void deleteItem(String itemKey) {

        if (userID != null) {
            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("items").child(userID).child(itemKey);
            dr.removeValue();
        } else {
            loginToast();
        }
    }

    public void deleteHistory(String itemKey) {

        if (userID != null) {
            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("history").child(userID).child(itemKey);
            dr.removeValue();
        } else {
            loginToast();
        }
    }
    public void deleteRequest(String itemKey, String familyName) {


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

    public void addItemToList(String item_name, String list_name, long quantity, Double price) {

        if (mAuth.getCurrentUser() != null){
            String id = myRef.push().getKey();

            Item item = new Item(item_name, list_name, quantity, price, id);

            myRef.child("items")
                    .child(mAuth.getCurrentUser().getUid()).child(id)
                    .setValue(item);
        }else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();

    }

    public void sendRequest(String item_name, String list_name, long quantity, Double price,
                            String familyName, DataSnapshot shot) {
        Log.d(TAG, "sendRequest: started");

        if (mAuth.getCurrentUser() != null){
            String id = myRef.push().getKey();

            Item item = new Item(item_name, list_name, quantity, price, id);
            myRef.getRoot().child("requests").child(familyName).child(id).setValue(item);

        }else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();
    }

    public void addItemToHistory(String item_name, String list_name, long quantity, Double price, String id) {

        if (mAuth.getCurrentUser() != null){

            Item item = new Item(item_name, list_name, quantity, price, id);

            myRef.child("history")
                    .child(mAuth.getCurrentUser().getUid()).child(id)
                    .setValue(item);
        }else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();

    }

    public void restoreItem(String item_name, String list_name, long quantity, Double price, String id) {

        if (mAuth.getCurrentUser() != null){

            Item item = new Item(item_name, list_name, quantity, price, id);

            myRef.child("items")
                    .child(mAuth.getCurrentUser().getUid()).child(id)
                    .setValue(item);
        }else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();

    }

    public void registerNewEmail(final String email, String password, final String username, final String family_name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if (task.isSuccessful()){
                            //add userInfo
                            addNewUser(email,username,family_name);
                            //send verificaton email
                            sendVerificationEmail();

                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }


    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                logoutAndClearStack();

                            }else{
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

    public void addNewUser(String email, String username, String familyname){
        User user = new User(mAuth.getCurrentUser().getUid(), email, username, familyname);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user);

    }

    public User getUser(DataSnapshot dataSnapshot) {
        User user = new User(
                dataSnapshot.getValue(User.class).getUser_id(),
                dataSnapshot.getValue(User.class).getEmail(),
                dataSnapshot.getValue(User.class).getUsername(),
                dataSnapshot.getValue(User.class).getFamily_name()
        );
            return user;
    }
}
