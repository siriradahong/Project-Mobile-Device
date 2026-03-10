package com.example.myapplication100.DataClass.Appointment_Examination

import android.util.Log
import com.example.myapplication100.DataClass.Family.FamilyMemberDetail
import com.example.myapplication100.DataClass.Family.FamilyResponse
import retrofit2.Response
import com.example.myapplication100.DataClass.HospitalApiService
import com.example.myapplication100.DataClass.Login.QueueResponse
import com.example.myapplication100.DataClass.Medicine_Prescription.*
import com.example.myapplication100.DataClass.HospitalApiService.*
class AppointmentRepository(
    private val api: HospitalApiService
) {
    // 1. create appoint
    suspend fun createAppointment(appointment: Appointment): Response<AppointmentResponse> {
        return api.createAppointment(appointment)
    }

    // 2. get appoint
    suspend fun getAppointmentsByUser(patient_iduser: Int): List<Appointment>? {
        val response = api.getAppointmentsByUser(patient_iduser)
        return if (response.isSuccessful) response.body() else null
    }

    // 3. get all appoint
    suspend fun getAllAppointments(): List<Appointment>? {
        return try {
            val response = api.getAllAppointments()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    // 4. get all fam
    suspend fun getAllFamilies(): Response<List<FamilyResponse>> {
        return api.getAllFamilies()
    }

    // 5. get fam member
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

    // 7. update Q
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


    suspend fun getDoctorVitalsQueue(): List<Appointment>? {
        return try {
            // ถ้าคืนค่าเป็น List เลย ก็ไม่ต้องเช็ก isSuccessful หรือ .body()
            val response = api.getDoctorQueue()
            response // คืนค่าตัวแปร response ออกไปเลยสัส!
        } catch (e: Exception) {
            Log.e("RepoError", e.message ?: "")
            null
        }
    }

}