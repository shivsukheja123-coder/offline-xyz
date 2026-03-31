package com.offlinexyz.tallysync

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webview)
        configureWebView(webView)

        if (isOnline()) {
            webView.loadUrl("https://tally.example.com")
        } else {
            webView.loadUrl("file:///android_asset/offline.html")
        }
    }

    private fun configureWebView(webView: WebView) {
        webView.webViewClient = WebViewClient()
        with(webView.settings) {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            domStorageEnabled = true
            setSupportZoom(false)
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
