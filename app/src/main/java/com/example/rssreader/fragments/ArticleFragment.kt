package com.example.rssreader.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.rssreader.R


class ArticleFragment : AppCompatActivity() {

     private lateinit var web : WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_article)

        val url = intent.getStringExtra("url")
        web = findViewById<WebView>(R.id.webView)


        web.settings.javaScriptEnabled = true
        web.webViewClient = WebViewClient()

        web.loadUrl(url!!)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}