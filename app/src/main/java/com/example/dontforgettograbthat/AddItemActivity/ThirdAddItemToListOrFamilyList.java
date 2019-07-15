package com.example.dontforgettograbthat.AddItemActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dontforgettograbthat.Interface.IAddItem;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.Const;
import com.example.dontforgettograbthat.utils.UserClient;
import com.google.firebase.auth.FirebaseAuth;

public class ThirdAddItemToListOrFamilyList extends android.support.v4.app.Fragment  {
    private static final String TAG="AddToListOrReque";
    private IAddItem mInterface;

    private Button submit;
    private TextView tvFamilyList, tvYourList;
    private Switch aSwitch;

    private User currentUser;
    private Item item;

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

        aSwitch.setChecked(true);
        tvFamilyList.setTextColor(Color.WHITE);
        tvFamilyList.setBackgroundResource(R.drawable.background_rounded_primary);

        tvYourList.setTextColor(Color.BLACK);
        tvYourList.setBackgroundColor(Color.WHITE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()){
                    mInterface.addItemToFamilyList();
                }else{
                    mInterface.addItemToList();
                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //family
                    tvFamilyList.setTextColor(Color.WHITE);
                    tvFamilyList.setBackgroundResource(R.drawable.background_rounded_primary);

                    tvYourList.setTextColor(Color.BLACK);
                    tvYourList.setBackgroundColor(Color.WHITE);

                }else{
                    //unchecked, yout lsit
                     tvYourList.setTextColor(Color.WHITE);
                    tvYourList.setBackgroundResource(R.drawable.background_rounded_primary);

                    tvFamilyList.setTextColor(Color.BLACK);
                    tvFamilyList.setBackgroundColor(Color.WHITE);
                }
            }
        });

        return view;
    }

    public void referenceWidgets(View view){
        submit = view.findViewById(R.id.btnSubmit);
        tvFamilyList = view.findViewById(R.id.tvFamilyList);
        tvYourList = view.findViewById(R.id.tvMyList);
        aSwitch = view.findViewById(R.id.aSwitch);
    }
}