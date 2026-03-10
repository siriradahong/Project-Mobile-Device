package com.example.myapplication100.DataClass.Appointment_Examination

data class DoctorQueueItem(
    val idAppointment: Int,
    val queue_number: String?,
    val status: String?,
    val patient_name: String?,
    val patient_lastname: String?,
    val drug_allergy: String?,
    val initial_symptom: String?,
    // ข้อมูลจากตาราง Exam (ปรับให้สะกด blood_presure ตาม DB เป๊ะๆ)
    val blood_presure_sys: String?,
    val blood_presure_dia: String?,
    val heart_rate: String?,
    val weight: String?,
    val height: String?,
    val temperature: String?
)
