package com.example.dontforgettograbthat.CommonFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dontforgettograbthat.ActivityProfile.ProfileActivity;
import com.example.dontforgettograbthat.AddItemActivity.AddItemActivity;
import com.example.dontforgettograbthat.HistoryActivity.HistoryActivity;
import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.ActivityRequestItems.RequestItemsActivity;

public class TopTitleFragment extends android.support.v4.app.Fragment {
    private ImageView analytics, add, request, profile, cart;
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

    private void setTopTitle() {
        if (getActivity() instanceof CartActivity){
            title.setText("Shopping-List");
        }
        if (getActivity() instanceof ProfileActivity){
            title.setText("Your Profile");
        }
        if (getActivity() instanceof RequestItemsActivity){
            title.setText("Your Requests");
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestItemsActivity.class);
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
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ReferenceViews(View view) {
        analytics = view.findViewById(R.id.analyticsIcon);
        add = view.findViewById(R.id.addIcon);
        request = view.findViewById(R.id.requestIcon);
        title = view.findViewById(R.id.tvTitlePage);
        profile=  view.findViewById(R.id.profile);
        cart =  view.findViewById(R.id.cart);

    }
}
