package com.example.yolc_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.yolc_kotlin.Fragment.FragHome
import com.example.yolc_kotlin.Fragment.FragProfile
import com.example.yolc_kotlin.Fragment.FragRecord
import com.example.yolc_kotlin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

public class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    private val f1: FrameLayout by lazy{
        binding.mainLayout
    }
    private val bn: BottomNavigationView by lazy{
        binding.bottomNavigationView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(f1.id, FragHome()).commit()

        bn.run{
            setOnItemSelectedListener{item->
                replaceFragment(
                    when(item.itemId){
                        R.id.home -> FragHome()
                        R.id.record -> FragRecord()
                        R.id.profile -> FragProfile()
                        else -> FragHome()
                    }
                )
                true
            }
        }
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(f1.id, fragment).commit()
    }
}

