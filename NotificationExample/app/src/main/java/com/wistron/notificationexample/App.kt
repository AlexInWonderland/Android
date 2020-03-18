package com.wistron.notificationexample

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class App : Application() {
    companion object{
        const val CHANNEL_ID_1:String = "channel1"
        const val CHANNEL_ID_2:String = "channel2"
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels();
    }

    fun createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel1:NotificationChannel = NotificationChannel(
                CHANNEL_ID_1,
                "Channel 1",
                 NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is channel 1"

            var channel2:NotificationChannel = NotificationChannel(
                CHANNEL_ID_2,
                "Channel 2",
                NotificationManager.IMPORTANCE_LOW
            )
            channel1.description = "This is channel 2"
            var manager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)

        }
    }
}