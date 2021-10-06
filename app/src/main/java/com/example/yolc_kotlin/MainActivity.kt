package com.example.yolc_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.yolc_kotlin.InF.YOLC_API
import com.example.yolc_kotlin.data.PostItem
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*

import retrofit2.converter.gson.GsonConverterFactory

public class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        allowEntry.isEnabled = false
        test.setOnClickListener {
            allowEntry.isEnabled = true
        }

        checkLog.setOnClickListener {
            val intent = Intent(this, TempActivity::class.java)
            startActivity(intent)
        }
    }
}

