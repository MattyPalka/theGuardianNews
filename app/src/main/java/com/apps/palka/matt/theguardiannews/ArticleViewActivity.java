package com.apps.palka.matt.theguardiannews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import static android.view.View.GONE;


public class ArticleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);
        // article title holder that is received from list item via Itent
        String titleObject;
        String urlObject;

        // Get the title of the article from the Intent(list item) and set it as a activity label
        titleObject = getIntent().getStringExtra("articleTitle");
        setTitle(titleObject);

        // Get the URL of the article from the List Item via Intent
        urlObject = getIntent().getStringExtra("articleURL");

        // Find progress bar and webView in the activity_article_view.xml
        final ProgressBar progressBar = findViewById(R.id.progress_circle);
        WebView webView = findViewById(R.id.web_view);

        // Show progress bar until the page loads
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(GONE);
            }
        });

        // Load the URL
        webView.loadUrl(urlObject);
    }



}
