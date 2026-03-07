package com.example.myapplication100

data class Appointment(
    val idAppointment: Int,
    val queue_number: String?,
    val status: String,
    val firstname: String,
    val lastname: String,
    val idexamination: Int?
)

