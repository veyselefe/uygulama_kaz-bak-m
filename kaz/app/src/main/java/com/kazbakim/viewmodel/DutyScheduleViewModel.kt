package com.kucukbalina.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kucukbalina.data.AppDatabase
import com.kucukbalina.data.entity.DutySchedule
import com.kucukbalina.service.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DutyScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val dutyScheduleDao = database.dutyScheduleDao()
    private val alarmScheduler = AlarmScheduler(application)
    
    private val _dutySchedules = MutableStateFlow<List<DutySchedule>>(emptyList())
    val dutySchedules: StateFlow<List<DutySchedule>> = _dutySchedules.asStateFlow()
    
    init {
        loadDutySchedules()
    }
    
    private fun loadDutySchedules() {
        viewModelScope.launch {
            dutyScheduleDao.getAllDutySchedules().collect { schedules ->
                _dutySchedules.value = schedules
            }
        }
    }
    
    fun addDutySchedule(personName: String, startTime: String, endTime: String, date: String) {
        viewModelScope.launch {
            val schedule = DutySchedule(
                personName = personName,
                startTime = startTime,
                endTime = endTime,
                date = date,
                isAlarmEnabled = true
            )
            val id = dutyScheduleDao.insertDutySchedule(schedule)
            
            // Schedule alarm
            if (schedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = startTime,
                    title = "Nöbet Vakti",
                    message = "$personName nöbete başlıyor!",
                    requestCode = id.toInt()
                )
            }
        }
    }
    
    fun updateDutySchedule(schedule: DutySchedule) {
        viewModelScope.launch {
            dutyScheduleDao.updateDutySchedule(schedule)
            
            // Update alarm
            if (schedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = schedule.startTime,
                    title = "Nöbet Vakti",
                    message = "${schedule.personName} nöbete başlıyor!",
                    requestCode = schedule.id.toInt()
                )
            } else {
                alarmScheduler.cancelAlarm(schedule.id.toInt())
            }
        }
    }
    
    fun deleteDutySchedule(schedule: DutySchedule) {
        viewModelScope.launch {
            dutyScheduleDao.deleteDutySchedule(schedule)
            alarmScheduler.cancelAlarm(schedule.id.toInt())
        }
    }
    
    fun toggleAlarm(schedule: DutySchedule) {
        viewModelScope.launch {
            val updatedSchedule = schedule.copy(isAlarmEnabled = !schedule.isAlarmEnabled)
            dutyScheduleDao.updateDutySchedule(updatedSchedule)
            
            if (updatedSchedule.isAlarmEnabled) {
                alarmScheduler.scheduleAlarm(
                    time = updatedSchedule.startTime,
                    title = "Nöbet Vakti",
                    message = "${updatedSchedule.personName} nöbete başlıyor!",
                    requestCode = updatedSchedule.id.toInt()
                )
            } else {
                alarmScheduler.cancelAlarm(updatedSchedule.id.toInt())
            }
        }
    }
}
