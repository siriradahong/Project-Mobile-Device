package com.example.myapplication100

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.myapplication100.DataClass.Appointment_Examination.Examination
import com.example.myapplication100.DataClass.Appointment_Examination.Appointment

import com.example.myapplication100.LoginRegis.RetrofitClient
import com.example.myapplication100.LoginRegis.UserSession
import kotlinx.coroutines.launch

val MainBlue = Color(0xFF154897)




@Composable
fun HistoryScreen() {

    var historyList by remember { mutableStateOf<List<Examination>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()



    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitClient.instance.getMedicalHistory(com.example.myapplication100.UserSession.id)
                if (response.isSuccessful) {
                    historyList = response.body() ?: emptyList()
                } else {
                    error = "โหลดข้อมูลไม่สำเร็จ"
                }
            } catch (e: Exception) {
                error = "เชื่อมต่อเซิร์ฟเวอร์ไม่ได้"
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    MainBlue,
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ประวัติการรักษา",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            error.isNotEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(error, color = Color.Red)
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(historyList) { exam ->
                        HistoryCard(
                            date = exam.appointmentDate?: "-",
                            symptom = exam.symptom ?: "-",
                            diagnosis = exam.diagnosis ?: "-",
                            medicine = exam.treatment ?: "-"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(
    date: String,
    symptom: String,
    diagnosis: String,
    medicine: String
) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCE6F7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "รายการเมื่อ : $date",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("อาการ : $symptom")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("ผลวินิจฉัย : $diagnosis")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("ยาที่ได้รับ : $medicine")
                    }
                }
            }
        }
    }
}