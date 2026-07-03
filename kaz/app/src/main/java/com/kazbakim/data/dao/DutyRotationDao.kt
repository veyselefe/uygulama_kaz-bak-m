package com.kucukbalina.data.dao

import androidx.room.*
import com.kucukbalina.data.entity.DutyRotation
import kotlinx.coroutines.flow.Flow

@Dao
interface DutyRotationDao {
    @Query("SELECT * FROM duty_rotations WHERE isActive = 1 ORDER BY orderIndex")
    fun getAllActiveDutyRotations(): Flow<List<DutyRotation>>

    @Query("SELECT * FROM duty_rotations ORDER BY orderIndex")
    fun getAllDutyRotations(): Flow<List<DutyRotation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDutyRotation(dutyRotation: DutyRotation): Long

    @Update
    suspend fun updateDutyRotation(dutyRotation: DutyRotation)

    @Delete
    suspend fun deleteDutyRotation(dutyRotation: DutyRotation)

    @Query("SELECT MAX(orderIndex) FROM duty_rotations")
    suspend fun getMaxOrderIndex(): Int?
}
