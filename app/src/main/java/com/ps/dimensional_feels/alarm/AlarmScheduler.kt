package com.ps.dimensional_feels.alarm
import java.time.LocalTime

interface AlarmScheduler {
    fun schedule(time: LocalTime)
    fun cancelAlarm()
}