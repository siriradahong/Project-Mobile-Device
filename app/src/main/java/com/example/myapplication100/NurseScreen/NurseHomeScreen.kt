package com.example.myapplication100.NurseScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication100.DataClass.Appointment_Examination.AppointmentViewModel
import com.example.myapplication100.LoginRegis.UserSession

@Composable
fun NurseHomeScreen(nav: NavHostController, viewModel: AppointmentViewModel) {
    // ดึงรายการนัดหมายทั้งหมดมาโชว์
    val allAppointments by viewModel.allAppointments.collectAsState()

    // กรองเอาเฉพาะคนที่หมอตรวจเสร็จแล้ว (Status = "Treated")
    val pendingBilling = allAppointments.filter { it.status == "Treated" }

    LaunchedEffect(Unit) {
        viewModel.loadAllAppointments()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        // Header เขียวพยาบาล
        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF4A8C8E)).padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
            Text(text = "รายการรอชำระเงิน", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        if (pendingBilling.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("ไม่มีรายการรอชำระเงิน", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(pendingBilling) { app ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, modifier = Modifier.size(40.dp), tint = Color(0xFF4A8C8E))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "คนไข้ ID: ${app.patient_iduser}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("อาการ: ${app.initial_symptom}", fontSize = 14.sp, color = Color.Gray)
                            }
                            Button(
                                onClick = {
                                    // เก็บค่าคนไข้ที่เลือกลง ViewModel ก่อนไปหน้าคิดเงิน
                                    viewModel.selectedAppointment.value = app
                                    nav.navigate("nurse_home")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A8C8E))
                            ) {
                                Text("คิดเงิน")
                            }
                        }
                    }
                }
            }
        }
    }
}