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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dontforgettograbthat.Dialogs.UnAuthenticatedDialogFragment;
import com.example.dontforgettograbthat.Interface.IAddItem;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddToListOrRequestFragment extends android.support.v4.app.Fragment  {
    private static final String TAG="AddToListOrReque";
    private IAddItem mInterface;
    private TextView tvFamilyName;
    private TextView addToList;
    private Button request;
    private User user;
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

        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser()!=null){
                    //tells mother activity (AddItemActivity) to add to the database if appropriate
                    mInterface.triggers(1);
                }else{
                    Log.d(TAG, "onClick: testing out dialog fragment unAuthenticated");
                    //testing Dialogue Fragment and Firebase Navigation
                    FragmentManager fragmentManager = getFragmentManager();
                    UnAuthenticatedDialogFragment alert =  new UnAuthenticatedDialogFragment();
                    assert fragmentManager != null;
                    alert.show(fragmentManager, "1");
                }
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tells mother activity (AddItemActivity) to send request to database
                if (user!=null){
                    tvFamilyName.setText(user.getParent_name());
                    Toast.makeText(getActivity(), "Request sent to the list of the follwing Family : " + user.getParent_name(), Toast.LENGTH_SHORT).show();
                }
                mInterface.triggers(2);
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 user= new User(
                        dataSnapshot.getValue(User.class).getUser_id(),
                        dataSnapshot.getValue(User.class).getEmail(),
                        dataSnapshot.getValue(User.class).getUsername(),
                        dataSnapshot.getValue(User.class).getParent_name()
                );

                 setFamilyName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setFamilyName() {
        if (user!=null) {
            tvFamilyName.setText(user.getParent_name());
        }
    }

    public void referenceWidgets(View view){
        request= view.findViewById(R.id.btnSubmit);
        addToList = view.findViewById(R.id.tvAddToYourList);
        tvFamilyName =view.findViewById(R.id.tvFamilyName);
    }
}