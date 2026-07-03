package com.kucukbalina.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kucukbalina.data.dao.*
import com.kucukbalina.data.entity.*

@Database(
    entities = [
        DutySchedule::class,
        DutyRotation::class,
        FeedSchedule::class,
        WaterSchedule::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dutyScheduleDao(): DutyScheduleDao
    abstract fun dutyRotationDao(): DutyRotationDao
    abstract fun feedScheduleDao(): FeedScheduleDao
    abstract fun waterScheduleDao(): WaterScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaz_bakim_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
