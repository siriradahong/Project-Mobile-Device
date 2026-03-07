package com.example.myapplication100.DataClass.Appointment_Examination

data class Appointment(
    val idAppointment: Int? = null,
    val patient_iduser: Int? = null, // ใส่ ? = null เพื่อความปลอดภัยสัส
    val booking_by_user_id: Int? = null,
    val Appointment_date: String? = null,
    val time_slot: String? = null,
    val initial_symptom: String? = null,
    val queue_number: String? = null,
    val check_in_time: String? = null,
    val status: String? = null,
    val booking_type: String? = null,

    // --- 🟢 ข้อมูลที่ดึงมาจาก JOIN (ต้องใส่ ? = null ให้หมดสัส!) ---
    val patient_name: String? = null,
    val patient_lastname: String? = null,
    val chronic_allergy: String? = null,
    val drug_allergy: String? = null,
    val blood_type: String? = null,
    val gender: String? = null,
    val birthdate: String? = null,

    // --- 🔵 ข้อมูลร่างกาย (ซักประวัติโดยพยาบาล) ---
    val weight: Double? = null,
    val height: Double? = null,
    val temperature: Double? = null,
    val blood_pressure: String? = null,
    val heart_rate: Int? = null
)