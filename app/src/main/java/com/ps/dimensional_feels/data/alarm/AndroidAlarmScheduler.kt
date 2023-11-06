package com.ps.dimensional_feels.data.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.ps.dimensional_feels.alarm.AlarmScheduler
import com.ps.dimensional_feels.util.Constants
import java.time.ZoneId
import java.util.Calendar

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private lateinit var pendingIntent: PendingIntent
    @SuppressLint("MissingPermission")
    override fun schedule(item: AlarmItem) {
        // in Extras here I will have to put all the data needed by for the notification

        cancelPreviousAlarm()

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(Constants.EXTRA_ALARM_MESSAGE, item.message)
            putExtra("HOUR", item.time.hour)
            putExtra("MINUTE", item.time.minute)
        }

        pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, item.time.hour)
            set(Calendar.MINUTE, item.time.minute)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Log.d("lolipop","alarm scheduled for : $calendar" )
    }

    private fun cancelPreviousAlarm(){
        if (::pendingIntent.isInitialized) {
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun cancelAlarm(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}