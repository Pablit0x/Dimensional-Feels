package com.ps.dimensional_feels.data.alarm

import java.time.LocalDateTime
import java.time.LocalTime

data class AlarmItem(
    val time: LocalTime,
    val message: String
)