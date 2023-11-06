package com.ps.dimensional_feels.alarm

import com.ps.dimensional_feels.data.alarm.AlarmItem

interface AlarmScheduler {
    fun schedule(item: AlarmItem)

    fun cancelAlarm(item: AlarmItem)
}