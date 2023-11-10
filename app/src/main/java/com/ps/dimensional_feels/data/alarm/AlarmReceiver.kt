package com.ps.dimensional_feels.data.alarm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ps.dimensional_feels.alarm.AlarmScheduler
import com.ps.dimensional_feels.util.Constants
import com.ps.dimensional_feels.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @SuppressLint("MissingPermission")
    fun showNotification(
        notificationTitle: String = "Rick's Reminder 🌀",
        notificationDescription: String = "Rick here! It's time to document your misadventures!",
    ) {
        notificationBuilder.setContentTitle(notificationTitle)
        notificationBuilder.setContentText(notificationDescription)
        notificationManager.notify(
            Constants.DAILY_REMINDER_NOTIFICATION_ID, notificationBuilder.build()
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constants.ALARM_ACTION) {
            val isReminderStillEnabled =
                preferencesManager.getBoolean(Constants.IS_DAILY_REMINDER_ENABLED_KEY, false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val calendar =
                    intent.getParcelableExtra(Constants.ALARM_TIME_EXTRA, Calendar::class.java)
                calendar?.let { scheduledAlarm ->
                    if (isReminderStillEnabled) {
                        showNotification()
                        val alarmForNextDay = scheduledAlarm.apply { add(Calendar.DATE, 1) }
                        alarmScheduler.schedule(alarmForNextDay)
                    }
                }
            }
        }
    }
}