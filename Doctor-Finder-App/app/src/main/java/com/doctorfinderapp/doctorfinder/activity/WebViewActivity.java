package com.doctorfinderapp.doctorfinder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.doctorfinderapp.doctorfinder.R;


public class WebViewActivity extends AppCompatActivity {
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);


        // Makes Progress bar Visible
        //getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);


        setContentView(R.layout.activity_web_view);
        ProgressBar progressBarWeb = (ProgressBar) findViewById(R.id.progressBarWeb);
        final RelativeLayout rel1 = (RelativeLayout) findViewById(R.id.rel1);

        Bundle b = getIntent().getExtras();
        String URL = b.getString("URL");
        //Toast.makeText(this,URL,Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_webview);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        myWebView = (WebView) findViewById(R.id.webview);
        final Activity activity = this;

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO: 4/13/16 fatto giusto il rigo seguente? si è aggiunto con alt+enter però il warnings è sparito
                assert rel1 != null;
                rel1.setVisibility(View.VISIBLE);
            }
        });

        myWebView.loadUrl(URL);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        // or call onBackPressed()
        return true;
    }
}
