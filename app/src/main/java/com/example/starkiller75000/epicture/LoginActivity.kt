package com.example.starkiller75000.epicture

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient

class LoginActivity : AppCompatActivity() {

    var client_id: String = "df9e9932bc6786b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intent = Intent(applicationContext, HomeActivity::class.java)
        val type = "token"
        val imgurWebView = findViewById<WebView>(R.id.LoginWebView)
        imgurWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=$client_id&response_type=$type")
        imgurWebView.settings.javaScriptEnabled

        imgurWebView.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String) : Boolean {
                intent.putExtra("url", url)
                startActivity(intent)
                finish()
                return true
            }
        }
    }
}