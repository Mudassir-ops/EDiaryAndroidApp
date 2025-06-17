package com.example.neweasydairy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.neweasydairy.alarm.Constants.ALARM_ID
import com.example.neweasydairy.alarm.Constants.MESSAGE
import com.example.neweasydairy.alarm.Constants.TITLE
import com.example.neweasydairy.alarm.receiver.AlarmReceiver
import com.example.neweasydairy.data.ReminderEntity
import com.google.gson.Gson

class AlarmSchedulerImpl(private val context: Context) : AlarmScheduler {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(alarm: ReminderEntity) {
        Log.d("scheduleSatti", "schedule: ${Gson().toJson(alarm)}")
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.hashCode(),
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(TITLE, alarm.reminderTime)
                putExtra(MESSAGE, alarm.description)
                putExtra(ALARM_ID, alarm.id)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val alarmClockInfo = AlarmManager.AlarmClockInfo(alarm.scheduleAt, pendingIntent)
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarm.scheduleAt,
                pendingIntent
            )
        }
    }

    override fun cancel(alarm: ReminderEntity) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

}