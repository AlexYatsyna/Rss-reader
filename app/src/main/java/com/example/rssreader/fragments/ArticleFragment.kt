package com.example.rssreader.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.rssreader.R


class ArticleFragment : AppCompatActivity() {

     private lateinit var web : WebView
     lateinit var curUrl : String
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_article)

        curUrl = intent.getStringExtra("url").toString()
        web = findViewById<WebView>(R.id.webView)
        web.settings.javaScriptEnabled = true
        web.webViewClient = WebViewClient()

        web.loadUrl(curUrl)

        web.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                var name = (curUrl.split("//"))[1].split("/")[0]
                var url = request?.url.toString()

                if(url.contains("//"))
                    url = url.split("//")[1]
                if(url.contains('?'))
                    url= url.split('?')[0]
                if(url.contains('/'))
                    url=url.split('/')[0]
                if(url.contains('@'))
                    return true

                if(url.contains(name))
                    return false
                return true
            }

        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}



