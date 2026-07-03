package com.kucukbalina.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_schedules")
data class FeedSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val time: String,       // Format: "HH:mm"
    val description: String = "",
    val isAlarmEnabled: Boolean = true
)
