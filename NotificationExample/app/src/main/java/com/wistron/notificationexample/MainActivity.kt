package com.wistron.notificationexample

import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    lateinit var  manager:NotificationManagerCompat
    lateinit var et_title:EditText
    lateinit var et_message:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = NotificationManagerCompat.from(this)
        et_message = findViewById(R.id.edit_message)
        et_title = findViewById(R.id.edit_title)
    }

    fun sendOnChannel1(v: View){
        var title:String = et_title.text.toString()
        var msg:String = et_message.text.toString()
        var notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_ID_1)
                                         .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()
        manager.notify(1, notification)
    }

    fun sendOnChannel2(v: View){
        var title:String = et_title.text.toString()
        var msg:String = et_message.text.toString()
        var notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_ID_2)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        manager.notify(2, notification)
    }

}
