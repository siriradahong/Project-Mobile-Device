package com.example.myapplication100.DataClass.Appointment_Examination

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication100.DataClass.Family.FamilyMemberDetail
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

    private val _allAppointments = MutableStateFlow<List<Appointment>>(emptyList())
    val allAppointments = _allAppointments

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

            calculateQueue(patient_iduser)
        }
    }

    fun loadAllAppointments(patient_iduser: Int) {
        viewModelScope.launch {
            val response = repository.getAllAppointments()
            if (response.isSuccessful) {
                _allAppointments.value = response.body() ?: emptyList()

                calculateQueue(patient_iduser)

            }

        }
    }
    fun calculateQueue(patient_iduser: Int) {

        val all = _allAppointments.value
        val my = _appointments.value

        val Screening = all.firstOrNull { it.status == "Screening" }

        currentQueue.value = Screening?.queue_number ?: "ไม่มีคิว"

        val myQueue = my.firstOrNull()

        if (Screening != null && myQueue != null) {

            val runningNumber = Screening.queue_number?.substring(1)?.toIntOrNull() ?: 0
            val myNumber = myQueue.queue_number?.substring(1)?.toIntOrNull() ?: 0

            val remain = myNumber - runningNumber

            remainingQueue.value = if (remain > 0) remain else 0
        }
    }

    fun createAppointmentFamily(data: Appointment) {
        viewModelScope.launch {
            try {
                val response = repository.createAppointmentFamily(data)
                if (response.isSuccessful) {
                    Log.d("BOOK", "success")
                }
            } catch (e: Exception) {
                Log.e("BOOK", e.message ?: "error")
            }
        }
    }


    var familyMembers by mutableStateOf<List<FamilyMemberDetail>>(emptyList())
        private set

    // 2️⃣ เก็บสมาชิกที่เลือก
    var selectedMember by mutableStateOf<FamilyMemberDetail?>(null)
        private set

    // 3️⃣ dropdown เปิดปิด
    var expanded by mutableStateOf(false)

    // 4️⃣ โหลดสมาชิกครอบครัว
    fun loadFamilyMembers(userId: Int) {
        viewModelScope.launch {
            val response = repository.getFamilyMembers(userId)

            if (response.isSuccessful) {
                familyMembers = response.body() ?: emptyList()
            }
        }
    }

    // 5️⃣ เลือกสมาชิก
    fun selectMember(member: FamilyMemberDetail) {
        selectedMember = member
        expanded = false
    }
}






