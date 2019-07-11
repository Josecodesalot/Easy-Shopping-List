package com.example.dontforgettograbthat.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dontforgettograbthat.ActivityProfile.ChildrenMangementActivity;
import com.example.dontforgettograbthat.Interface.ChildrenManagementInterface;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;

public class ChildrenManagementDialog extends DialogFragment {

    TextView username;
    Button deleteChild, sendBackToRequest;
    ChildrenManagementInterface mInterface;
    private static final String TAG = "AcceptOrRejectRequestDi";
    User user;

    public static ChildrenManagementDialog newInstance(User user) {
        ChildrenManagementDialog frag = new ChildrenManagementDialog();
        frag.setUser(user);
        return frag;
    }

    public void setUser(User user) {
        this.user=user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_children_management, container, false);
        mInterface = (ChildrenMangementActivity) getContext();
        setRetainInstance(true);
        referenceViews(view);


        username.setText(user.getUsername());

        deleteChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.Delete(user);
                dismiss();
            }
        });

        sendBackToRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.SendBack(user);
                dismiss();
            }
        });

        return view;
    }

    private void referenceViews(View view) {
        username = view.findViewById(R.id.tvUserName);
        deleteChild = view.findViewById(R.id.btnReject);
        sendBackToRequest = view.findViewById(R.id.btnSendBack);
    }


}
