package com.example.dontforgettograbthat.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.dontforgettograbthat.ActivityProfile.RequestActivity;
import com.example.dontforgettograbthat.Interface.AcceptDeleteOrHoldInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;

import java.util.ArrayList;

public class AcceptOrRejectRequestDialog extends DialogFragment {

    TextView username;
    Button deleteRequest, acceptIntoFamily;
    AcceptDeleteOrHoldInterface mInterface;
    int position;
    private static final String TAG = "AcceptOrRejectRequestDi";
    ArrayList<Item> items;

    public static AcceptOrRejectRequestDialog newInstanceOfAcceptOrRejectDialog(ArrayList<Item> items) {
        AcceptOrRejectRequestDialog dialog = new AcceptOrRejectRequestDialog();
        dialog.setItems(items);
        return dialog;
    }


    public void setItems(ArrayList<Item> items) {
        this.items=items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dailog_add_reject_user, container, false);
        mInterface = (RequestActivity) getContext();
        setRetainInstance(true);
        referenceViews(view);
        position = 69;

        Bundle bundle = getArguments();
        if (bundle!=null) {
            username.setText(bundle.getString("username"));
            position = bundle.getInt("position");
            Log.d(TAG, "onCreateView: " + position);
        }else{
            Toast.makeText(getActivity(), "Error, we are working to fix this error, please wait for us!", Toast.LENGTH_SHORT).show();
            dismiss();
        }

        deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.reject(position);
                dismiss();
            }
        });

        acceptIntoFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.accept(position);
                dismiss();
            }
        });

        return view;
    }

    private void referenceViews(View view) {
        username = view.findViewById(R.id.tvUserName);
        deleteRequest = view.findViewById(R.id.btnReject);
        acceptIntoFamily = view.findViewById(R.id.btnAccept);
    }


}
