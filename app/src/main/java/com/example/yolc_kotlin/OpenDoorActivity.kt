package com.example.yolc_kotlin

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.databinding.ActivityOpenBinding

class OpenDoorActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val bind = ActivityOpenBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val wView = bind.streaming


        val ws = wView.settings
        ws.setSupportMultipleWindows(false)
        ws.useWideViewPort
        ws.loadWithOverviewMode
        ws.javaScriptEnabled
        ws.domStorageEnabled
        wView.webViewClient = WebViewClient()
        wView.loadUrl("http://heean6620.iptime.org:12345")


    }
}