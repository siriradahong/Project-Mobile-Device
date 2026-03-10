package com.example.myapplication100.DataClass.Appointment_Examination

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication100.DataClass.Family.FamilyMemberDetail
import com.example.myapplication100.DataClass.Medicine_Prescription.Medicine
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Data Class สำหรับเก็บข้อมูลยาที่จะสั่ง
data class PrescribedMedicine(
    var medicineId: Int = 0,
    var medicineName: String = "เลือกยา...",
    var quantity: Int = 1
)

class AppointmentViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    // --- 1. ข้อมูลสถานะคิว (สำหรับหน้า Home คนไข้) ---
    var currentQueue = mutableStateOf<String>("A00")
        private set

    var remainingQueue = mutableStateOf(0)
        private set

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    private val _allAppointments = MutableStateFlow<List<Appointment>>(emptyList())
    val allAppointments: StateFlow<List<Appointment>> = _allAppointments.asStateFlow()

    // --- 2. ข้อมูลยา ---
    private val _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    val medicines: StateFlow<List<Medicine>> = _medicines.asStateFlow()

    var prescribedMedicines = mutableStateListOf<PrescribedMedicine>()
        private set

    var nextAppointmentDate by mutableStateOf("")

    // --- 3. ข้อมูล Nurse/Doctor/Family ---
    var selectedAppointment = mutableStateOf<Appointment?>(null)
    var familyMembers by mutableStateOf<List<FamilyMemberDetail>>(emptyList())
    var selectedMember by mutableStateOf<FamilyMemberDetail?>(null)
    var expanded by mutableStateOf(false)

    // --- 💊 ฟังก์ชันจัดการรายการยา ---
    fun addMedicineEntry() {
        prescribedMedicines.add(PrescribedMedicine())
    }

    fun removeMedicineEntry(index: Int) {
        if (prescribedMedicines.isNotEmpty()) {
            prescribedMedicines.removeAt(index)
        }
    }

    fun clearMedicineEntries() {
        prescribedMedicines.clear()
        nextAppointmentDate = ""
    }

    // --- 🟢 ระบบคิว (NEW!) ---

    // โหลดสถานะคิวที่กำลังตรวจอยู่ตอนนี้ (เรียก Route: /appointments/current-status)
    fun loadCurrentQueueStatus() {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentQueueStatus()
                if (response.isSuccessful && response.body() != null) {
                    currentQueue.value = response.body()?.current_queue ?: "A00"
                    calculateRemainingQueue() // คำนวณคิวที่เหลือทันที
                }
            } catch (e: Exception) {
                Log.e("VM_ERROR", "loadCurrentQueueStatus: ${e.message}")
            }
        }
    }

    // คำนวณคิวที่เหลือ: เอาเลขคิวมึง ลบ เลขคิวปัจจุบัน
    private fun calculateRemainingQueue() {
        // หานัดหมายล่าสุดของวันนี้ที่ยังไม่จบเคส
        val myAppointment = _appointments.value.firstOrNull {
            it.status == "Pending" || it.status == "Screening" || it.status == "In-room"
        }

        val myQueueStr = myAppointment?.queue_number
        val currentQueueStr = currentQueue.value

        if (myQueueStr != null && currentQueueStr != "A00" && currentQueueStr != "ไม่มีคิว") {
            try {
                val myNum = myQueueStr.substring(1).toInt()
                val currentNum = currentQueueStr.substring(1).toInt()
                val diff = myNum - currentNum
                remainingQueue.value = if (diff > 0) diff else 0
            } catch (e: Exception) {
                remainingQueue.value = 0
            }
        } else {
            remainingQueue.value = 0
        }
    }

    // --- 🩺 งานของหมอ (ไม่กระทบสัส) ---
    fun doctorSubmitTreatment(
        idAppointment: Int,
        diagnosis: String,
        treatment: String,
        note: String
    ) {
        viewModelScope.launch {
            try {
                val medicinesJson = Gson().toJson(prescribedMedicines)
                val examSuccess = repository.updateExamination(
                    id = idAppointment,
                    diag = diagnosis,
                    treat = treatment,
                    note = note,
                    medJson = medicinesJson,
                    nextAppoint = if (nextAppointmentDate.isEmpty()) null else nextAppointmentDate
                )

                if (examSuccess) {
                    repository.updateAppointmentStatus(idAppointment, "Completed")
                    val allAppoints = repository.getAllAppointments() ?: emptyList()
                    val nextPatient = allAppoints.firstOrNull {
                        it.status == "Screening" || it.status == "Pending"
                    }
                    nextPatient?.idAppointment?.let { nextId ->
                        repository.updateAppointmentStatus(nextId, "In-room")
                    }
                    loadAllAppointments()
                    clearMedicineEntries()
                }
            } catch (e: Exception) {
                Log.e("DoctorSubmit", "Error: ${e.message}")
            }
        }
    }

    // --- 💊 โหลดรายชื่อยา ---
    fun loadMedicines() {
        viewModelScope.launch {
            try {
                val response = repository.getMedicines()
                if (response.isSuccessful) {
                    _medicines.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("VM_ERROR", "loadMedicines: ${e.message}")
            }
        }
    }

    // --- 📊 โหลดนัดหมายคนไข้ (แก้ให้คำนวณคิวด้วย) ---
    fun loadAppointments(patient_iduser: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getAppointmentsByUser(patient_iduser)
                _appointments.value = result ?: emptyList()
                calculateRemainingQueue() // โหลดเสร็จต้องคำนวณคิวใหม่
            } catch (e: Exception) {
                Log.e("VM_ERROR", "loadAppointments: ${e.message}")
            }
        }
    }

    fun loadAllAppointments() {
        viewModelScope.launch {
            try {
                val result = repository.getAllAppointments()
                if (result != null) _allAppointments.value = result
            } catch (e: Exception) { Log.e("VM_ERROR", e.message ?: "") }
        }
    }

    // --- 🏥 งานของพยาบาล ---
    fun nurseCompleteBilling(idAppointment: Int, totalCost: Double) {
        viewModelScope.launch {
            try {
                if (repository.updateTotalCost(idAppointment, totalCost) &&
                    repository.updateAppointmentStatus(idAppointment, "Completed")) {
                    loadAllAppointments()
                }
            } catch (e: Exception) { Log.e("NurseSubmit", e.message ?: "") }
        }
    }

    fun loadFamilyMembers(userId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getFamilyMembers(userId)
                if (response.isSuccessful) familyMembers = response.body() ?: emptyList()
            } catch (e: Exception) { Log.e("VM_ERROR", e.message ?: "") }
        }
    }

    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                val response = repository.createAppointment(appointment)
                if (response.isSuccessful && appointment.patient_iduser != null) {
                    loadAppointments(appointment.patient_iduser)
                }
            } catch (e: Exception) { Log.e("BOOKING", e.message ?: "") }
        }
    }

    // --- เพิ่มฟังก์ชันใหม่สำหรับหน้าหมอโดยเฉพาะ ---
    fun loadDoctorQueue() {
        viewModelScope.launch {
            try {
                // เรียกผ่าน Repository โดยใช้ฟังก์ชันใหม่ที่เราจะไปเพิ่มใน Repository
                val result = repository.getDoctorVitalsQueue()
                if (result != null) {
                    _allAppointments.value = result
                }
            } catch (e: Exception) {
                Log.e("VM_DOCTOR_ERROR", "Error: ${e.message}")
            }
        }
    }


}

