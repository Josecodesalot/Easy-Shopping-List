package com.example.dontforgettograbthat.AddItemActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dontforgettograbthat.Dialogs.UnAuthenticatedDialogFragment;
import com.example.dontforgettograbthat.Interface.IAddItem;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddToListOrRequestFragment extends android.support.v4.app.Fragment  {
    private static final String TAG="AddToListOrReque";
    private IAddItem mInterface;
    private Button btnAddToFamilyList, btnAddToOwnList;
    private User currentUser;
    private Item item;
    FirebaseMethods firebaseMethods = new FirebaseMethods(getActivity());

    //firebase
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_or_submit, container,false);
        Log.d(TAG, "onCreateView: ");
        referenceWidgets(view);
        mAuth= FirebaseAuth.getInstance();
        mInterface = (AddItemActivity) getContext();

        currentUser = ((UserClient) (getActivity().getApplicationContext())).getUser();
        item= ((UserClient) (getActivity().getApplicationContext())).getItem();

        String s = "Send To " + currentUser.getParent_name() + "'s list";
        btnAddToFamilyList.setText(s);

        btnAddToOwnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.addItemToList();

                firebaseMethods.addItemToList(item);
            }
        });

        btnAddToFamilyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.addItemToFamilyList();
                firebaseMethods.addItemToFamilyList(item, currentUser);
            }
        });

        return view;
    }

    public void referenceWidgets(View view){
        btnAddToFamilyList = view.findViewById(R.id.btnSendToFamilyList);
        btnAddToOwnList = view.findViewById(R.id.btnAddToOwnList);
    }
}