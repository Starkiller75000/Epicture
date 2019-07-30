package com.example.starkiller75000.epicture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

        fun Home(view: View?) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
    }

    fun sign_up(view: View?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://imgur.com/register"))
        startActivity(browserIntent)
    }
}