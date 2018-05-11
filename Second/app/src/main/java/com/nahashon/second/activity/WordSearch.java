package com.nahashon.second.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.nahashon.second.R;

public class WordSearch extends AppCompatActivity {
    WebView webView;
    TextView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_search);

        webView  = findViewById(R.id.web_view);
        search=findViewById(R.id.search);


        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.com");
        int i;
        while ((i=webView.getProgress())<100){
            progress(i);
        }


    }
    public void progress(int i){
        search.setText(Integer.toString(i));
    }
}
