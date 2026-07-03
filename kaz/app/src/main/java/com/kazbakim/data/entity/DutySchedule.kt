package com.kucukbalina.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duty_schedules")
data class DutySchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val personName: String,
    val startTime: String, // Format: "HH:mm"
    val endTime: String,   // Format: "HH:mm"
    val date: String,      // Format: "yyyy-MM-dd"
    val isAlarmEnabled: Boolean = true
)
