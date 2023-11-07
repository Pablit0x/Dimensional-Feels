package com.ps.dimensional_feels.data.alarm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @SuppressLint("MissingPermission")
    fun showNotification(
        notificationTitle: String = "Rick's Reminder 🌀",
        notificationDescription: String = "Rick here! It's time to document your misadventures!",
    ) {
        notificationBuilder.setContentTitle(notificationTitle)
        notificationBuilder.setContentText(notificationDescription)
        notificationBuilder.setSmallIcon(R.drawable.happy_rick)
        notificationManager.notify(
            Constants.DAILY_REMINDER_NOTIFICATION_ID, notificationBuilder.build()
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constants.ALARM_ACTION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val calendar =
                    intent.getParcelableExtra(Constants.ALARM_TIME_EXTRA, Calendar::class.java)
                calendar?.let { scheduledAlarm ->
                    showNotification()
                    val alarmScheduler = AndroidAlarmScheduler(context)
                    val alarmForNextDay = scheduledAlarm.apply { add(Calendar.DATE, 1) }
                    alarmScheduler.schedule(alarmForNextDay)
                }
            }
        }
    }
}