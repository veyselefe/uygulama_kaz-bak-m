package com.kucukbalina.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kucukbalina.data.AppDatabase
import com.kucukbalina.data.entity.FeedSchedule
import com.kucukbalina.service.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val feedScheduleDao = database.feedScheduleDao()
    private val alarmScheduler = AlarmScheduler(application)
    
    private val _feedSchedules = MutableStateFlow<List<FeedSchedule>>(emptyList())
    val feedSchedules: StateFlow<List<FeedSchedule>> = _feedSchedules.asStateFlow()
    
    init {
        loadFeedSchedules()
    }
    
    private fun loadFeedSchedules() {
        viewModelScope.launch {
            feedScheduleDao.getAllFeedSchedules().collect { schedules ->
                _feedSchedules.value = schedules
            }
        }
    }
    
    fun addFeedSchedule(time: String, description: String) {
        viewModelScope.launch {
            val schedule = FeedSchedule(
                time = time,
                description = description,
                isAlarmEnabled = true
            )
            val id = feedScheduleDao.insertFeedSchedule(schedule)
            
            if (schedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = time,
                    title = "Yem Vakti",
                    message = "Kazlara yem verme zamanı!",
                    requestCode = id.toInt()
                )
            }
        }
    }
    
    fun updateFeedSchedule(schedule: FeedSchedule) {
        viewModelScope.launch {
            feedScheduleDao.updateFeedSchedule(schedule)
            
            if (schedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = schedule.time,
                    title = "Yem Vakti",
                    message = "Kazlara yem verme zamanı!",
                    requestCode = schedule.id.toInt()
                )
            } else {
                alarmScheduler.cancelAlarm(schedule.id.toInt())
            }
        }
    }
    
    fun deleteFeedSchedule(schedule: FeedSchedule) {
        viewModelScope.launch {
            feedScheduleDao.deleteFeedSchedule(schedule)
            alarmScheduler.cancelAlarm(schedule.id.toInt())
        }
    }
    
    fun toggleAlarm(schedule: FeedSchedule) {
        viewModelScope.launch {
            val updatedSchedule = schedule.copy(isAlarmEnabled = !schedule.isAlarmEnabled)
            feedScheduleDao.updateFeedSchedule(updatedSchedule)
            
            if (updatedSchedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = updatedSchedule.time,
                    title = "Yem Vakti",
                    message = "Kazlara yem verme zamanı!",
                    requestCode = updatedSchedule.id.toInt()
                )
            } else {
                alarmScheduler.cancelAlarm(updatedSchedule.id.toInt())
            }
        }
    }
}
