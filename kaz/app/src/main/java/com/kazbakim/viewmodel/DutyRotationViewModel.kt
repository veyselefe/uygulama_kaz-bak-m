package com.kucukbalina.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kucukbalina.data.AppDatabase
import com.kucukbalina.data.entity.DutyRotation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DutyRotationViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val dutyRotationDao = database.dutyRotationDao()
    
    private val _dutyRotations = MutableStateFlow<List<DutyRotation>>(emptyList())
    val dutyRotations: StateFlow<List<DutyRotation>> = _dutyRotations.asStateFlow()
    
    init {
        loadDutyRotations()
    }
    
    private fun loadDutyRotations() {
        viewModelScope.launch {
            dutyRotationDao.getAllActiveDutyRotations().collect { rotations ->
                _dutyRotations.value = rotations
            }
        }
    }
    
    fun addPersonToRotation(personName: String) {
        viewModelScope.launch {
            val maxOrder = dutyRotationDao.getMaxOrderIndex() ?: -1
            val rotation = DutyRotation(
                personName = personName,
                orderIndex = maxOrder + 1,
                isActive = true
            )
            dutyRotationDao.insertDutyRotation(rotation)
        }
    }
    
    fun updateRotation(rotation: DutyRotation) {
        viewModelScope.launch {
            dutyRotationDao.updateDutyRotation(rotation)
        }
    }
    
    fun deleteRotation(rotation: DutyRotation) {
        viewModelScope.launch {
            dutyRotationDao.deleteDutyRotation(rotation)
        }
    }
    
    fun moveUp(rotation: DutyRotation) {
        viewModelScope.launch {
            val currentList = _dutyRotations.value.toMutableList()
            val currentIndex = currentList.indexOf(rotation)
            
            if (currentIndex > 0) {
                val previousRotation = currentList[currentIndex - 1]
                val updatedRotation = rotation.copy(orderIndex = previousRotation.orderIndex)
                val updatedPrevious = previousRotation.copy(orderIndex = rotation.orderIndex)
                
                dutyRotationDao.updateDutyRotation(updatedRotation)
                dutyRotationDao.updateDutyRotation(updatedPrevious)
            }
        }
    }
    
    fun moveDown(rotation: DutyRotation) {
        viewModelScope.launch {
            val currentList = _dutyRotations.value.toMutableList()
            val currentIndex = currentList.indexOf(rotation)
            
            if (currentIndex < currentList.size - 1) {
                val nextRotation = currentList[currentIndex + 1]
                val updatedRotation = rotation.copy(orderIndex = nextRotation.orderIndex)
                val updatedNext = nextRotation.copy(orderIndex = rotation.orderIndex)
                
                dutyRotationDao.updateDutyRotation(updatedRotation)
                dutyRotationDao.updateDutyRotation(updatedNext)
            }
        }
    }
}
