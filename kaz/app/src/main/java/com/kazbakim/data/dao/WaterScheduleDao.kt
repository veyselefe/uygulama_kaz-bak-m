package com.kucukbalina.data.dao

import androidx.room.*
import com.kucukbalina.data.entity.WaterSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterScheduleDao {
    @Query("SELECT * FROM water_schedules ORDER BY time")
    fun getAllWaterSchedules(): Flow<List<WaterSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaterSchedule(waterSchedule: WaterSchedule): Long

    @Update
    suspend fun updateWaterSchedule(waterSchedule: WaterSchedule)

    @Delete
    suspend fun deleteWaterSchedule(waterSchedule: WaterSchedule)

    @Query("DELETE FROM water_schedules WHERE id = :id")
    suspend fun deleteWaterScheduleById(id: Long)
}
