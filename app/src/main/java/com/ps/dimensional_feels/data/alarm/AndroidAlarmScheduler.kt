package com.ps.dimensional_feels.data.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ps.dimensional_feels.alarm.AlarmScheduler
import com.ps.dimensional_feels.util.Constants
import java.util.Calendar
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private lateinit var pendingIntent: PendingIntent

    @SuppressLint("MissingPermission")
    override fun schedule(calendar: Calendar) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.ALARM_ACTION
            putExtra(Constants.ALARM_TIME_EXTRA, calendar)
        }

        pendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.ALARM_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    override fun cancelAlarm() {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                Constants.ALARM_CODE,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}