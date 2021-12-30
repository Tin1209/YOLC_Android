package com.example.yolc_kotlin

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.InF.LoginService
import com.example.yolc_kotlin.data.Login
import com.example.yolc_kotlin.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity(){
    val TAG = "LOGIN"
    private val BASE_URL = "http://ec2-18-191-209-53.us-east-2.compute.amazonaws.com"

    var isExistBlank = false
    var login: Login? = null


    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, "token: " + msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var loginService: LoginService = retrofit.create(LoginService::class.java)
        var current = this

        binding.btnLogin.setOnClickListener{
            Log.d(TAG,"로그인 버튼 클릭")

            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()

            if(id.isEmpty() || pw.isEmpty()) isExistBlank = true
            if(!isExistBlank){
                loginService.requestLogin(id, pw).enqueue(object: Callback<Login>{
                    override fun onFailure(call:Call<Login>, t:Throwable){
                        Log.e(TAG,"${t.message}")
                        var dialog = AlertDialog.Builder(this@LoginActivity)
                        dialog.setTitle("알림")
                        dialog.setMessage("통신에 실패했습니다")

                        dialogShow(dialog)
                    }
                    override fun onResponse(call:Call<Login>, response: Response<Login>){
                        Log.d(TAG,"통신 성공")
                        var login = response.body()
                        var dialog = AlertDialog.Builder(this@LoginActivity)
                        dialog.setTitle("알림")
                        dialog.setMessage(login?.msg)
                        dialogShow(dialog)

                        if(login?.code == "0000"){
                            startActivity(Intent(current, MainActivity::class.java))
                            finish()
                        }
                    }
                })

            } else{
                Log.d(TAG,"빈칸이 존재합니다.")
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("로그인 실패")
                dialog.setMessage("입력칸을 모두 채워주세요.")

                dialogShow(dialog)
            }

        }

        binding.btnRegister.setOnClickListener{
            Log.d(TAG,"회원가입 버튼 클릭")

            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    fun dialogShow(dialog:AlertDialog.Builder){
        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int){
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG,"다이얼로그")
                }
            }
        }
        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

}