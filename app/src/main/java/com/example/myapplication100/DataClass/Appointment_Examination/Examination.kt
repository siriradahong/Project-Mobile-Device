package com.example.myapplication100.DataClass.Appointment_Examination

import com.google.gson.annotations.SerializedName

data class Examination(
    val idexamination: Int? = null,
    val exam_date: String?,
    val diagnosis: String?,
    val treatment: String?,
    val note: String?,
    val idAppointment: Int,
    val doctor_id: Int,
    val blood_presure_sys: Int?,
    val blood_presure_dia: Int?,
    val heart_rate: Int?,
    val weight: Double?,
    val height: Double?,
    val temperature: Double?,
    val total_cost: Double?,


    @SerializedName("Appointment_date")
val appointmentDate: String? = null,

@SerializedName("initial_symptom")
val symptom: String? = null,


)
