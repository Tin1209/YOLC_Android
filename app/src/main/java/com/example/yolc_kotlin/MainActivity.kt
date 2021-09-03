package com.example.yolc_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.yolc_kotlin.InF.YOLC_API
import com.example.yolc_kotlin.data.PostItem
import retrofit2.*

import retrofit2.converter.gson.GsonConverterFactory

public class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = MainActivity::class.java.simpleName
    private val BASE_URL = "http://ec2-18-191-209-53.us-east-2.compute.amazonaws.com"

    private var yAPI: YOLC_API? = null

    private var mListTv: TextView? = null


    private var mGetButton: Button? = null
    private var mPostButton: Button? = null
    private var mPatchButton: Button? = null
    private var mDeleteButton: Button? = null
    private var moveLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListTv = findViewById(R.id.result1)

        mGetButton = findViewById(R.id.button1)
        mGetButton?.setOnClickListener(this)
        mPostButton = findViewById(R.id.button2)
        mPostButton?.setOnClickListener(this)
        mPatchButton = findViewById(R.id.button3)
        mPatchButton?.setOnClickListener(this)
        mDeleteButton = findViewById(R.id.button4)
        mDeleteButton?.setOnClickListener(this)

        moveLogin = findViewById(R.id.login)
        moveLogin?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        initAPI(BASE_URL)
    }

    private fun initAPI(baseUrl: String){
        Log.d(TAG,"initAPI : " + baseUrl)
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        yAPI = retrofit.create(YOLC_API::class.java)
    }

    override fun onClick(v: View){
        if(v == mGetButton){
            Log.d(TAG,"GET")
            val getCall: Call<List<PostItem>>? = yAPI?.get_posts()
            if(getCall != null) {
                getCall.enqueue(object : Callback<List<PostItem>> {
                    override fun onResponse(
                        call: Call<List<PostItem>>,
                        response: Response<List<PostItem>>
                    ) {
                        if (response.isSuccessful()) {
                            val mList: List<PostItem>? = response.body()
                            var result: String = ""
                            if (mList != null) {
                                for (item: PostItem in mList) {
                                    result += "title: " + item.getTitle() + "text: " + item.getText() + "\n"
                                }
                                mListTv?.setText(result)
                            } else {
                                Log.d(TAG, "Status code : " + response.code())
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<PostItem>>, t: Throwable) {
                        Log.d(TAG, "Fail msg : " + t.message)
                    }
                })
            }
        } else if(v == mPostButton){
            Log.d(TAG,"POST")

            val item: PostItem = PostItem()
            item.setText("Android text")
            item.setTitle("Android title")
            val postCall:Call<PostItem>? = yAPI?.post_posts(item)
            if (postCall != null) {
                postCall.enqueue(object: Callback<PostItem>{
                    override fun onResponse(call:Call<PostItem>, response:Response<PostItem>){
                        if(response.isSuccessful()){
                            Log.d(TAG,"등록 완료")
                        } else {
                            Log.d(TAG,"Status Code : " + response.code())
                            Log.d(TAG,response.errorBody().toString())
                            Log.d(TAG,call.request().body().toString())
                        }
                    }

                    override fun onFailure(call:Call<PostItem>, t:Throwable){
                        Log.d(TAG,"Fail msg : " + t.message)
                    }
                })
            }
        } else if(v == mPatchButton){
            Log.d(TAG,"PATCH")
            val item: PostItem = PostItem()
            item.setTitle("android patch title")
            item.setText("android patch text")
            val patchCall: Call<PostItem>? = yAPI?.patch_posts(1,item)
            if (patchCall != null) {
                patchCall.enqueue(object:Callback<PostItem>{
                    override fun onResponse(call:Call<PostItem>, response:Response<PostItem>){
                        if(response.isSuccessful()){
                            Log.d(TAG,"성공")
                        } else{
                            Log.d(TAG,"Status Code: " + response.body())
                        }
                    }

                    override fun onFailure(call: Call<PostItem>, t: Throwable) {
                        Log.d(TAG,"Fail msg : " + t.message)
                    }
                })
            }
        } else if(v == mDeleteButton){
            Log.d(TAG,"DELETE")
            val deleteCall:Call<PostItem>? = yAPI?.delete_posts(2)
            if (deleteCall != null) {
                deleteCall.enqueue(object:Callback<PostItem>{
                    override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
                        if(response.isSuccessful()){
                            Log.d(TAG,"삭제 완료")

                        } else{
                            Log.d(TAG,"Status Code : " + response.code())
                        }
                    }

                    override fun onFailure(call: Call<PostItem>, t: Throwable) {
                        Log.d(TAG,"Fail msg : " + t.message)
                    }
                })
            }
        }
    }
}

