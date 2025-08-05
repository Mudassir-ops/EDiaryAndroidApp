package com.example.neweasydairy.applicationClass

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.alarm.Constants.ALARM_CHANNEL_NAME
import com.example.neweasydairy.usecase.LogFirebaseEventUseCase
import com.example.neweasydairy.utilis.AppEventLogger
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var logEventUseCase: LogFirebaseEventUseCase

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.reminder)
            val channelDescription = getString(R.string.reminder_channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(ALARM_CHANNEL_NAME, name, importance)
            mChannel.description = channelDescription
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
        Log.d("FirebaseCheck", "Firebase initialized: ${FirebaseApp.getApps(this).isNotEmpty()}")
        AppEventLogger.init(useCase = logEventUseCase)
    }

}