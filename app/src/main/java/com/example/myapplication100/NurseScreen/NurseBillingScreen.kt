package com.example.myapplication100.NurseScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication100.DataClass.Appointment_Examination.AppointmentViewModel

@Composable
fun NurseBillingScreen(nav: NavHostController, viewModel: AppointmentViewModel) {
    val selectedApp = viewModel.selectedAppointment.value
    var totalCost by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("สรุปรายการค่าใช้จ่าย", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF4A8C8E))

        if (selectedApp != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("คนไข้ ID: ${selectedApp.patient_iduser}", style = MaterialTheme.typography.bodyLarge)
            Text("อาการ: ${selectedApp.initial_symptom}")

            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = totalCost,
                onValueChange = { totalCost = it },
                label = { Text("กรอกราคารวมสุทธิ") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val cost = totalCost.toDoubleOrNull() ?: 0.0
                    // เรียกฟังก์ชันปิดเคส
                    viewModel.nurseCompleteBilling(selectedApp.idAppointment ?: 0, cost)
                    nav.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A8C8E)),
                enabled = totalCost.isNotEmpty()
            ) {
                Text("ยืนยันการชำระเงิน")
            }
        }
    }
}