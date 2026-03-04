package com.example.myapplication100.DataClass

import com.example.myapplication100.DataClass.Appointment_Examination.*
import com.example.myapplication100.DataClass.Login.*
import com.example.myapplication100.DataClass.Medicine_Prescription.*
import com.example.myapplication100.DataClass.User_Roles.*
import retrofit2.Response
import retrofit2.http.*
import com.example.myapplication100.DataClass.Login.*


interface HospitalApiService {

    // --- ส่วนของ Register & Login ---

    // เปลี่ยนชื่อให้ตรงกับหน้า UI (RegisterStep2Screen)

    // เปลี่ยนชื่อและ Request Class ให้ตรงกับหน้า UI (LoginScreen)

    @POST("register/patient")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("users")
    suspend fun getAllUsers(): Response<List<UserResponse>>
    // --- ส่วนของ Patient App (หน้าแรก & ประวัติ) ---

    @GET("patient/{id}/appointments")
    suspend fun getMyAppointments(@Path("id") userId: Int): Response<List<Appointment>>

    @GET("patient/{id}/history")
    suspend fun getMedicalHistory(@Path("id") userId: Int): Response<List<Examination>>


    // --- ส่วนของ Assistant & Doctor App ---

    @GET("appointments/today")
    suspend fun getTodayQueue(): Response<List<Appointment>>

    @POST("appointments")
    suspend fun createAppointment(
        @Body appointment: Appointment
    ): Response<AppointmentResponse>

    @PUT("examination/vitals/{id}")
    suspend fun updateVitals(
        @Path("id") examId: Int,
        @Body vitals: VitalsRequest
    ): Response<Unit>

    @POST("examination/diagnose")
    suspend fun saveTreatment(@Body treatment: Examination): Response<Unit>


}