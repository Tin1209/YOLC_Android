package com.example.yolc_kotlin.InF

import com.example.yolc_kotlin.data.Auth
import com.example.yolc_kotlin.data.GetAuth
import retrofit2.Call
import retrofit2.http.*

interface AuthService {
    @FormUrlEncoded
    @POST("auth/")
    fun smsAuth(
        @Field("phone_number") phoneNum: String
    ): Call<Auth>

    @GET("auth/")
    fun getAuth(
        @Query("phone_number") phoneNum:String, @Query("auth_number") authNum:String
    ): Call<GetAuth>
}