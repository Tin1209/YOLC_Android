package com.example.yolc_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

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

