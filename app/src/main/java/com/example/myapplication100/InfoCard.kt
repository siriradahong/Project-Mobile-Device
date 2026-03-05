package com.example.myapplication100

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication100.DataClass.Appointment_Examination.PatientInfo
import com.example.myapplication100.LoginRegis.RetrofitClient

@Composable
fun InfoCard(userId: Int) {
    var info by remember { mutableStateOf<PatientInfo?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getPatientInfo(userId)
            if (response.isSuccessful) {
                info = response.body()
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