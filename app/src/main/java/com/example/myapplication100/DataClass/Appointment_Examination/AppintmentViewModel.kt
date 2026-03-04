package com.example.myapplication100.DataClass.Appointment_Examination

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                val response = repository.createAppointment(appointment)
                Log.d("BOOKING", response.message())
            } catch (e: Exception) {
                Log.e("BOOKING", e.message ?: "Error")
            }
        }
    }
}