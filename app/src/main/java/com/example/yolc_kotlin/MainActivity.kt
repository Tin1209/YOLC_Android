package com.example.yolc_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentTransaction
import com.example.yolc_kotlin.Fragment.FragHome
import com.example.yolc_kotlin.Fragment.FragProfile
import com.example.yolc_kotlin.Fragment.FragRecord
import com.example.yolc_kotlin.databinding.ActivityMainBinding

public class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    var flag = "home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun onNavigationItemSelected(p0: MenuItem): Boolean{
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            when(p0.itemId){
                R.id.home -> {
                    val fragment_home = FragHome()
                    transaction.replace(R.id.main_layout, fragment_home, "home")
                }
                R.id.record ->{
                    val fragment_record = FragRecord()
                    transaction.replace(R.id.main_layout, fragment_record, "record")
                }
                R.id.profile ->{
                    val fragment_profile = FragProfile()
                    transaction.replace(R.id.main_layout, fragment_profile, "profile")
                }
            }
            transaction.addToBackStack(null)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
            return true
        }
    }
}

