package com.example.yolc_kotlin

import YolcSharedPreferences
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.InF.UploadService
import com.example.yolc_kotlin.data.Login
import com.example.yolc_kotlin.databinding.ActivityEditProfileBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*

class EditProfileActivity: AppCompatActivity() {
    val app: url = url()
    private val BASE_URL = app.get_url()
    private lateinit var userId: String
    private lateinit var uploadImg: Bitmap
    private lateinit var imgDir: String
    private var currentImageURL: Uri? = null
    private var curId: Int = 0
    private lateinit var img1: WebView
    private lateinit var img2: WebView
    private lateinit var img3: WebView
    private lateinit var img4: WebView
    private lateinit var img5: WebView
    private lateinit var img6: WebView
    var test = false

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        Log.d("getContent", "in")
        test = true
        currentImageURL = result.data?.data
        Log.d("ActivityResult", currentImageURL.toString())
        val ins: InputStream? = currentImageURL?.let{
            applicationContext.contentResolver.openInputStream(it)
        }
        uploadImg = BitmapFactory.decodeStream(ins)
        ins?.close()
        uploadImage(curId)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val bind = ActivityEditProfileBinding.inflate(layoutInflater)
        userId = YolcSharedPreferences.getUserId(this)
        setContentView(bind.root)
        img1 = bind.profile1
        img2 = bind.profile2
        img3 = bind.profile3
        img4 = bind.profile4
        img5 = bind.profile5
        img6 = bind.profile6
        initWebView(img1,"1")
        initWebView(img2,"2")
        initWebView(img3,"3")
        initWebView(img4,"4")
        initWebView(img5,"5")
        initWebView(img6,"6")
        bind.editImage1.setOnClickListener{
            curId = 1
            selectMenu()
        }
        bind.editImage2.setOnClickListener{
            curId = 2
            selectMenu()
        }
        bind.editImage3.setOnClickListener{
            curId = 3
            selectMenu()
        }
        bind.editImage4.setOnClickListener{
            curId = 4
            selectMenu()
        }
        bind.editImage5.setOnClickListener{
            curId = 5
            selectMenu()
        }
        bind.editImage6.setOnClickListener{
            curId = 6
            selectMenu()
        }
    }
    private fun uploadImage(id: Int){
        Log.d("uploadImage", "in")
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var uploadService: UploadService = retrofit.create(UploadService::class.java)
        val filename = "image${id}"
        val photoDir = File(cacheDir.absolutePath + "/cameraphoto/")
        if(!photoDir.exists()){
            photoDir.mkdirs()
        }
        val imgFile = File(photoDir, "${filename}.jpg")
        val fo: FileOutputStream
        try{
            imgFile.createNewFile()
            fo = FileOutputStream(imgFile)
            uploadImg.compress(CompressFormat.JPEG,100,fo)
            fo.close()
        } catch(e: FileNotFoundException){
            e.printStackTrace()
        } catch(e: IOException){
            e.printStackTrace()
        }
        imgDir = cacheDir.absolutePath + "/cameraphoto/" + "${filename}.jpg"
        val uploadFile = File(imgDir)
        Log.d("file", uploadFile.isFile.toString())
        val reqImg = uploadFile.asRequestBody("image/jpg".toMediaType())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("img",filename,reqImg)
        uploadService.upload_image(user=userId,id=id,img=body).enqueue(object: Callback<Login> {
            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e("UploadImage", "${t.message}")
            }
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                Log.d("UploadImage", "통신 성공")
                var login = response.body()
                Log.d("UploadImage", login?.code.toString())
                when(curId){
                    1 -> {img1.loadUrl("$BASE_URL$userId/1/showImg")}
                    2 -> {img2.loadUrl("$BASE_URL$userId/2/showImg")}
                    3 -> {img3.loadUrl("$BASE_URL$userId/3/showImg")}
                    4 -> {img4.loadUrl("$BASE_URL$userId/4/showImg")}
                    5 -> {img5.loadUrl("$BASE_URL$userId/5/showImg")}
                    6 -> {img6.loadUrl("$BASE_URL$userId/6/showImg")}
                }
            }
        })
    }
    private fun selectMenu(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle(" ")
        mBuilder.show()
        val edit = mDialogView.findViewById<Button>(R.id.edit_image)
        val remove = mDialogView.findViewById<Button>(R.id.remove_image)
        edit.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            getContent.launch(intent)
        }
    }
    private fun initWebView(wView: WebView, imageId: String){
        val ws = wView.settings
        ws.setSupportMultipleWindows(false)
        ws.useWideViewPort
        ws.loadWithOverviewMode
        ws.javaScriptEnabled
        ws.domStorageEnabled
        wView.webViewClient = WebViewClient()
        wView.loadUrl("$BASE_URL$userId/$imageId/showImg/")
    }
}