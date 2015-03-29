package com.sparklegrid.radium.feedengineng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Rahman on 3/29/2015.
 */
public class DisplayWebPageActivity extends Activity {
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Intent in = getIntent();
        String page_url = in.getStringExtra("page_url");

        webView = (WebView) findViewById(R.id.webpage);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(page_url);

        webView.setWebViewClient(new DisplayWebPageActivityClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return  true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private class DisplayWebPageActivityClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }
    }

}
