package com.example.dontforgettograbthat.Dialogs;

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

import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddFamilyNameDialog extends DialogFragment {

    public EditText etParentname;
    public String sParentName;
    public Button submit;
    public boolean allowFamilyName;
    private FirebaseMethods firebase;
    private static final String TAG = "AddFamilyNameDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_parentname, container, false);

        firebase = new FirebaseMethods(getActivity());

        etParentname = view.findViewById(R.id.etParentName);
        submit = view.findViewById(R.id.btnSubmit);
        allowFamilyName = false;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sParentName = etParentname.getText().toString().toLowerCase();
                Query qry = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(sParentName);
                qry.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Log.d(TAG, "onDataChange: exists");
                            if (!sParentName.equals("")) {
                                firebase.sendParentRequest(sParentName, ((UserClient)(getActivity().getApplicationContext())).getUser());
                                Toast.makeText(getActivity(), "SendBack Sent to " + sParentName, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Parent Name is Empty", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "That Parents Username does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }
}
