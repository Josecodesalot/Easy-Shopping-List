package com.example.dontforgettograbthat.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import com.example.dontforgettograbthat.ActivityProfile.ChildRequestActivity;
import com.example.dontforgettograbthat.Interface.ChildrenRequestInterface;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;

public class AcceptOrRejectRequestDialog extends DialogFragment {

    TextView username;
    Button deleteRequest, acceptIntoFamily;
    ChildrenRequestInterface mInterface;
    private static final String TAG = "AcceptOrRejectRequestDi";
    User user;



    public static AcceptOrRejectRequestDialog newInstance(User user) {
        AcceptOrRejectRequestDialog frag = new AcceptOrRejectRequestDialog();
        frag.setUser(user);
        return frag;
    }

    public void setUser(User user) {
        this.user=user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dailog_add_reject_user, container, false);
        mInterface = (ChildRequestActivity) getContext();
        setRetainInstance(true);
        referenceViews(view);


        username.setText(user.getUsername());

        deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.reject(user);
                dismiss();
            }
        });

        acceptIntoFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.accept(user);
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
