package com.example.yolc_kotlin

import YolcSharedPreferences
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.InF.ShowService
import com.example.yolc_kotlin.InF.UploadService
import com.example.yolc_kotlin.data.ImageData
import com.example.yolc_kotlin.data.Login
import com.example.yolc_kotlin.databinding.ActivityEditProfileBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class EditProfileActivity: AppCompatActivity() {
    val app: url = url()
    private val BASE_URL = app.get_url()
    private lateinit var uploadImg: Bitmap
    private var currentImageURL: Uri? = null
    private var originImg: Bitmap? = null
    private var out: FileOutputStream? = null
    private lateinit var file: File

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        Log.d("getContent", "in")
        currentImageURL = result.data?.data
        Log.d("ActivityResult", currentImageURL.toString())
        val ins: InputStream? = currentImageURL?.let{
            applicationContext.contentResolver.openInputStream(it)
        }
        file = File(currentImageURL.toString())
        uploadImg = BitmapFactory.decodeStream(ins)
        originImg = uploadImg
        ins?.close()
        uploadImage(2)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val bind = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(bind.root)
        for(i in 1..6){
            when(i){
                1 -> {
                    thread(start = true) {
                        val userId = YolcSharedPreferences.getUserId(this)
                        val url = URL("http://18.119.47.187/${userId}/${1}/showImg/")
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.doInput
                        conn.connect()

                        val input: InputStream = conn.inputStream
                        originImg = BitmapFactory.decodeStream(input)
                        Log.d("thread", "success")

                    }
                    bind.image1.setImageBitmap(originImg)
                    Log.d("showImage", "success")
                    originImg = null
                }
                2 -> {
                    thread(start = true) {
                        val userId = YolcSharedPreferences.getUserId(this)
                        val url = URL("http://18.119.47.187/${userId}/${2}/showImg/")
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.doInput
                        conn.connect()

                        val input: InputStream = conn.inputStream
                        originImg = BitmapFactory.decodeStream(input)
                        Log.d("thread", "success")

                    }
                    bind.image2.setImageBitmap(originImg)
                    Log.d("showImage", "success")
                    originImg = null
                }
                3 -> {
                    thread(start = true) {
                        val userId = YolcSharedPreferences.getUserId(this)
                        val url = URL("http://18.119.47.187/${userId}/${3}/showImg/")
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.doInput
                        conn.connect()

                        val input: InputStream = conn.inputStream
                        originImg = BitmapFactory.decodeStream(input)
                        Log.d("thread", "success")

                    }
                    bind.image3.setImageBitmap(originImg)
                    Log.d("showImage", "success")
                    originImg = null
                }
                4 -> {
                    thread(start = true) {
                        val userId = YolcSharedPreferences.getUserId(this)
                        val url = URL("http://18.119.47.187/${userId}/${4}/showImg/")
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.doInput
                        conn.connect()

                        val input: InputStream = conn.inputStream
                        originImg = BitmapFactory.decodeStream(input)
                        Log.d("thread", "success")

                    }
                    bind.image4.setImageBitmap(originImg)
                    Log.d("showImage", "success")
                    originImg = null
                }
                5 -> {
                    thread(start = true) {
                        val userId = YolcSharedPreferences.getUserId(this)
                        val url = URL("http://18.119.47.187/${userId}/${5}/showImg/")
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.doInput
                        conn.connect()

                        val input: InputStream = conn.inputStream
                        originImg = BitmapFactory.decodeStream(input)
                        Log.d("thread", "success")

                    }
                    bind.image5.setImageBitmap(originImg)
                    Log.d("showImage", "success")
                    originImg = null
                }
                6 -> {
                    thread(start = true) {
                        val userId = YolcSharedPreferences.getUserId(this)
                        val url = URL("http://18.119.47.187/${userId}/${6}/showImg/")
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.doInput
                        conn.connect()

                        val input: InputStream = conn.inputStream
                        originImg = BitmapFactory.decodeStream(input)
                        Log.d("thread", "success")

                    }
                    bind.image6.setImageResource(R.drawable.ic_push)
                    Log.d("showImage", "success")
                    originImg = null
                }
            }
            Log.d("when", "finish")
        }

        bind.editImage1.setOnClickListener{
            selectMenu(1)
        }

        bind.editImage2.setOnClickListener{
            selectMenu(2)
        }

        bind.editImage3.setOnClickListener{
            selectMenu(3)
        }

        bind.editImage4.setOnClickListener{
            selectMenu(4)
        }

        bind.editImage5.setOnClickListener{
            selectMenu(5)
        }

        bind.editImage6.setOnClickListener{
            selectMenu(6)
        }
    }

    private fun selectGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        getContent.launch(intent)
    }

    private fun uploadImage(id: Int){
        Log.d("uploadImage", "in")
        val userId = YolcSharedPreferences.getUserId(this)
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var uploadService: UploadService = retrofit.create(UploadService::class.java)
        file.createNewFile()
        val out = FileOutputStream(file)
        originImg?.compress(CompressFormat.JPEG,100,out)
        val reqImg = file.asRequestBody("image/*".toMediaType())
        val filename = "image${id}"
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file",filename,reqImg)
        uploadService.upload_image(user=userId,id=id,img=body).enqueue(object: Callback<Login> {
            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e("UploadImage", "${t.message}")
            }
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                Log.d("UploadImage", "통신 성공")
                var login = response.body()
                Log.d("UploadImage", login?.code.toString())
                showImage(id)
            }
        })
    }

    private fun showImage(id: Int){
        thread(start = true) {
            val userId = YolcSharedPreferences.getUserId(this)
            val url = URL("http://18.119.47.187/${userId}/${id}/showImg/")
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.doInput
            conn.connect()

            val input: InputStream = conn.inputStream
            originImg = BitmapFactory.decodeStream(input)
            Log.d("thread", "success")

        }
    }

    private fun selectMenu(id: Int){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle(" ")
        mBuilder.show()

        val edit = mDialogView.findViewById<Button>(R.id.edit_image)
        val remove = mDialogView.findViewById<Button>(R.id.remove_image)
        edit.setOnClickListener{
            selectGallery()
//            uploadImage(id)
        }
    }

    inner class BitmapRequestBody(private val bitmap: Bitmap): RequestBody(){
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink){
            bitmap.compress(CompressFormat.JPEG,99,sink.outputStream())
        }
    }
}