package com.example.neweasydairy.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.activity.MainActivity

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("REMINDER_TITLE") ?: "Reminder"
        val description = intent.getStringExtra("REMINDER_DESC") ?: "You have a reminder."
        Log.d("ReminderReceiver", "Notification clicked! Title: $title, Description: $description")
        showNotification(context, title, description,intent)
    }
    private fun showNotification(context: Context, title: String, description: String,intent: Intent) {
        val channelId = "REMINDER_CHANNEL"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH).apply { setDescription("Reminder notifications") }
            notificationManager.createNotificationChannel(channel)
        }
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("REMINDER_TITLE", title)
            putExtra("REMINDER_DESC", description)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notificationManager.notify(0, notificationBuilder.build())
    }
}


