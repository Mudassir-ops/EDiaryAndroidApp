package com.example.neweasydairy.alarm.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.activity.MainActivity
import com.example.neweasydairy.alarm.Constants.ALARM_CHANNEL_NAME
import com.example.neweasydairy.alarm.Constants.ALARM_ID
import com.example.neweasydairy.alarm.Constants.MESSAGE
import com.example.neweasydairy.alarm.Constants.STOP_ALARM
import com.example.neweasydairy.alarm.Constants.TITLE


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val mediaPlayer: MediaPlayer =
            MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer.isLooping = true
        if (intent?.action == STOP_ALARM) {
            val alarmId = intent.getIntExtra(ALARM_ID, 2)
            context?.let { NotificationManagerCompat.from(it).cancel(alarmId) }

            mediaPlayer.release()
            mediaPlayer.stop()

            val pIntent = PendingIntent.getBroadcast(
                context,
                alarmId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager?.cancel(pIntent)
            if (context != null) {
                NotificationManagerCompat.from(context).cancel(alarmId)
            }
            return
        }
        val title = intent?.getStringExtra(TITLE) ?: return
        val message = intent.getStringExtra(MESSAGE)
        val alarmId = intent.getIntExtra(ALARM_ID, 1)
        val goIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 1, goIntent, PendingIntent.FLAG_IMMUTABLE)

        val stopPendingIntent = PendingIntent.getBroadcast(
            context, 1, Intent(context, AlarmReceiver::class.java).apply {
                action = STOP_ALARM
                putExtra(ALARM_ID, alarmId)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = context?.let {
            NotificationCompat.Builder(it, ALARM_CHANNEL_NAME)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .addAction(
                    R.drawable.app_icon, "STOP",
                    stopPendingIntent
                )
        }

        mediaPlayer.start()

        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            builder?.build()?.let { NotificationManagerCompat.from(context).notify(1, it) }
        }
    }

}