package com.ps.dimensional_feels.alarm
import java.time.LocalTime
import java.util.Calendar

interface AlarmScheduler {
    fun schedule(calendar: Calendar)
    fun cancelAlarm()
}