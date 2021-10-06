package com.example.yolc_kotlin.InF

import com.example.yolc_kotlin.data.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterService {
    @FormUrlEncoded
    @POST("signup/")
    fun requestSignUp(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Call<Login>

}