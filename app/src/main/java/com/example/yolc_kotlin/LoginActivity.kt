package com.example.yolc_kotlin

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.InF.LoginService
import com.example.yolc_kotlin.data.Login
import com.example.yolc_kotlin.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity(){
    val TAG = "LOGIN"
    val app: url = url()
    val BASE_URL = app.get_url()
    var isExistBlank = false
    var check = false

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(YolcSharedPreferences.getUserId(this).isNullOrBlank() ||
            YolcSharedPreferences.getUserPass(this).isNullOrBlank()) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val loginService: LoginService = retrofit.create(LoginService::class.java)
            val current = this

            binding.btnLogin.setOnClickListener {
                Log.d(TAG, "로그인 버튼 클릭")
                val id = binding.editId.text.toString()
                val pw = binding.editPw.text.toString()

                if (id.isEmpty() || pw.isEmpty()) isExistBlank = true
                if (!isExistBlank) {
                    loginService.requestLogin(id, pw).enqueue(object : Callback<Login> {
                        override fun onFailure(call: Call<Login>, t: Throwable) {
                            Log.e(TAG, "${t.message}")
                            var dialog = AlertDialog.Builder(this@LoginActivity)
                            dialog.setTitle("알림")
                            dialog.setMessage("통신에 실패했습니다")
                            dialogShow(dialog)
                        }
                        override fun onResponse(call: Call<Login>, response: Response<Login>) {
                            Log.d(TAG, "통신 성공")
                            var login = response.body()
                            var dialog = AlertDialog.Builder(this@LoginActivity)
                            Log.d("code", login?.code.toString())
                            if (login?.code == "0000") {
                                YolcSharedPreferences.setAddress(this@LoginActivity, login?.address)
                                YolcSharedPreferences.setUserId(this@LoginActivity, id)
                                if(check) {
                                    YolcSharedPreferences.setUserPass(this@LoginActivity, id)
                                }
                                startActivity(Intent(current, MainActivity::class.java))
                            } else {
                                dialog.setTitle("알림")
                                dialog.setMessage("잘못된 비밀번호이거나 존재하지 않는 ID입니다.")
                                dialogShow(dialog)
                            }
                        }
                    })
                } else {
                    Log.d(TAG, "빈칸이 존재합니다.")
                    val dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("로그인 실패")
                    dialog.setMessage("입력칸을 모두 채워주세요.")
                    dialogShow(dialog)
                }
            }
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnRegister.setOnClickListener{
            Log.d(TAG,"회원가입 버튼 클릭")
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
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

    fun onCheckedboxClicked(view: View){
        if(view is CheckBox){
            val checked: Boolean = view.isChecked
            check = checked
        }
    }
}