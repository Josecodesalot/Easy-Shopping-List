package com.joseapps.simpleshoppinglist.CommonFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joseapps.simpleshoppinglist.ActivityProfile.UserSettingsActivity;
import com.joseapps.simpleshoppinglist.ActivityAddItem.AddItemActivity;
import com.joseapps.simpleshoppinglist.ActivityHistory.HistoryActivity;
import com.joseapps.simpleshoppinglist.ActivityCart.CartActivity;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.ActivityFamilyList.FamilyListActivity;

public class TopTitleFragment extends android.support.v4.app.Fragment {
    private ImageView analytics, addItem, familyList, userSettings, cart;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_navigation, container,false);
        ReferenceViews(view);
        Navigation();
        setTopTitle();
        return view;
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    private void setTopTitle() {
        if (getActivity() instanceof CartActivity){
            title.setText("Shopping-List");
        }
        if (getActivity() instanceof UserSettingsActivity){
            title.setText("Your Profile");
        }
        if (getActivity() instanceof FamilyListActivity){
            title.setText("Your Family Requests");
        }
        if (getActivity() instanceof HistoryActivity){
            title.setText("Purchase History");
        }
        if(getActivity() instanceof AddItemActivity){
            title.setText("Find The Best Price");
        }
    }

    private void Navigation() {
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
            }
        });
        familyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FamilyListActivity.class);
                startActivity(intent);
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });
        userSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ReferenceViews(View view) {
        analytics = view.findViewById(R.id.analyticsIcon);
        addItem = view.findViewById(R.id.addIcon);
        familyList = view.findViewById(R.id.requestIcon);
        title = view.findViewById(R.id.tvTitlePage);
        userSettings =  view.findViewById(R.id.profile);
        cart =  view.findViewById(R.id.cart);

    }
}
