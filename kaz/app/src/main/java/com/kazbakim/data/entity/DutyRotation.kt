package com.kucukbalina.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duty_rotations")
data class DutyRotation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val personName: String,
    val orderIndex: Int,
    val isActive: Boolean = true
)
