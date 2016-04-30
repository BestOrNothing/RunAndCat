package com.example.lucas.running_cat1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by yanzhensong on 4/25/16.
 */
public class InfoWebActivity extends Activity {
    private WebView webView;
    private String url;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.info_web);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        webView = (WebView) findViewById(R.id.info_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
