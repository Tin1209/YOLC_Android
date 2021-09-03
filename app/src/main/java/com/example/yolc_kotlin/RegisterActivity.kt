package com.example.yolc_kotlin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.data.Login
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class RegisterActivity : AppCompatActivity() {

    val TAG: String = "Register"
    private val BASE_URL = "http://ec2-18-191-209-53.us-east-2.compute.amazonaws.com"

    interface RegisterService{
        @FormUrlEncoded
        @POST("signup/")
        fun requestSignUp(
            @Field("username") username: String,
            @Field("password") password: String
        ) : Call<Login>
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val intent = Intent(this, LoginActivity::class.java)

        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var registerService: RegisterService = retrofit.create(RegisterService::class.java)

        btn_register2.setOnClickListener{
            Log.d(TAG,"회원가입 버튼 클릭")

            var isExistBlank = false
            var isPWSame = false
            var isExistID = false

            val id = edit_id.text.toString()
            val pw = edit_pw.text.toString()
            val pw_re = edit_pw_re.text.toString()

            Log.d(TAG,id)
            Log.d(TAG,pw)
            Log.d(TAG,pw_re)

            if(id.isEmpty() || pw.isEmpty() || pw_re.isEmpty()){
                isExistBlank = true
            } else{
                if(pw == pw_re){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame){
                registerService.requestSignUp(id, pw).enqueue(object: Callback<Login>{
                    override fun onFailure(call: Call<Login>, t:Throwable){

                        Log.e("SIGNUP", "${t.message}")
                        var dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("알림")
                        dialog.setMessage("통신에 실패했습니다.")
                        dialog.show()
                    }
                    override fun onResponse(call: Call<Login>, response: Response<Login>){
                        var login = response.body()

                        if(login?.code == "1002"){
                            Log.d(TAG,"이미 존재하는 ID입니다.")
                            isExistID = true
                        }
                    }
                })

                if(!isExistID) {
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                    val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)

                    val editor = sharedPreference.edit()
                    editor.putString("id", id)
                    editor.putString("pw", pw)
                    editor.apply()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else dialogShow("Exist ID")

            } else{

                if(isExistBlank){
                    dialogShow("blank")
                } else if(!isPWSame){
                    dialogShow("not same")
                }
            }
        }
    }

    fun dialogShow(type: String){
        val dialog = AlertDialog.Builder(this)

        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        } else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 일치하지 않습니다.")
        } else if(type.equals("Exist ID")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("이미 존재하는 ID 입니다.")
        }

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