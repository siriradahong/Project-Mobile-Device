package com.example.myapplication100.DataClass.Appointment_Examination

data class Appointmentnurse(    val idAppointment: Int,
                                val queue_number: String?,
                                val status: String,
                                val firstname: String,
                                val lastname: String,
                                val idexamination: Int?
)
