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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.example.dontforgettograbthat.R;

public class WebViewDialogueFragment extends DialogFragment {
    public WebView wv;
    public Button btnClose;
    private static final String TAG = "WebViewDialogueFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogue_webview, container,false);

        Log.d(TAG, "onCreateView: Attempting to get Arguemtns line 34");
        Bundle mArgs = getArguments();
        assert mArgs != null;
        String itemName = mArgs.getString("key");
        String url = mArgs.getString("url");

        wv = view.findViewById(R.id.webView);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        wv.loadUrl(url+itemName);


        btnClose = view.findViewById(R.id.closeBtn);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }
}
