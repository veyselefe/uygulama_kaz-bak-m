package com.kucukbalina.data.dao

import androidx.room.*
import com.kucukbalina.data.entity.DutySchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface DutyScheduleDao {
    @Query("SELECT * FROM duty_schedules ORDER BY date, startTime")
    fun getAllDutySchedules(): Flow<List<DutySchedule>>

    @Query("SELECT * FROM duty_schedules WHERE date = :date ORDER BY startTime")
    fun getDutySchedulesByDate(date: String): Flow<List<DutySchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDutySchedule(dutySchedule: DutySchedule): Long

    @Update
    suspend fun updateDutySchedule(dutySchedule: DutySchedule)

    @Delete
    suspend fun deleteDutySchedule(dutySchedule: DutySchedule)

    @Query("DELETE FROM duty_schedules WHERE id = :id")
    suspend fun deleteDutyScheduleById(id: Long)
}
