package com.kucukbalina.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kucukbalina.data.AppDatabase
import com.kucukbalina.data.entity.WaterSchedule
import com.kucukbalina.service.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WaterScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val waterScheduleDao = database.waterScheduleDao()
    private val alarmScheduler = AlarmScheduler(application)
    
    private val _waterSchedules = MutableStateFlow<List<WaterSchedule>>(emptyList())
    val waterSchedules: StateFlow<List<WaterSchedule>> = _waterSchedules.asStateFlow()
    
    init {
        loadWaterSchedules()
    }
    
    private fun loadWaterSchedules() {
        viewModelScope.launch {
            waterScheduleDao.getAllWaterSchedules().collect { schedules ->
                _waterSchedules.value = schedules
            }
        }
    }
    
    fun addWaterSchedule(time: String, description: String) {
        viewModelScope.launch {
            val schedule = WaterSchedule(
                time = time,
                description = description,
                isAlarmEnabled = true
            )
            val id = waterScheduleDao.insertWaterSchedule(schedule)
            
            if (schedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = time,
                    title = "Su Vakti",
                    message = "Kazlara su değiştirme zamanı!",
                    requestCode = id.toInt()
                )
            }
        }
    }
    
    fun updateWaterSchedule(schedule: WaterSchedule) {
        viewModelScope.launch {
            waterScheduleDao.updateWaterSchedule(schedule)
            
            if (schedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = schedule.time,
                    title = "Su Vakti",
                    message = "Kazlara su değiştirme zamanı!",
                    requestCode = schedule.id.toInt()
                )
            } else {
                alarmScheduler.cancelAlarm(schedule.id.toInt())
            }
        }
    }
    
    fun deleteWaterSchedule(schedule: WaterSchedule) {
        viewModelScope.launch {
            waterScheduleDao.deleteWaterSchedule(schedule)
            alarmScheduler.cancelAlarm(schedule.id.toInt())
        }
    }
    
    fun toggleAlarm(schedule: WaterSchedule) {
        viewModelScope.launch {
            val updatedSchedule = schedule.copy(isAlarmEnabled = !schedule.isAlarmEnabled)
            waterScheduleDao.updateWaterSchedule(updatedSchedule)
            
            if (updatedSchedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = updatedSchedule.time,
                    title = "Su Vakti",
                    message = "Kazlara su değiştirme zamanı!",
                    requestCode = updatedSchedule.id.toInt()
                )
            } else {
                alarmScheduler.cancelAlarm(updatedSchedule.id.toInt())
            }
        }
    }
}
