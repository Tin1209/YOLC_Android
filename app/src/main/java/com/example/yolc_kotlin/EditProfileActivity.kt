package com.example.yolc_kotlin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.yolc_kotlin.databinding.ActivityEditProfileBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream


class EditProfileActivity: AppCompatActivity() {

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        currentImageURL = result.data?.data
        Log.d("ActivityResult", currentImageURL.toString())
        val ins: InputStream? = currentImageURL?.let{
            applicationContext.contentResolver.openInputStream(it)
        }
        val img: Bitmap = BitmapFactory.decodeStream(ins)
        ins?.close()
        val resized = Bitmap.createScaledBitmap(img,1024,1024,true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        profileImageBase64 = Base64.encodeToString(byteArray, NO_WRAP)
        Log.d("ActivityResult", "success")
        Log.d("encode Result", profileImageBase64)
        try{

        }catch(e:Exception){
            e.printStackTrace()
        }
    }
    lateinit var profileImageBase64: String
    var currentImageURL: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val bind = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.editImage1.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(" ")
            mBuilder.show()

            val edit = mDialogView.findViewById<Button>(R.id.edit_image)
            val remove = mDialogView.findViewById<Button>(R.id.remove_image)
            edit.setOnClickListener{
                selectGallery()

            }
        }

        bind.editImage2.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(" ")
            mBuilder.show()

            val edit = mDialogView.findViewById<Button>(R.id.edit_image)
            val remove = mDialogView.findViewById<Button>(R.id.remove_image)
        }

        bind.editImage3.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(" ")
            mBuilder.show()

            val edit = mDialogView.findViewById<Button>(R.id.edit_image)
            val remove = mDialogView.findViewById<Button>(R.id.remove_image)
        }

        bind.editImage4.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(" ")
            mBuilder.show()

            val edit = mDialogView.findViewById<Button>(R.id.edit_image)
            val remove = mDialogView.findViewById<Button>(R.id.remove_image)
        }

        bind.editImage5.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(" ")
            mBuilder.show()

            val edit = mDialogView.findViewById<Button>(R.id.edit_image)
            val remove = mDialogView.findViewById<Button>(R.id.remove_image)
        }

        bind.editImage6.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(" ")
            mBuilder.show()

            val edit = mDialogView.findViewById<Button>(R.id.edit_image)
            val remove = mDialogView.findViewById<Button>(R.id.remove_image)
        }
    }

    private fun bitmapToString(bitmap: Bitmap): String{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 300, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun stringToBitmap(encodedString: String): Bitmap{
        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }

    private fun selectGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        getContent.launch(intent)
    }
}