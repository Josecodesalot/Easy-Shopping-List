package com.joseapps.simpleshoppinglist.ActivityAddItem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.Const;

public class WebViewSearchActivity extends AppCompatActivity {

    private Button submit;
    private WebView webView;
    private EditText etItemName, etItemPrice;
    private Context mContext = WebViewSearchActivity.this;
    private Bundle bundleGetExtras;
    private static final String TAG = "WebViewSearchActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webviewsearch);
        setUpWidgets();


        bundleGetExtras = getIntent().getExtras();
        Log.d(TAG, "onCreate: bundle extras = " + bundleGetExtras.get(Const.ITEMNAME));

        etItemName.setText(bundleGetExtras.getString(Const.ITEMNAME));
        etItemPrice.setText(bundleGetExtras.getString(bundleGetExtras.getString(Const.WHICHLIST)));


        String url = bundleGetExtras.getString(Const.URL);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(url + etItemName.getText().toString());
    }

    private void setUpWidgets() {
        submit = findViewById(R.id.btnSubmit);
        webView = findViewById(R.id.webView);
        etItemName = findViewById(R.id.etItemName);
        etItemPrice = findViewById(R.id.etItemPrice);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddItemActivity.class);
                bundleGetExtras.putString(Const.WEBVIEWCODE, Const.WEBVIEWCODE);
                bundleGetExtras.putString(bundleGetExtras.getString(Const.WHICHLIST), etItemPrice.getText().toString());


                bundleGetExtras.putString(Const.ITEMNAME, etItemName.getText().toString());
                intent.putExtras(bundleGetExtras);
                startActivity(intent);

            }
        });
    }
}