package com.example.myapplication100.DataClass.Appointment_Examination

data class Appointment(
    val idAppointment: Int? = null,
    val patient_iduser: Int,
    val booking_by_user_id: Int,
    val Appointment_date: String,
    val time_slot: String,
    val initial_symptom: String?,
    val queue_number: String?,
    val check_in_time: String?,
    val status: String, // Pending, Screening, etc.
    val booking_type: String // Online, Walk-in
)