package com.example.myapplication100.DataClass.Appointment_Examination

import com.example.myapplication100.DataClass.Family.FamilyMemberDetail
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

    suspend fun getAllAppointments(): Response<List<Appointment>> {
        return api.getAllAppointments()
    }

    suspend fun createAppointmentFamily(data: Appointment): Response<AppointmentResponse> {
        return api.createAppointment(data)
    }


    suspend fun getFamilyMembers(userId: Int): Response<List<FamilyMemberDetail>> {
        return api.getFamilyMembersByUserId(userId)
    }

}