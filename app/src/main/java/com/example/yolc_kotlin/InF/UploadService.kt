package com.example.yolc_kotlin.InF

import com.example.yolc_kotlin.data.Login
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UploadService {

    @Multipart
    @PUT("{username}/{image_id}/uploadImg/")
    fun upload_image(
        @Path("username") user: String,
        @Path("image_id") id: Int,
        @Part img: MultipartBody.Part?
        ): Call<Login>
}