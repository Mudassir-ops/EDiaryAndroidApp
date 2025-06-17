package com.example.neweasydairy.fragments.reminderFragment

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
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
import com.example.neweasydairy.alarm.AlarmSchedulerImpl
import com.example.neweasydairy.data.ReminderDao
import com.example.neweasydairy.data.ReminderEntity
import com.example.neweasydairy.utilis.gone
import com.example.neweasydairy.utilis.showDatePickerWithTime
import com.example.neweasydairy.utilis.toast
import com.example.neweasydairy.utilis.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun FragmentReminderBinding?.clickListener(
    activity: Activity,
    reminderFragment: ReminderFragment,
    reminderDao: ReminderDao,
    alarmSchedulerImpl: AlarmSchedulerImpl
) {
    reminderFragment.binding?.apply {
        icSwitchReminder.setOnClickListener {
            val currentState = reminderFragment.reminderViewModel.isToggleClick.value ?: true
            val newState = !currentState
            reminderFragment.reminderViewModel.setReminderToggle(newState)
        }

        txtAddNew.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                setReminder(
                    reminderFragment = reminderFragment,
                    context = activity,
                    reminderDao = reminderDao,
                    alarmSchedulerImpl
                )
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

fun setReminder(
    reminderFragment: ReminderFragment,
    context: Activity,
    reminderDao: ReminderDao,
    alarmSchedulerImpl: AlarmSchedulerImpl
) {
    reminderFragment.showDatePickerWithTime(reminderFragment.calendar) { selectedDate, selectedTime ->
        val reminder = ReminderEntity(
            reminderDate = selectedDate,
            reminderTime = selectedTime,
            description = "Custom reminder description",
            scheduleAt = reminderFragment.calendar.timeInMillis
        )
        CoroutineScope(Dispatchers.IO).launch {
            reminderDao.insertReminder(reminder)
            setReminderAlarm(
                reminderFragment,
                alarmSchedulerImpl,
                reminder
            )
        }
        context.toast("Reminder set for $selectedDate at $selectedTime")
    }
}

fun setReminderAlarm(
    reminderFragment: ReminderFragment,
    alarmSchedulerImpl: AlarmSchedulerImpl,
    reminder: ReminderEntity
) {
    val alarmManager =
        reminderFragment.requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.fromParts("package", reminderFragment.requireContext().packageName, null)
        }
        reminderFragment.startActivity(intent)
        return
    }
    reminder.let {
        alarmSchedulerImpl.schedule(it)
        Log.d("Reminder", "Alarm set for: ${it.scheduleAt}--${alarmManager.canScheduleExactAlarms()}")
    }
}

fun cancelReminderAlarm(
    alarmSchedulerImpl: AlarmSchedulerImpl
) {
    //alarmSchedulerImpl.cancel()
    Log.d("Reminder", "Alarm canceled")
}