package com.ps.dimensional_feels.data.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ps.dimensional_feels.alarm.AlarmScheduler
import com.ps.dimensional_feels.util.Constants
import java.time.LocalTime
import java.util.Calendar

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private lateinit var pendingIntent: PendingIntent
    @SuppressLint("MissingPermission")
    override fun schedule(time: LocalTime) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.ALARM_ACTION
        }

        pendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.ALARM_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
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