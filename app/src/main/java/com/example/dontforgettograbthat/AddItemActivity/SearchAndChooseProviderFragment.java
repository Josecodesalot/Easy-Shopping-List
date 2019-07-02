package com.example.dontforgettograbthat.AddItemActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dontforgettograbthat.Dialogs.WebViewDialogueFragment;
import com.example.dontforgettograbthat.Interface.IAddItem;
import com.example.dontforgettograbthat.R;


public class SearchAndChooseProviderFragment extends android.support.v4.app.Fragment
{
    //Constants
    private static final String TAG="SearchFragment";

    //Widgets
    private CheckBox chkWalmart, chkCostco, chkZhers, chkMetro, chkCustom;
    private EditText etWalmart, etCostco, etZhers, etMetro, etCustomPrice;
    private TextView tvWalmart, tvCostco, tvZhers, tvMetro;
    private EditText etCustomName;
    public WebView wv;

    //vars
    private String itemName;
    public  Bundle args;

    //Interface
    private IAddItem mInterface;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container,false);
        Log.d(TAG, "onCreateView: ");
        //references all views
        referenceViews(view);
        //initialize Interface
        mInterface=(AddItemActivity) getActivity();
        //This method does:
        //
        //   #1 Make only one checkBox Checkable at the same time
        //   #2 Initialize a checkBox Arrray
        //   #3 Use the interface to send the name of the list associated
        //      to the checkbot to AddItemActivity.java
        setUpCheckBoxes();
        //this method takes data from the price EditText and sends it to
        //AddItemActivity.java Only if the associated CheckBox isChecked
        sendPriceData(view);
        //This Methods creates OnClickListeners for the TextViews, and hooks up a dialogue box
        //with a Webview. It lands on a search query of the ItemName on the Relevant Grocery Proveders Website
        setUpBrowserCheck();

        ((AddItemActivity)getActivity()).setOnBundleSelected(new AddItemActivity.SelectedBundle() {
            @Override
            public void onBundleSelect(Bundle bundle) {
                if (bundle!=null){
                    itemName = bundle.getString("key");
                    Log.d(TAG, "onBundleSelect: should have recieved key :" + itemName);

                }
            }
        });



        return view;
    }

    public void setUpBrowserCheck() {
        args = new Bundle();
        args.putString("key", "");

        tvWalmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: tvWalmart");
                FragmentManager fragmentManager = getFragmentManager();
                WebViewDialogueFragment userPopUp = new WebViewDialogueFragment();
                args.putString("key", itemName);
                args.putString("url", "https://www.walmart.ca/search/");
                userPopUp.setArguments(args);
                assert fragmentManager != null;
                userPopUp.show(fragmentManager, "1");
            }
        });

        tvCostco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: tvCostco");
                FragmentManager fragmentManager = getFragmentManager();
                WebViewDialogueFragment userPopUp = new WebViewDialogueFragment();
                args.putString("key", itemName);
                args.putString("url", "https://www.costco.ca/CatalogSearch?dept=All&keyword=");
                userPopUp.setArguments(args);
                assert fragmentManager != null;
                userPopUp.show(fragmentManager, "1");

            }
        });

        tvZhers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: tvCostco");
                FragmentManager fragmentManager = getFragmentManager();
                WebViewDialogueFragment userPopUp = new WebViewDialogueFragment();
                args.putString("key", itemName);
                args.putString("url", "https://www.zehrs.ca/search/?search-bar=");
                userPopUp.setArguments(args);
                assert fragmentManager != null;
                userPopUp.show(fragmentManager, "1");

            }
        });

        tvMetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: tvCostco");
                FragmentManager fragmentManager = getFragmentManager();
                WebViewDialogueFragment userPopUp = new WebViewDialogueFragment();
                args.putString("key", itemName);
                args.putString("url", "https://www.metro.ca/en/search?filter=");
                userPopUp.setArguments(args);
                assert fragmentManager != null;
                userPopUp.show(fragmentManager, "1");

            }
        });

    }

    private void sendPriceData(View view) {

            etWalmart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (chkWalmart.isChecked()){
                        if (!etWalmart.getText().toString().equals("")){
                        mInterface.setPrice(Double.parseDouble(etWalmart.getText().toString()));
                        }else {
                        mInterface.setPrice(0);
                        }
                    }
                }
            });
        etCostco.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (chkCostco.isChecked()){
                    if (!etCostco.getText().toString().equals("")){
                        mInterface.setPrice(Double.parseDouble(etCostco.getText().toString()));
                    }else {
                        mInterface.setPrice(0);
                    }
                }
            }
        });

        etZhers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (chkZhers.isChecked()){
                    if (!etZhers.getText().toString().equals("")){
                        mInterface.setPrice(Double.parseDouble(etZhers.getText().toString()));
                    }else {
                        mInterface.setPrice(0);
                    }
                }
            }
        });
        etMetro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (chkMetro.isChecked()){
                    if (!etMetro.getText().toString().equals("")){
                        mInterface.setPrice(Double.parseDouble(etMetro.getText().toString()));
                    }else {
                        mInterface.setPrice(0);
                    }
                }
            }
        });

        etCustomPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (chkCustom.isChecked()){
                    if (!etCustomPrice.getText().toString().equals("")){
                        mInterface.setPrice(Double.parseDouble(etCustomPrice.getText().toString()));
                    }else {
                        mInterface.setPrice(0);
                    }
                }
            }
        });
        etCustomName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (chkCustom.isChecked()){
                    mInterface.setlistName(etCustomName.getText().toString());
                }
            }
        });
    }

    private void setUpCheckBoxes() {
        chkWalmart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mInterface.setlistName("Walmart");
                    chkCostco.setChecked(false);
                    chkZhers.setChecked(false);
                    chkMetro.setChecked(false);
                    chkCustom.setChecked(false);
                }
            }
        });


        chkCostco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mInterface.setlistName("Costco");
                    chkWalmart.setChecked(false);

                    chkZhers.setChecked(false);
                    chkMetro.setChecked(false);
                    chkCustom.setChecked(false);
                }
            }
        });

        chkZhers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mInterface.setlistName(tvZhers.getText().toString());

                    chkWalmart.setChecked(false);
                    chkCostco.setChecked(false);

                    chkMetro.setChecked(false);
                    chkCustom.setChecked(false);
                }
            }
        });

        chkMetro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mInterface.setlistName(tvMetro.getText().toString());

                    chkWalmart.setChecked(false);
                    chkCostco.setChecked(false);
                    chkZhers.setChecked(false);

                    chkCustom.setChecked(false);
                }
            }
        });

        chkCustom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mInterface.setlistName(etCustomName.getText().toString());
                    chkWalmart.setChecked(false);
                    chkCostco.setChecked(false);
                    chkZhers.setChecked(false);
                    chkMetro.setChecked(false);
                }
            }
        });
    }

    private void referenceViews(View view){

        try {
            chkWalmart = view.findViewById(R.id.chkWalmart);
            chkCostco = view.findViewById(R.id.chkCostco);
            chkZhers = view.findViewById(R.id.chkZhers);
            chkMetro= view.findViewById(R.id.chkMetro);
            chkCustom=view.findViewById(R.id.chkCustom);

            etWalmart= view.findViewById(R.id.etWalmartPrice);
            etCostco= view.findViewById(R.id.etCostcoPrice);
            etZhers= view.findViewById(R.id.etZhersPrice);
            etMetro= view.findViewById(R.id.etMetroPrice);
            etCustomPrice = view.findViewById(R.id.etCustom);

            tvWalmart = view.findViewById(R.id.tvWalmart);
            tvCostco = view.findViewById(R.id.tvCostco);
            tvZhers= view.findViewById(R.id.tvZhers);
            tvMetro= view.findViewById(R.id.tvMetro);

            etCustomName = view.findViewById(R.id.etCustomName);

        }catch(NullPointerException e){
            Log.d(TAG, "referenceViews: null pointer " + e.getMessage());
        }
    }

}