package com.example.myapplication100.DataClass.Appointment_Examination

import com.example.myapplication100.DataClass.Family.FamilyMemberDetail
import com.example.myapplication100.DataClass.Family.FamilyResponse
import retrofit2.Response
import com.example.myapplication100.DataClass.HospitalApiService
import com.example.myapplication100.DataClass.Login.QueueResponse
import com.example.myapplication100.DataClass.Medicine_Prescription.*

class AppointmentRepository(
    private val api: HospitalApiService
) {
    // 1. สร้างนัดหมาย
    suspend fun createAppointment(appointment: Appointment): Response<AppointmentResponse> {
        return api.createAppointment(appointment)
    }

    // 2. ดึงนัดหมายรายคน
    suspend fun getAppointmentsByUser(patient_iduser: Int): List<Appointment>? {
        val response = api.getAppointmentsByUser(patient_iduser)
        return if (response.isSuccessful) response.body() else null
    }

    // 3. ดึงนัดหมายทั้งหมด
    suspend fun getAllAppointments(): List<Appointment>? {
        return try {
            val response = api.getAllAppointments()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    // 4. ดึงกลุ่มครอบครัวทั้งหมด
    suspend fun getAllFamilies(): Response<List<FamilyResponse>> {
        return api.getAllFamilies()
    }

    // 5. ดึงสมาชิกในครอบครัว
    suspend fun getFamilyMembers(userId: Int): Response<List<FamilyMemberDetail>> {
        return api.getFamilyMembersByUserId(userId)
    }

    // 🟢 6. บันทึกผลตรวจ (แก้ไขให้ส่งครบ 6 ค่าตาม Interface ใหม่สัส!)
    suspend fun updateExamination(
        id: Int,
        diag: String,
        treat: String,
        note: String,
        medJson: String,      // JSON ของรายการยาหลายชุด
        nextAppoint: String?  // วันนัดครั้งหน้า
    ): Boolean {
        return try {
            // 👈 มึงต้องส่งให้ครบ 6 ตัวตามที่มึงแก้ใน HospitalApiService สัส!
            val response = api.updateExamination(
                id = id,
                diagnosis = diag,
                treatment = treat,
                note = note,
                medicinesJson = medJson,
                nextAppoint = nextAppoint
            )
            response.isSuccessful
        } catch (e: Exception) { false }
    }

    // 7. อัปเดตสถานะคิว
    suspend fun updateAppointmentStatus(id: Int, status: String): Boolean {
        return try {
            api.updateStatus(id, status).isSuccessful
        } catch (e: Exception) { false }
    }

    // 8. บันทึกค่าใช้จ่าย
    suspend fun updateTotalCost(id: Int, cost: Double): Boolean {
        return try {
            api.updateCost(id, cost).isSuccessful
        } catch (e: Exception) { false }
    }

    // 9. ดึงรายชื่อยามาทำ Dropdown
    suspend fun getMedicines(): Response<List<Medicine>> {
        return api.getMedicines()
    }

    suspend fun getCurrentQueueStatus(): Response<QueueResponse> {
        return api.getCurrentQueueStatus()
    }
}