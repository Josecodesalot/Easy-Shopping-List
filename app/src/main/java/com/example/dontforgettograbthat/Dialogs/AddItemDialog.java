package com.example.dontforgettograbthat.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dontforgettograbthat.CartActivity.CartActivity;
import com.example.dontforgettograbthat.Interface.DialogInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;


public class AddItemDialog extends DialogFragment {
    private Context mContext = getActivity();
    private static final String TAG = "AddItemDialog";
    private EditText etItemName, etItemCount;
    private ImageView addBtn;

    private String ItemName;
    private Long ItemCount;

    DialogInterface mInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_item , container, false);
        Log.d(TAG, "onCreateView: Started");

        mInterface = (CartActivity) getContext();

        ReferenceViews(view);
        RunButton();
        return view;
    }

    private void RunButton() {
        Log.d(TAG, "RunButton: Starting Run");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpItemNameAndCountVars();

                //sends Item Count and Name to CartActivity
                mInterface.ItemCount(ItemCount);
                mInterface.ItemName(ItemName);
                mInterface.trigger(1);
                dismiss();
            }
        });
    }

    private void setUpItemNameAndCountVars() {
        try {
            ItemName = etItemName.getText().toString();
            ItemCount = Long.parseLong(etItemCount.getText().toString());
        }catch (Exception e){
            Toast.makeText(mContext, "Error: Please put a valid decimal on the Item Count Section", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "setUpItemNameAndCountVars: caght exeption e + " + e.getMessage());
        }
        }

    private void ReferenceViews(View view) {
        Log.d(TAG, "ReferenceViews: Started");
        addBtn = view.findViewById(R.id.imgAdd);
        etItemName = view.findViewById(R.id.etItemName);
        etItemCount = view.findViewById(R.id.etItemCount);
    }
}
