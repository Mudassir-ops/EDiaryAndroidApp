package com.example.neweasydairy.fragments.reminderFragment

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.FragmentReminderBinding
import com.example.neweasydairy.activity.MainActivity
import com.example.neweasydairy.data.ReminderDao
import com.example.neweasydairy.data.ReminderEntity
import com.example.neweasydairy.receiver.ReminderReceiver
import com.example.neweasydairy.utilis.gone
import com.example.neweasydairy.utilis.showDatePickerWithTime
import com.example.neweasydairy.utilis.toast
import com.example.neweasydairy.utilis.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

fun FragmentReminderBinding?.clickListener(activity: Activity, reminderFragment: ReminderFragment,reminderDao: ReminderDao) {
    reminderFragment.binding?.apply {
        var isSwitchOn = false
        icSwitchReminder.setOnClickListener {
            isSwitchOn = !isSwitchOn
            if (isSwitchOn) {
                txtAddNew.visible()
                reminderRecyclerView.visible()
                icSwitchReminder.setImageResource(R.drawable.ic_switch_on)
            } else {
                icSwitchReminder.setImageResource(R.drawable.ic_switch_off)
                txtAddNew.gone()
                reminderRecyclerView.gone()
                cancelReminderAlarm(reminderFragment)
            }
        }

        txtAddNew.setOnClickListener {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                setReminder(reminderFragment = reminderFragment, context = activity, reminderDao = reminderDao)
            } else {
                reminderFragment.requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        txtAuto.setOnClickListener {
            reminderFragment.reminderTextDialogBinding?.show()
        }

        icBack.setOnClickListener {
            reminderFragment.findNavController().navigateUp()
        }

    }
}

fun setReminder(reminderFragment: ReminderFragment, context: Activity, reminderDao: ReminderDao) {
    reminderFragment.showDatePickerWithTime(reminderFragment.calendar) { selectedDate, selectedTime ->
        val reminder = ReminderEntity(
            reminderDate = selectedDate,
            reminderTime = selectedTime,
            description = "Custom reminder description"
        )
        CoroutineScope(Dispatchers.IO).launch {
            reminderDao.insertReminder(reminder)
            setReminderAlarm(reminderFragment, reminderFragment.calendar)

        }
        context.toast("Reminder set for $selectedDate at $selectedTime")
    }
}

fun setReminderAlarm(reminderFragment: ReminderFragment, calendar: Calendar) {
    val alarmManager =
        reminderFragment.requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.fromParts("package", reminderFragment.requireContext().packageName, null)
        }
        reminderFragment.startActivity(intent)
        return
    }

    val intent = Intent(reminderFragment.requireContext(), ReminderReceiver::class.java).apply {
        putExtra("OPEN_FRAGMENT", "ReminderFragment")
        putExtra("REMINDER_TITLE", reminderFragment.title)
        putExtra("REMINDER_DESC", reminderFragment.description)

    }

    val pendingIntent = PendingIntent.getBroadcast(
        reminderFragment.requireContext(),
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    Log.d("Reminder", "Alarm set for: ${calendar.time}")
}

fun cancelReminderAlarm(reminderFragment: ReminderFragment) {
    val alarmManager =
        reminderFragment.requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(reminderFragment.requireContext(), MainActivity::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        reminderFragment.requireContext(),
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.cancel(pendingIntent)
    Log.d("Reminder", "Alarm canceled")
}