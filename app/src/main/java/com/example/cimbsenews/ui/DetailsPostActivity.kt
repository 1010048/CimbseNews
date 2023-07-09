package com.example.cimbsenews.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cimbsenews.R

class DetailsPostActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var backButton: Button

    @SuppressLint("SetJavaScriptEnabled", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_post)

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        backButton = findViewById(R.id.backButton)
        webView.settings.javaScriptEnabled = true

        val url = intent.getStringExtra(EXTRA_URL)
        val postName = intent.getStringExtra(EXTRA_POST_NAME)

        url?.let {
            webView.webChromeClient = WebChromeClient()
            webView.webViewClient = CustomWebViewClient()
            webView.loadUrl(it)
        }

        val textViewPostName = findViewById<TextView>(R.id.toolbarCountieName)
        textViewPostName.text = postName

        backButton.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_POST_NAME = "extra_post_name"
    }
}
