package com.example.dontforgettograbthat.AddItemActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

import com.example.dontforgettograbthat.Interface.IAddItem;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;

import java.util.ArrayList;

public class AddNameAndQuantityFragment extends android.support.v4.app.Fragment {
    private static final String TAG="AddNameAndQuan";
    private EditText itemName, itemQuantity;
    private IAddItem mInterface;
    private ArrayList<String> url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_quantity, container,false);
        Log.d(TAG, "onCreateView: ");
        referenceViews(view);
        url= new ArrayList<>();
        url.add("https://www.walmart.ca/search/");
        url.add("https://www.costco.ca/CatalogSearch?dept=All&keyword=");
        url.add("https://www.zehrs.ca/search/?search-bar=");
        url.add("https://www.metro.ca/en/search?filter=");

        mInterface = (AddItemActivity) getContext();

        itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: Item Name data sent" );
                mInterface.setItemName(itemName.getText().toString());
            }
        });

        itemName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.d(TAG, "onKey: key code =" + keyCode);
                    itemQuantity.callOnClick();
                }
                return false;
            }
        });


        itemQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }
                if (!itemQuantity.getText().toString().equals("")){
                    mInterface.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                    Log.d(TAG, "onFocusChange: itemQuantity sending data");
                }else {
                    mInterface.setQuantity(0);
                    Log.d(TAG, "onFocusChange: itemQuantity sending data ");

                        WebView web1 = new WebView(getActivity());
                        web1.loadUrl(url.get(0)+itemName.getText().toString());
                    WebView web2 = new WebView(getActivity());
                    web2.loadUrl(url.get(1)+itemName.getText().toString());
                    WebView web3 = new WebView(getActivity());
                    web3.loadUrl(url.get(2)+itemName.getText().toString());
                    WebView web4 = new WebView(getActivity());
                    web4.loadUrl(url.get(3)+itemName.getText().toString());


                }
            }
        });
        return view;
    }

    private void referenceViews(View view) {
    itemName= view.findViewById(R.id.etItemName);
    itemQuantity = view.findViewById(R.id.etQuantity);
    }
}