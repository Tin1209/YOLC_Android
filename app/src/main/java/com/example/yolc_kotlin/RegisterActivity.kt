package com.example.yolc_kotlin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.InF.AuthService
import com.example.yolc_kotlin.InF.RegisterService
import com.example.yolc_kotlin.data.Auth
import com.example.yolc_kotlin.data.GetAuth
import com.example.yolc_kotlin.data.Login
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    val TAG: String = "Register"
    private val BASE_URL = "http://ec2-18-191-209-53.us-east-2.compute.amazonaws.com"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val registerService: RegisterService = retrofit.create(RegisterService::class.java)
        val authService: AuthService = retrofit.create(AuthService::class.java)
        var checkAuth = false

        sendSms.setOnClickListener{
            Log.d(TAG,"인증번호 전송 버튼 클릭")
            val phone_number = textPhone.text.toString()
            if(phone_number.length == 13) {
                authService.smsAuth(phone_number).enqueue(object : Callback<Auth> {
                    override fun onFailure(call: Call<Auth>, t: Throwable) {
                        Log.e("AUTH", "${t.message}")
                        var dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("알림")
                        dialog.setMessage("통신에 실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                        var auth = response.body()
                        Log.d(TAG, auth.toString())
                    }
                })
            }
        }

        phoneAuth.setOnClickListener{
            val auth_number = smsAuth.text.toString()
            val phone_number = textPhone.text.toString()
            if(phone_number.length == 13 && auth_number.length == 4) {
                authService.getAuth(phoneNum = phone_number, authNum = auth_number)
                    .enqueue(object : Callback<GetAuth> {
                        override fun onFailure(call: Call<GetAuth>, t: Throwable) {
                            Log.e("GetAuth", "${t.message}")
                            var dialog = AlertDialog.Builder(this@RegisterActivity)
                            dialog.setTitle("알림")
                            dialog.setMessage("통신에 실패했습니다")
                            dialog.show()
                        }

                        override fun onResponse(call: Call<GetAuth>, response: Response<GetAuth>) {
                            val res = response.body()
                            if (res?.result == true) {
                                checkAuth = true
                                Log.d(TAG, res.result.toString())
                                dialogShow("auth success")
                            }
                        }
                    })
            }
        }

        btn_register2.setOnClickListener{
            Log.d(TAG,"회원가입 버튼 클릭")

            var isExistBlank = false
            var isPWSame = false
            var isExistID = false

            val id = edit_id.text.toString()
            val pw = edit_pw.text.toString()
            val pw_re = edit_pw_re.text.toString()

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
                    if(checkAuth) {
                        dialogShow("success")
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else{
                        dialogShow("not auth")
                    }
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
        } else if(type.equals("not auth")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("휴대폰 인증을 완료해주세요")
        } else if(type.equals("success")){
            dialog.setTitle("회원가입 성공!")
            dialog.setMessage("회원가입에 성공하였습니다!")
        } else if(type.equals("auth success")){
            dialog.setTitle("인증 성공")
            dialog.setMessage("인증이 완료되었습니다")
        }
        val dialog_listener = object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }
        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }


}