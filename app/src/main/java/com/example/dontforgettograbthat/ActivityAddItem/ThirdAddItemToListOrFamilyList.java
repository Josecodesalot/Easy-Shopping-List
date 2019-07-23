package com.example.dontforgettograbthat.ActivityAddItem;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dontforgettograbthat.Interface.AddItemInterface;
import com.example.dontforgettograbthat.R;

public class ThirdAddItemToListOrFamilyList extends android.support.v4.app.Fragment  {
    private AddItemInterface mInterface;
    private Button btnFamilyLis, btnYourList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_or_submit, container,false);
        referenceWidgets(view);

        mInterface = (AddItemActivity) getContext();

        btnFamilyLis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.addItemToFamilyList();
            }
        });

        btnYourList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.addItemToList();
            }
        });
        return view;
    }

    private void referenceWidgets(View view){
        btnFamilyLis = view.findViewById(R.id.tvFamilyList);
        btnYourList = view.findViewById(R.id.tvMyList);
    }
}