package com.example.neweasydairy.alarm

import com.example.neweasydairy.data.ReminderEntity

interface AlarmScheduler {
    fun schedule(alarm: ReminderEntity)
    fun cancel(alarm: ReminderEntity)
}