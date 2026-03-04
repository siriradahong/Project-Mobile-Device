package com.example.myapplication100.DataClass.Appointment_Examination

import retrofit2.Response
import com.example.myapplication100.DataClass.HospitalApiService

class AppointmentRepository(
    private val api: HospitalApiService
) {
    suspend fun createAppointment(
        appointment: Appointment
    ): Response<AppointmentResponse> {
        return api.createAppointment(appointment)
    }
}