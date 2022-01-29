package com.example.yolc_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.yolc_kotlin.Fragment.FragHome
import com.example.yolc_kotlin.Fragment.FragProfile
import com.example.yolc_kotlin.Fragment.FragRecord
import com.example.yolc_kotlin.InF.NotificationService
import com.example.yolc_kotlin.data.Login
import com.example.yolc_kotlin.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    private val f1: FrameLayout by lazy{
        binding.mainLayout
    }
    private val bn: BottomNavigationView by lazy{
        binding.bottomNavigationView
    }
    private val app: url = url()
    private val BASE_URL = app.get_url()

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = YolcSharedPreferences.getUserId(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if(!task.isSuccessful){
                Log.w("fail!", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            YolcSharedPreferences.setToken(this, token)
            val address = YolcSharedPreferences.getAddress(this)
            Log.d("token", token)
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("success", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val notificationService: NotificationService = retrofit.create(NotificationService::class.java)
            notificationService.sendToken(user=userId,token=token,address=address).enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable){
                    Log.e("fail", "${t.message}")
                }
                override fun onResponse(call: Call<Login>, response: Response<Login>){
                    Log.d("success", response.body()?.code.toString())
                    Toast.makeText(baseContext,"token",Toast.LENGTH_SHORT).show()
                }
            })
            Log.i("Log: ", "success to saving token")
        })

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

    fun goEditProfile(){
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(f1.id, fragment).commit()
    }
}

