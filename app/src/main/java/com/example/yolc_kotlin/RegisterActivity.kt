package com.example.yolc_kotlin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.InF.AuthService
import com.example.yolc_kotlin.InF.RegisterService
import com.example.yolc_kotlin.data.Auth
import com.example.yolc_kotlin.data.GetAuth
import com.example.yolc_kotlin.data.Login
import com.example.yolc_kotlin.databinding.ActivityRegistrationBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    val TAG: String = "Register"
    val app: url = url()
    val BASE_URL = app.get_url()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val registerService: RegisterService = retrofit.create(RegisterService::class.java)
        val authService: AuthService = retrofit.create(AuthService::class.java)
        var checkAuth = false

        binding.sendSms.setOnClickListener{
            Log.d(TAG,"인증번호 전송 버튼 클릭")
            val phoneNumber = binding.textPhone.text.toString()
            Log.d(TAG,phoneNumber)
            Log.d(TAG,phoneNumber.length.toString())
            if(phoneNumber.length == 11) {
                Log.d(TAG,"번호입력 성공!")
                authService.smsAuth(phoneNumber).enqueue(object : Callback<Auth> {
                    override fun onFailure(call: Call<Auth>, t: Throwable) {
                        Log.e("AUTH", "${t.message}")
                        var dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("알림")
                        dialog.setMessage("통신에 실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                        Log.d(TAG,"요청 성공!")
                        var auth = response.body()
                        Log.d(TAG, auth.toString())
                    }
                })
            }else{
                Log.d(TAG,"번호 오류")
            }
        }
        binding.phoneAuth.setOnClickListener{
            val authNumber = binding.smsAuth.text.toString()
            val phoneNumber = binding.textPhone.text.toString()
            if(phoneNumber.length == 11 && authNumber.length == 4) {
                authService.getAuth(phoneNum = phoneNumber, authNum = authNumber)
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
            else{
                val log = "fail"
            }
        }
        binding.btnRegister2.setOnClickListener{
            Log.d(TAG,"회원가입 버튼 클릭")

            var isExistBlank = false
            var isPWSame = false
            var isExistID = false

            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()
            val pw_re = binding.editPwRe.text.toString()
            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            val phone_number = binding.textPhone.text.toString()

            if(id.isEmpty() || pw.isEmpty() || pw_re.isEmpty() || name.isEmpty()){
                isExistBlank = true
            } else{
                if(pw == pw_re){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame){
                registerService.requestSignUp(username=id, password=pw, name=name, address=address, phone_number=phone_number)
                    .enqueue(object: Callback<Login>{
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
        binding.QR.setOnClickListener{
            IntentIntegrator(this).initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                // todo
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show()
                try{
                    val obj: JSONObject =  JSONObject(result.contents)
                } catch(e: JSONException){
                    e.printStackTrace()
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun dialogShow(type: String){
        val dialog = AlertDialog.Builder(this)
        when (type) {
            "blank" -> {
                dialog.setTitle("회원가입 실패")
                dialog.setMessage("입력란을 모두 작성해주세요")
            }
            "not same" -> {
                dialog.setTitle("회원가입 실패")
                dialog.setMessage("비밀번호가 일치하지 않습니다.")
            }
            "Exist ID" -> {
                dialog.setTitle("회원가입 실패")
                dialog.setMessage("이미 존재하는 ID 입니다.")
            }
            "not auth" -> {
                dialog.setTitle("회원가입 실패")
                dialog.setMessage("휴대폰 인증을 완료해주세요")
            }
            "success" -> {
                dialog.setTitle("회원가입 성공!")
                dialog.setMessage("회원가입에 성공하였습니다!")
            }
            "auth success" -> {
                dialog.setTitle("인증 성공")
                dialog.setMessage("인증이 완료되었습니다")
            }
        }
        val dialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE ->
                    Log.d(TAG, "다이얼로그")
            }
        }
        dialog.setPositiveButton("확인",dialogListener)
        dialog.show()
    }


}