package com.joseapps.simpleshoppinglist.ActivityAddItem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.joseapps.simpleshoppinglist.Interface.AddItemInterface;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.Const;

import java.util.ArrayList;


public class SecondBrowserPriceAndListName extends android.support.v4.app.Fragment implements View.OnClickListener, View.OnKeyListener, CheckBox.OnCheckedChangeListener{
    //Constants
    private static final String TAG="SearchFragment";

    //Widgets
    private CheckBox chkWalmart, chkCostco, chkZhers, chkMetro, chkCustom;
    private EditText etWalmart, etCostco, etZhers, etMetro, etCustomPrice;
    private EditText etCustomName;
    private String sWalmartPrice, sCostcoPrice,sZhersPrice,sMetroPrice;


    //Interface
    private AddItemInterface mInterface;

    //vars

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_fragment_search, container, false);
        Log.d(TAG, "onCreateView: ");
        //references all views
        referenceViews(view);
        //initialize Interface
        mInterface = (AddItemActivity) getActivity();

        etWalmart.setOnKeyListener(this);
        etCostco.setOnKeyListener(this);
        etZhers.setOnKeyListener(this);
        etMetro.setOnKeyListener(this);
        etCustomPrice.setOnKeyListener(this);


        return view;

    }

    private void referenceViews(View view){

        try {
            chkWalmart = view.findViewById(R.id.chkWalmart);
            chkCostco = view.findViewById(R.id.chkCostco);
            chkZhers = view.findViewById(R.id.chkZhers);
            chkMetro = view.findViewById(R.id.chkMetro);
            chkCustom = view.findViewById(R.id.chkCustom);

            chkWalmart.setOnCheckedChangeListener(this);
            chkCostco.setOnCheckedChangeListener(this);
            chkZhers.setOnCheckedChangeListener(this);
            chkMetro.setOnCheckedChangeListener(this);
            chkCustom.setOnCheckedChangeListener(this);

            etWalmart = view.findViewById(R.id.etWalmartPrice);
            etCostco = view.findViewById(R.id.etCostcoPrice);
            etZhers = view.findViewById(R.id.etZhersPrice);
            etMetro = view.findViewById(R.id.etMetroPrice);
            etCustomPrice = view.findViewById(R.id.etCustomPrice);

            etWalmart.setText(sWalmartPrice);
            etCostco.setText(sCostcoPrice);
            etZhers.setText(sZhersPrice);
            etMetro.setText(sMetroPrice);



            etWalmart.setOnKeyListener(this);
            etCostco.setOnKeyListener(this);
            etZhers.setOnKeyListener(this);
            etMetro.setOnKeyListener(this);
            etCustomPrice.setOnKeyListener(this);

            TextView tvWalmart = view.findViewById(R.id.tvWalmart);
            TextView tvCostco = view.findViewById(R.id.tvCostco);
            TextView tvZhers = view.findViewById(R.id.tvZhers);
            TextView tvMetro = view.findViewById(R.id.tvMetro);
            etCustomName = view.findViewById(R.id.etCustomName);
            etCustomName.setOnKeyListener(this);

            tvWalmart.setOnClickListener(this);
            tvCostco.setOnClickListener(this);
            tvMetro.setOnClickListener(this);
            tvZhers.setOnClickListener(this);

        }catch(NullPointerException e){
            Log.d(TAG, "referenceViews: null pointer " + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int webCode = 0;
        switch (v.getId()){
            case R.id.tvWalmart:
                webCode = Const.WALMART;
                break;
            case R.id.tvCostco:
                webCode = Const.COSTCO;
                break;
            case R.id.tvZhers:
                webCode = Const.ZHERS;
                break;
            case R.id.tvMetro:
                webCode = Const.METRO;
                break;
        }

        String walmart = etWalmart.getText().toString();
        String costco = etCostco.getText().toString();
        String zhers = etZhers.getText().toString();
        String metro = etMetro.getText().toString();

        mInterface.openBrowser(webCode, walmart,costco,zhers,metro );
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.chkWalmart:
                if (isChecked){
                    chkCostco.setChecked(false);
                    chkZhers.setChecked(false);
                    chkMetro.setChecked(false);
                    chkCustom.setChecked(false);
                    if (hasText(etWalmart)) {
                        Double d = Double.parseDouble(etWalmart.getText().toString());
                        mInterface.setPrice(d);
                    }
                    mInterface.setlistName(Const.sWalmart);
                }
                break;
            case R.id.chkCostco:
                if (isChecked){
                    chkWalmart.setChecked(false);
                    chkZhers.setChecked(false);
                    chkMetro.setChecked(false);
                    chkCustom.setChecked(false);
                    if (hasText(etCostco)) {
                        Double d = Double.parseDouble(etCostco.getText().toString());
                        mInterface.setPrice(d);
                    }
                    mInterface.setlistName(Const.sCostco);

                }
                break;
            case R.id.chkZhers:
                if (isChecked){
                    chkWalmart.setChecked(false);
                    chkCostco.setChecked(false);
                    chkMetro.setChecked(false);
                    chkCustom.setChecked(false);
                    if (hasText(etZhers)) {
                        Double d = Double.parseDouble(etZhers.getText().toString());
                        mInterface.setPrice(d);
                    }
                    mInterface.setlistName(Const.sZhers);

                }
                break;
            case R.id.chkMetro:
                if (isChecked){
                    chkWalmart.setChecked(false);
                    chkCostco.setChecked(false);
                    chkZhers.setChecked(false);
                    chkCustom.setChecked(false);
                    if (hasText(etMetro)) {
                        Double d = Double.parseDouble(etMetro.getText().toString());
                        mInterface.setPrice(d);
                    }
                    mInterface.setlistName(Const.sMetro);

                }
                break;
            case R.id.chkCustom:
                if (isChecked){
                    chkWalmart.setChecked(false);
                    chkCostco.setChecked(false);
                    chkZhers.setChecked(false);
                    chkMetro.setChecked(false);
                    if (hasText(etCustomPrice)) {
                        Double d = Double.parseDouble(etCustomPrice.getText().toString());
                        mInterface.setPrice(d);
                    }
                    if (hasText(etCustomName)){
                        String s = etCustomName.getText().toString();
                        mInterface.setlistName(s);
                    }
                }
        }
    }

    private boolean hasText(EditText et){
        return !et.getText().toString().equals("");
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()){

            case R.id.etWalmartPrice:
                if (chkWalmart.isChecked()&&hasText(etWalmart)){
                    double d = Double.parseDouble(etWalmart.getText().toString());
                    mInterface.setPrice(d);
                }
                break;
            case R.id.etCostcoPrice:
                if (chkCostco.isChecked()&&hasText(etCostco)){
                    double d = Double.parseDouble(etCostco.getText().toString());
                    mInterface.setPrice(d);
                }
                break;
            case R.id.etZhersPrice:
                if (chkZhers.isChecked()&&hasText(etZhers)){
                    double d = Double.parseDouble(etZhers.getText().toString());
                    mInterface.setPrice(d);
                }
                break;
            case R.id.etMetroPrice:
                if (chkMetro.isChecked()&&hasText(etMetro)){
                    double d = Double.parseDouble(etMetro.getText().toString());
                    mInterface.setPrice(d);
                }
                break;
            case R.id.etCustomName:
                if (chkCustom.isChecked()&&hasText(etCustomName)){
                    mInterface.setlistName(etCustomName.getText().toString());
                }
            case R.id.etCustomPrice:
                if (chkCustom.isChecked()&&hasText(etCustomPrice)){
                    mInterface.setPrice(Double.parseDouble(etCustomPrice.getText().toString()));
                }

        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle!=null){
            if (bundle.get(Const.REFRESH)!=null){
                reset();
            }
            if (bundle.getString(Const.WEBVIEWCODE)!=null){
                sWalmartPrice=(bundle.getString(Const.sWalmart));
                sCostcoPrice=(bundle.getString(Const.sCostco));
                sZhersPrice=(bundle.getString(Const.sZhers));
                sMetroPrice=(bundle.getString(Const.sMetro));
            }
        }
    }

    private void reset(){
        chkWalmart.callOnClick();
        chkWalmart.setChecked(false);

        etCostco.setText("");
        etWalmart.setText("");
        etCustomName.setText("");
        etMetro.setText("");
        etCustomPrice.setText("");
        etZhers.setText("");

    }
}