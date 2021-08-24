package com.example.yolc_kotlin

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path



interface YOLC_API {
    @POST("/posts/")
    fun post_posts(@Body post: PostItem): Call<PostItem>

    @PATCH("/posts/{pk}/")
    fun patch_posts(@Path("pk") pk: Int, @Body post: PostItem): Call<PostItem>

    @DELETE("/posts/{pk}/")
    fun delete_posts(@Path("pk") pk: Int): Call<PostItem>

    @GET("/posts/")
    fun get_posts(): Call<List<PostItem>>

    @GET("/posts/{pk}/")
    fun get_post_pk(@Path("pk") pk: Int): Call<PostItem>
}