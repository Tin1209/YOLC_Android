package com.example.yolc_kotlin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class LoginActivity : AppCompatActivity(){
    val TAG = "LOGIN"
    private val BASE_URL = "http://ec2-18-191-209-53.us-east-2.compute.amazonaws.com"
    var isExistBlank = false

    interface LoginService{
        @FormUrlEncoded
        @POST("/login/")
        fun requestLogin(
            @Field("username") userid:String,
            @Field("password") userpw:String
            ) : Call<Login>
    }

    var login:Login? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginService:LoginService = retrofit.create(LoginService::class.java)
        var current = this

        btn_login.setOnClickListener{
            Log.d(TAG,"로그인 버튼 클릭")

            val id = edit_id.text.toString()
            val pw = edit_pw.text.toString()

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

        btn_register.setOnClickListener{
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