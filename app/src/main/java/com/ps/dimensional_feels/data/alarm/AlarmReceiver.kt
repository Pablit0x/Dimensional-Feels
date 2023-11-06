package com.ps.dimensional_feels.data.alarm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ps.dimensional_feels.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @SuppressLint("MissingPermission")
    fun showNotification(
        notificationTitle: String = "Title",
        notificationDescription: String = "Desc",
    ){
        notificationBuilder.setContentTitle(notificationTitle)
        notificationBuilder.setContentText(notificationDescription)
        notificationManager.notify(Constants.DAILY_REMINDER_NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Constants.ALARM_ACTION){
            showNotification()
        }
    }
}