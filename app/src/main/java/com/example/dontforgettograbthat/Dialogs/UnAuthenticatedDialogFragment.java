package com.example.dontforgettograbthat.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dontforgettograbthat.Login.LoginActivity;
import com.example.dontforgettograbthat.Login.RegisterActivity;
import com.example.dontforgettograbthat.R;

public class UnAuthenticatedDialogFragment extends DialogFragment {

    private static final String TAG = "UnAuthenticatedDialogFragment";
    private Button  login, register, sendToEmail;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_unauthenticated, container, false);
        referenceWidgets(view);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent (getActivity(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        sendToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }

    public void referenceWidgets(View view){
        login = view.findViewById(R.id.btnLogin);
        register = view.findViewById(R.id.btnRegister);
        sendToEmail = view.findViewById(R.id.btnSendToEmail);
    }
}
