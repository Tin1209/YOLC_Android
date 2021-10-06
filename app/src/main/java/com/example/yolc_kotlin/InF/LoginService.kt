package com.example.yolc_kotlin.InF

import com.example.yolc_kotlin.data.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("/login/")
    fun requestLogin(
        @Field("username") userid:String,
        @Field("password") userpw:String
    ) : Call<Login>
}