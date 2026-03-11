package com.example.myapplication100.NurseScreen


import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication100.DataClass.Appointment_Examination.Appointment
import com.example.myapplication100.DataClass.Appointment_Examination.Appointmentnurse
import com.example.myapplication100.DataClass.Appointment_Examination.QueueSummary
import com.example.myapplication100.LoginRegis.RetrofitClient
import java.sql.DriverManager.println
import java.util.Collections.emptyList

//modifier: Modifier = Modifier
@Composable
fun QueueControlScreen(modifier: Modifier = Modifier,
                       nav: NavHostController? = null) {

    var queueList by remember { mutableStateOf<List<Appointmentnurse>>(emptyList()) }
    var summary by remember { mutableStateOf(QueueSummary(0,0,0)) }
    val viewModel: QueueViewModel = viewModel()

    LaunchedEffect(Unit) {

        try {

            val q = RetrofitClient.instance.getTodayQueue()
            println("QUEUE = $q")
            queueList = q

        } catch (e: Exception) {
            println("QUEUE ERROR")
            e.printStackTrace()
        }

        try {

            val s = RetrofitClient.instance.getQueueSummary()
            println("SUMMARY = $s")
            summary = s

        } catch (e: Exception) {
            println("SUMMARY ERROR")
            e.printStackTrace()
        }

    }



    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF21539D))
    ) {

        // ===== Header =====
        Box(
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "จัดการคิว",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ===== Content Container =====
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF4F4F4),
                    RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ================= SUMMARY SECTION =================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SummaryCard(
                    title = "รอเรียกคิว",
                    value = "${summary.waiting} คน",
                    icon = Icons.Default.List,
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "รอพบแพทย์",
                    value = "${summary.doctor} คน",
                    icon = Icons.Default.Person,
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "ตรวจเสร็จแล้ว",
                    value = "${summary.done} คน",
                    icon = Icons.Default.Check,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                "นัดหมายทั้งหมด (${queueList.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            LazyColumn {

                items(queueList) { item ->

                    QueueItemCard(
                        appointmentId = item.idAppointment,
                        queueNumber = item.queue_number ?: "-",
                        name = "${item.firstname} ${item.lastname}",
                        time = "",
                        status = item.status,
                        isScreened = item.idexamination != null,
                        nav = nav
                    )
                    Spacer(Modifier.height(6.dp))

                }

            }

        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF3F6DB5)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                icon,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(26.dp)
            )

            Spacer(Modifier.height(6.dp))

            Text(title, fontSize = 12.sp)

            Spacer(Modifier.height(4.dp))

            Text(
                value,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3F6DB5)
            )
        }
    }
}
@Composable
fun QueueItemCard(
    appointmentId: Int?,
    queueNumber: String,
    name: String,
    time: String,
    status: String,
    isScreened: Boolean,
    nav: NavHostController? = null
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // วงกลมเลขคิว
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF3F6DB5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(queueNumber, color = Color.White)
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(time, fontSize = 12.sp)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        when {
                            status == "Completed" -> Color.Gray
                            isScreened -> Color(0xFF4CAF50)
                            else -> Color(0xFF3F7F8B)
                        }

                    )
                    .clickable(enabled = status == "Pending" || status == "Screening") {
                        nav?.navigate("vitals/$appointmentId/$queueNumber/${Uri.encode(name)}")
                    }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "คัดกรอง",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

        }
    }
}