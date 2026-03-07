package com.example.myapplication100

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication100.DataClass.Appointment_Examination.PatientInfo
import com.example.myapplication100.DataClass.Login.UserResponse
import com.example.myapplication100.LoginRegis.RetrofitClient

@Composable
fun InfoCard(userId: Int) {
    // เปลี่ยนกลับมาเป็น PatientInfo แต่ตอนรับค่าเราจะ Cast มันเอาสัส
    var info by remember { mutableStateOf<PatientInfo?>(null) }

    LaunchedEffect(userId) {
        try {
            val response = RetrofitClient.instance.getPatientProfile(userId)
            if (response.isSuccessful) {
                // ตรงนี้สำคัญ! ถ้า API ส่ง UserResponse มา แต่มึงจะใช้ PatientInfo
                // มึงต้องมั่นใจว่า Data Class สองอันนี้ฟิลด์มันเหมือนกัน
                info = response.body() as? PatientInfo
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(text = "โรคประจำตัว : ${info?.chronic_allergy ?: "-"}", color = Color.Black)
        Spacer(Modifier.height(6.dp))
        Text(text = "แพ้ยา : ${info?.drug_allergy ?: "-"}", color = Color.Black)
    }
    }

