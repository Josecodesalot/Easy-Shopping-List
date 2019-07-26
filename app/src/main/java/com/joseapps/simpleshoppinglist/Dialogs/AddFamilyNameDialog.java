package com.joseapps.simpleshoppinglist.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.joseapps.simpleshoppinglist.Models.User;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.Const;
import com.joseapps.simpleshoppinglist.utils.FirebaseMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddFamilyNameDialog extends DialogFragment {

    private EditText etParentname;
    private String sParentName;
    private Button submit;
    private boolean allowFamilyName;
    private FirebaseMethods firebase;
    private final DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "AddFamilyNameDialog";
    private Context mContext;


    private User user;

    public static  AddFamilyNameDialog newInstance(User user, Context mContext) {
        AddFamilyNameDialog frag = new AddFamilyNameDialog();
        frag.setUser(user, mContext);
        return frag;
    }

    private void setUser(User user, Context mContext){
        this.user = user;
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_parentname, container, false);

        firebase = new FirebaseMethods(getActivity());
        setUpWidgets(view);

        if (user!=null){

            etParentname.setText(user.getParent_name());

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sParentName = etParentname.getText().toString().toLowerCase();
                    Query qry = myRef.child(Const.USERS_FIELD).orderByChild(Const.USERNAME_FIELD).equalTo(sParentName);
                    Log.d(TAG, "onClick: queerry = " + qry.toString());
                    boolean pass = false;


                    if (sParentName.equals("")){
                        Toast.makeText(mContext, "Please Type in Parent's Username", Toast.LENGTH_SHORT).show();
                    }else{
                        if (sParentName.equals(user.getUsername())){
                            Toast.makeText(mContext, "This is your Username", Toast.LENGTH_SHORT).show();
                        }else {
                            if (sParentName.equals(user.getParent_name())){
                                Toast.makeText(mContext, "This is already your parent", Toast.LENGTH_SHORT).show();
                            }else{
                                qry.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot singlesnapshot: dataSnapshot.getChildren()) {
                                                if (singlesnapshot.exists()) {
                                                    Log.d(TAG, "onDataChange: exists");
                                                    firebase.sendParentRequest(sParentName, user);
                                                    Toast.makeText(mContext, "Request Sent", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(mContext, "That Parents Username does not exist", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }else{
                                            Toast.makeText(mContext, "That Parents Username does not exist", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }
            });
        }

        return view;
    }

    private void setUpWidgets(View view) {
        etParentname = view.findViewById(R.id.etParentName);
        submit = view.findViewById(R.id.btnSendToFamilyList);
        allowFamilyName = false;
    }
}
