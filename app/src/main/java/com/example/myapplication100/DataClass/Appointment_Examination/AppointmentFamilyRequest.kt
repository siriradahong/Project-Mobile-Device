package com.example.myapplication100.DataClass.Appointment_Examination

data class AppointmentFamilyRequest(
    val booking_by_user_id: Int,
    val citizen_id: String,
    val Appointment_date: String,
    val time_slot: String,
    val initial_symptom: String,
    val booking_type: String
)
