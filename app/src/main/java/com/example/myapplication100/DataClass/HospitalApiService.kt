package com.example.myapplication100.DataClass

import com.example.myapplication100.DataClass.Appointment_Examination.*
import com.example.myapplication100.DataClass.Family.FamilyMemberDetail
import com.example.myapplication100.DataClass.Family.FamilyResponse
import com.example.myapplication100.DataClass.Login.*
import com.example.myapplication100.DataClass.Medicine_Prescription.*
import com.example.myapplication100.DataClass.User_Roles.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface HospitalApiService {

    // --- 1. LOGIN & REGISTER ---
    @POST("register/patient")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("family/all-groups")
    suspend fun getAllFamilies(): Response<List<FamilyResponse>>

    @GET("users")
    suspend fun getAllUsers(): Response<List<UserResponse>>

    // --- 2. APPOINTMENTS ---
    @GET("appointments")
    suspend fun getAllAppointments(): Response<List<Appointment>>

    @GET("appointments/patient/{patient_iduser}") // 👈 แก้ให้ตรงกับ Node.js
    suspend fun getAppointmentsByUser(
        @Path("patient_iduser") patient_iduser: Int
    ): Response<List<Appointment>>

    @POST("appointments")
    suspend fun createAppointment(@Body appointment: Appointment): Response<AppointmentResponse>

    // --- 3. DOCTOR WORKFLOW ---

    // 🟢 รวมร่างเป็นอันเดียว: ส่งทั้งผลตรวจและ ID ยาไปที่ Node.js
    @FormUrlEncoded
    @POST("examination/update-result")
    suspend fun updateExamination(
        @Field("idAppointment") id: Int,      // ชื่อ Field ต้องตรงกับ req.body ใน Node.js สัส
        @Field("diagnosis") diagnosis: String,
        @Field("treatment") treatment: String,
        @Field("note") note: String,
        @Field("medicines_json") medicinesJson: String, // 👈 เปลี่ยนชื่อ field ให้สื่อถึง JSON
        @Field("next_appointment") nextAppoint: String?
    ): Response<Unit>

    @POST("prescription/add")
    suspend fun addPrescriptions(
        @Query("idAppointment") id: Int,
        @Body list: List<Prescription>
    ): Response<Unit>

    @FormUrlEncoded
    @POST("appointments/update-status")
    suspend fun updateStatus(
        @Field("idAppointment") id: Int,
        @Field("status") status: String
    ): Response<Unit>

    // 🟢 ดึงรายชื่อยามาโชว์ใน Dropdown
    @GET("medicines")
    suspend fun getMedicines(): Response<List<Medicine>>

    // --- 4. NURSE & BILLING ---
    @FormUrlEncoded
    @POST("examination/update-cost")
    suspend fun updateCost(
        @Field("idAppointment") id: Int,
        @Field("total_cost") cost: Double
    ): Response<Unit>

    @PUT("examination/vitals/{id}")
    suspend fun updateVitals(
        @Path("id") examId: Int,
        @Body vitals: VitalsRequest
    ): Response<Unit>

    // --- 5. PATIENT HISTORY ---
    @GET("medical-history/{id}")
    suspend fun getMedicalHistory(@Path("id") userId: Int): Response<List<Examination>>

    @GET("current-medicine/{id}")
    suspend fun getCurrentMedicine(@Path("id") userId: Int): Response<List<CurrentMedicine>>

    @GET("profile/{id}")
    suspend fun getPatientProfile(@Path("id") userId: Int): Response<UserResponse>

    // --- 6. FAMILY MANAGEMENT ---
    @GET("family/members/{userId}")
    suspend fun getFamilyMembersByUserId(@Path("userId") userId: Int): Response<List<FamilyMemberDetail>>

    @POST("family/create")
    suspend fun createFamily(@Body request: Map<String, String>): Response<Map<String, Any>>

    @FormUrlEncoded
    @POST("family/add-member")
    suspend fun addMemberToFamily(
        @Field("idFamily") idFamily: Int,
        @Field("citizenId") citizenId: String
    ): Response<Unit>

    @DELETE("family/member/{userId}")
    suspend fun removeMemberFromFamily(@Path("userId") userId: Int): Response<ResponseBody>

    @DELETE("family/{idFamily}")
    suspend fun deleteWholeFamilyById(@Path("idFamily") idFamily: Int): Response<ResponseBody>

    @GET("appointments/current-status")
    suspend fun getCurrentQueueStatus(): retrofit2.Response<QueueResponse>








        @GET("appointments/today")
        suspend fun getTodayQueue(): List<Appointmentnurse>

        @GET("appointments/summary")
        suspend fun getQueueSummary(): QueueSummary

        @POST("vitals")
        suspend fun saveVitals(
            @Body vitals: VitalsRequestnurse
        ): Response<Unit>







}