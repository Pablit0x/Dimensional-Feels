package com.ps.dimensional_feels.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.ps.dimensional_feels.util.Constants
import java.time.LocalTime

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(Constants.EXTRA_ALARM_MESSAGE)
    }
}