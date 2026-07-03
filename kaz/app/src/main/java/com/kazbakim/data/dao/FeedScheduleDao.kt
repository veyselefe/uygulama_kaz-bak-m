package com.kucukbalina.data.dao

import androidx.room.*
import com.kucukbalina.data.entity.FeedSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedScheduleDao {
    @Query("SELECT * FROM feed_schedules ORDER BY time")
    fun getAllFeedSchedules(): Flow<List<FeedSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedSchedule(feedSchedule: FeedSchedule): Long

    @Update
    suspend fun updateFeedSchedule(feedSchedule: FeedSchedule)

    @Delete
    suspend fun deleteFeedSchedule(feedSchedule: FeedSchedule)

    @Query("DELETE FROM feed_schedules WHERE id = :id")
    suspend fun deleteFeedScheduleById(id: Long)
}
