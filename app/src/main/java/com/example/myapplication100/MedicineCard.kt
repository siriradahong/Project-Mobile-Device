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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication100.DataClass.Medicine_Prescription.Medicine
import com.example.myapplication100.LoginRegis.RetrofitClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.myapplication100.DataClass.Appointment_Examination.CurrentMedicine


@Composable
fun MedicineCard(userId: Int) {

    var medicines by remember { mutableStateOf<List<CurrentMedicine>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getCurrentMedicine(userId)
            if (response.isSuccessful) {
                medicines = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        if (medicines.isEmpty()) {
            Text("ไม่มีรายการยา", color = Color.Gray, modifier = Modifier.padding(8.dp))
        } else {
            medicines.forEach { med: CurrentMedicine ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(med.medicine_name, fontWeight = FontWeight.Bold)
                        Text("ขนาด: ${med.dosage} | ${med.frequency}")
                        Text("วิธีทาน: ${med.instruction}")
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}