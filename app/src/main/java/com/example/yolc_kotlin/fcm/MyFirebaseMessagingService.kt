package com.example.yolc_kotlin.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.yolc_kotlin.MainActivity
import com.example.yolc_kotlin.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessaging

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        Log.d(TAG, "Refreshed token: $p0")

        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", p0).apply()
        editor.commit()

        Log.i("Log: ", "success to saving token")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d(TAG, "From: " + p0!!.from)

        if (p0.data.isNotEmpty()) {
            Log.i("Body: ", p0.data["body"].toString())
            Log.i("Title: ", p0.data["title"].toString())
            sendNotification(p0)
        } else {
            Log.i("error: ", "data is empty.")
            Log.i("data: ", p0.data.toString())
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.firebase_notification_channel_id)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.data["body"].toString())
            .setContentText(remoteMessage.data["title"].toString())
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)

        }

        notificationManager.notify(uniId, notificationBuilder.build())
    }
}