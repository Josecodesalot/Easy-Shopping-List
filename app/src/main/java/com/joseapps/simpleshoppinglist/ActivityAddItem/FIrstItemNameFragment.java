package com.joseapps.simpleshoppinglist.ActivityAddItem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.joseapps.simpleshoppinglist.Interface.AddItemInterface;
import com.joseapps.simpleshoppinglist.R;

public class FIrstItemNameFragment extends android.support.v4.app.Fragment {
    private static final String TAG="AddNameAndQuan";
    private EditText itemName, itemQuantity;
    private AddItemInterface mInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_quantity, container, false);
        Log.d(TAG, "onCreateView: ");
        referenceViews(view);

        mInterface = (AddItemActivity) getContext();

        itemName.requestFocus();
        itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "onFocusChange: Item Name data sent");
                    mInterface.setItemName(itemName.getText().toString());
                }
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
                if (!hasFocus) {
                    if (!itemQuantity.getText().toString().equals("")) {
                        mInterface.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                        Log.d(TAG, "onFocusChange: itemQuantity sending data");

                    } else {
                        mInterface.setQuantity(0);
                        Log.d(TAG, "onFocusChange: itemQuantity sending data ");
                    }
                }
            }
        });

        itemQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {
                    mInterface.setPage(1);
                }
                return false;
            }
        });
    return view;
    }


    private void referenceViews(View view) {
    itemName= view.findViewById(R.id.etItemName);
    itemQuantity = view.findViewById(R.id.etQuantity);
    }

    private void reset(){
        Log.d(TAG, "reset: called");
        itemName.setText("");
        itemQuantity.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle!=null){

            reset();
        }else{
            Log.d(TAG, "onResume: bundle null");
        }
    }
}