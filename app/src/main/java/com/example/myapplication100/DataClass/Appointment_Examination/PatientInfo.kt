package com.example.myapplication100.DataClass.Appointment_Examination

data class PatientInfo(
    val firstname: String,
    val lastname: String,
    val citizen_id: String,
    val chronic_allergy: String?,
    val drug_allergy: String?
)
