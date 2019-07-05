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

    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot) {
        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists.");

        User user = new User();

        for (DataSnapshot ds : datasnapshot.getChildren()) {
            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);

            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkIfUsernameExists: username: " + user.getUsername());

            if (user.getUsername().equals(username)) {
                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + user.getUsername());
                return true;
            }
        }
        return false;
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

        if (mAuth.getCurrentUser() != null) {
            String id = myRef.push().getKey();

            Item item = new Item(item_name, list_name, quantity, price, id);

            myRef.child("items")
                    .child(mAuth.getCurrentUser().getUid()).child(id)
                    .setValue(item);
        } else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();

    }

    public void sendRequest(String item_name, String list_name, long quantity, Double price,
                            String familyName, DataSnapshot shot) {
        Log.d(TAG, "sendRequest: started");

        if (mAuth.getCurrentUser() != null) {
            String id = myRef.push().getKey();

            Item item = new Item(item_name, list_name, quantity, price, id);
            myRef.getRoot().child("requests").child(familyName).child(id).setValue(item);

        } else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();
    }

    public void addItemToHistory(String item_name, String list_name, long quantity, Double price, String id) {

        if (mAuth.getCurrentUser() != null) {

            Item item = new Item(item_name, list_name, quantity, price, id);

            myRef.child("history")
                    .child(mAuth.getCurrentUser().getUid()).child(id)
                    .setValue(item);
        } else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();

    }

    public void restoreItem(String item_name, String list_name, long quantity, Double price, String id) {

        if (mAuth.getCurrentUser() != null) {

            Item item = new Item(item_name, list_name, quantity, price, id);

            myRef.child("items")
                    .child(mAuth.getCurrentUser().getUid()).child(id)
                    .setValue(item);
        } else
            Toast.makeText(mContext, "Error, Login, SignUp, or Send this to your won account through the Email Feature", Toast.LENGTH_LONG).show();

    }

    public void registerNewEmail(final String email, String password, final String username, final String family_name) {
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

                        } else if (task.isSuccessful()) {
                            //add userInfo
                            addNewUser(email, username, family_name);
                            //send verificaton email
                            sendVerificationEmail();

                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }


    public void sendVerificationEmail() {
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

    public void addNewUser(String email, String username, String familyname) {
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

    public void sendParentRequest(String familyName, String user_id, User user) {
        DatabaseReference ref = mFirebaseDatabase.getReference().child("request").child(familyName).child(user_id);
        ref.setValue(user);
    }

    public boolean familyNameExists(User user, String familyname, DataSnapshot dataSnapshot) {
        Log.d(TAG, "check if Family Name Exists: checking if " + familyname + " already exists.");

        if (user.getFamily_name().equals(familyname)) {
            Toast.makeText(mContext, "That is your family name, To add an Item to your List user other button, If you want to add this item to your providers list, ask for their family name ", Toast.LENGTH_LONG).show();
            return false;
        } else {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);

                if (familyname.equals(ds.getValue(User.class).getFamily_name())) {
                    Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + familyname);
                    return true;
                }
            }
            Log.d(TAG, "familyNameExists: familyName " + familyname + " Doesnt exist");
            Toast.makeText(mContext, "Family doesnt exist", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
