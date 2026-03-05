package com.example.myapplication100.DataClass.Appointment_Examination

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppointmentViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {


    var currentQueue = mutableStateOf<String>("ไม่มีคิว")
        private set

    var remainingQueue = mutableStateOf(0)
        private set

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                val response = repository.createAppointment(appointment)
                Log.d("BOOKING", response.message())
                // โหลดข้อมูลใหม่หลังจากจอง
                if (appointment.patient_iduser != null) {
                    loadAppointments(appointment.patient_iduser)
                }
            } catch (e: Exception) {
                Log.e("BOOKING", e.message ?: "Error")
            }
        }
    }

    fun loadAppointments(patient_iduser: Int) {
        viewModelScope.launch {
            Log.d("test_api", "call: $patient_iduser")
            val result = repository.getAppointmentsByUser(patient_iduser)
            Log.d("test_api", result.toString())
            _appointments.value = result ?: emptyList()

//            try {
//                val response = repository.getAppointmentsByUser(queue_number)
//                if (response.isSuccessful) {
//                    val list = response.body() // ดึง body ออกมาก่อน
//                    if (list != null) {
//                        appointments.clear()
//                        appointments.addAll(list) // ใส่รายการทั้งหมดลงไป
//
//                        // หาคิวที่ใกล้ที่สุด
//                        val myNextQueue = list.find { app ->
//                            app.status == "Pending" || app.status == "Confirmed"
//                        }
//                        currentQueue.value = myNextQueue?.queue_number ?: "ไม่มีคิว"
//
//                        // นับจำนวนคิวที่เหลือ
//                        remainingQueue.value = list.filter { app ->
//                            app.status == "Pending"
//                        }.size
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("FETCH_APP", e.message ?: "Error")
//            }

        }
    }
//fun loadAppointments(patient_iduser: Int) {
//    viewModelScope.launch {
//        try {val result = repository.getAppointmentsByUser(patient_iduser)
//            if (result != null) {
//                _appointments.value = result
//
//                // --- เพิ่มส่วนนี้เพื่ออัปเดตหน้าจอ ---
//
//                // 1. หาคิวที่ใกล้ที่สุด (เช่น วันนี้ และ Status ยังไม่เสร็จ)
//                val myNextQueue = result.find { it.status == "Pending" || it.status == "Confirmed" }
//                currentQueue.value = myNextQueue?.queue_number ?: "ไม่มีคิว"
//
//                // 2. นับจำนวนคิวที่เหลือ (กรองเฉพาะรายการที่กำลังรอ)
//                remainingQueue.value = result.filter { it.status == "Pending" }.size
//            } else {
//                _appointments.value = emptyList()
//                currentQueue.value = "ไม่มีคิว"
//                remainingQueue.value = 0
//            }
//        } catch (e: Exception) {
//            Log.e("FETCH_ERROR", e.message ?: "Error")
//        }
//    }
//}

//    fun fetchMyAppointments(userId: Int) {
//        viewModelScope.launch {
//            try {
//                val response = repository.getMyAppointments(userId)
//                if (response.isSuccessful) {
//                    val list = response.body() // ดึง body ออกมาก่อน
//                    if (list != null) {
//                        appointments.clear()
//                        appointments.addAll(list) // ใส่รายการทั้งหมดลงไป
//
//                        // หาคิวที่ใกล้ที่สุด
//                        val myNextQueue = list.find { app ->
//                            app.status == "Pending" || app.status == "Confirmed"
//                        }
//                        currentQueue.value = myNextQueue?.queue_number ?: "ไม่มีคิว"
//
//                        // นับจำนวนคิวที่เหลือ
//                        remainingQueue.value = list.filter { app ->
//                            app.status == "Pending"
//                        }.size
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("FETCH_APP", e.message ?: "Error")
//            }
//        }
//    }
}