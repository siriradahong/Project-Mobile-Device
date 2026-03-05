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

    suspend fun getAppointmentsByUser(patient_iduser: Int): List<Appointment>? {
        val response = api.getAppointmentsByUser(patient_iduser)

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }


}