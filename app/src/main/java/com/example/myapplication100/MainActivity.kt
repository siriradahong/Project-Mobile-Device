package com.example.myapplication100

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication100.DataClass.Appointment_Examination.*
import com.example.myapplication100.DataClass.Family.FamilyMemberDetail
import com.example.myapplication100.LoginRegis.*
import com.example.myapplication100.LoginRegis.UserSession.iduser
import com.example.myapplication100.DoctorScreen.*
import com.example.myapplication100.NurseScreen.*
import kotlinx.coroutines.delay
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    val nav = rememberNavController()
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val api = RetrofitClient.instance
    val repository = AppointmentRepository(api)
    val factory = AppointmentViewModelFactory(repository)
    val viewModel: AppointmentViewModel = viewModel(factory = factory)

    val startDestination = when (UserSession.role) {
        "Nurse" -> "queue"
        "Doctor" -> "doctor_home"
        else -> "login"
    }

    Scaffold(
        bottomBar = {
            val showBottomBar = currentRoute.startsWith("home") ||
                    currentRoute == "history" ||
                    currentRoute == "profile" ||
                    currentRoute == "booking" ||
                    currentRoute == "doctor_home" ||
                    currentRoute == "nurse_home"||
                    currentRoute == "queue"

            if (showBottomBar) { BottomBar(nav) }
        }
    ) { pad ->
        NavHost(nav, startDestination = startDestination, Modifier.padding(pad)) {
            composable("login") { LoginScreen(nav) }
            composable("register1") { RegisterStep1Screen(nav) }
            composable("register2") { RegisterStep2Screen(nav) }

            composable("home/{fname}/{lname}") { HomeScreen(nav, viewModel) }
            composable("booking") { BookingScreen(nav, viewModel) }
            composable("history") { HistoryScreen(UserSession.iduser) }
            composable("profile") {ProfileScreen(nav = nav)}

            composable("doctor_home") { DoctorHomeScreen(nav, viewModel) }
            composable("doctor_treatment/{appointmentId}") { entry ->
                val id = entry.arguments?.getString("appointmentId")?.toIntOrNull() ?: 0
                DoctorTreatmentScreen(nav, id, viewModel)
            }
            composable("nurse_home") { NurseHomeScreen(nav, viewModel) }

            composable("queue") {

                QueueControlScreen(
                    nav = nav
                )
            }

            composable(
                route = "vitals/{appointmentId}/{queue}/{name}"
            ) { backStackEntry ->

                val appointmentId =
                    backStackEntry.arguments?.getString("appointmentId")?.toIntOrNull() ?: 0

                val queueNumber =
                    backStackEntry.arguments?.getString("queue") ?: ""

                val patientName =
                    backStackEntry.arguments?.getString("name") ?: ""

                VitalsScreen(
                    nav = nav,
                    appointmentId = appointmentId,
                    queueNumber = queueNumber,
                    patientName = patientName
                )
            }
        }
    }
}
@Composable
fun HomeScreen(nav: NavHostController, viewModel: AppointmentViewModel) {
    // 1. ดึงข้อมูล State ทั้งหมด
    val allAppointments by viewModel.allAppointments.collectAsState()
    val myAppointments by viewModel.appointments.collectAsState()

    // 2. หาคิวปัจจุบันของคลินิก (Live)
    val currentClinicPatient = allAppointments.firstOrNull { it.status == "In-room" }
        ?: allAppointments.firstOrNull { it.status == "Screening" }

    // 3. หาคิว "ของฉัน" ที่กำลังจะมาถึง (กรองเอาเฉพาะคนที่มีสถานะยังไม่เสร็จ)
    val myNextQueue = myAppointments.firstOrNull { it.status != "Success" && it.status != "Cancelled" }

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.loadAllAppointments() // ดึงรวม
            viewModel.loadAppointments(UserSession.iduser) // ดึงส่วนตัว (สำคัญ!)
            delay(5000)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF21539D))) {
        // --- Header ---
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
            Text(text = "หน้าแรก", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                item {
                    Text("คิวของคุณ", fontWeight = FontWeight.Bold, color = Color(0xFF21539D), fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (myNextQueue != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // สีฟ้าอ่อน
                            border = BorderStroke(2.dp, Color(0xFF21539D)),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
                                Text(text = myNextQueue.queue_number ?: "รอคิว", fontSize = 70.sp, fontWeight = FontWeight.Black, color = Color(0xFF21539D))
                                Text("สถานะของคุณ: ${myNextQueue.status}", fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                                Text("เวลานัด: ${myNextQueue.time_slot}", fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("คุณยังไม่มีนัดหมายในวันนี้", modifier = Modifier.padding(20.dp), color = Color.Gray, textAlign = TextAlign.Center)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // --- [HIGHLIGHT] คิวของคุณ ---
// --- [LIVE] คิวปัจจุบันของคลินิก (ดีไซน์ใหม่ตามรูป) ---
                item {
                    Text("กำลังเรียกตรวจ (Live)", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (currentClinicPatient != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(10.dp, RoundedCornerShape(20.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Header สีส้ม พร้อม Status
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFFFB74D)) // สีส้มอ่อนตามรูป
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "กำลังตรวจคิว",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        // เพิ่ม Status Badge เข้าไปในแถบสีส้ม
                                        BadgeStatus(currentClinicPatient.status ?: "")
                                    }
                                }

                                // เลขคิวขนาดใหญ่
                                Text(
                                    text = currentClinicPatient.queue_number ?: "-",
                                    fontSize = 100.sp, // ขนาดใหญ่พิเศษตามรูป
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )

                                // คำนวณคิวที่เหลือ (ถ้าเรามีคิวในระบบ)
                                if (myNextQueue != null) {
                                    // หา index ของเราและคิวปัจจุบันในรายการทั้งหมดเพื่อหาผลต่าง
                                    val allPending = allAppointments.filter { it.status != "Success" && it.status != "Cancelled" }
                                    val currentIdx = allPending.indexOfFirst { it.queue_number == currentClinicPatient.queue_number }
                                    val myIdx = allPending.indexOfFirst { it.queue_number == myNextQueue.queue_number }

                                    val remainingQueues = if (myIdx > currentIdx) myIdx - currentIdx else 0

                                    if (remainingQueues > 0) {
                                        Row(modifier = Modifier.padding(bottom = 20.dp)) {
                                            Text("เหลือก่อนถึงคิวคุณ ", color = Color.Gray, fontSize = 16.sp)
                                            Text("$remainingQueues คิว", color = Color.Red, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else if (myNextQueue.queue_number == currentClinicPatient.queue_number) {
                                        Text("ถึงคิวของคุณแล้ว!", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 20.dp))
                                    }
                                } else {
                                    Text("กรุณาจองคิวเพื่อรับบริการ", color = Color.Gray, modifier = Modifier.padding(bottom = 20.dp))
                                }
                            }
                        }
                    } else {
                        NoPatientCard()
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // --- [ACTION] ปุ่มจองคิว ---
                item {
                    Button(
                        onClick = { nav.navigate("booking") },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F7F8B))
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("จองคิวใหม่", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // --- [LIST] คิวรอตรวจทั้งหมด ---
                item {
                    Text("คิวรอตรวจทั้งหมด", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                val pendingQueues = allAppointments.filter { it.status == "Pending" || it.status == "Screening" }
                if (pendingQueues.isEmpty()) {
                    item { Text("ไม่มีคิวรอตรวจ", color = Color.LightGray) }
                } else {
                    items(pendingQueues.take(5)) { patient ->
                        NextQueueCard(patient.queue_number ?: "-", "หมายเลขคิวรอรับบริการ")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}
@Composable
fun BookingScreen(navController: NavHostController, viewModel: AppointmentViewModel) {
    var screenState by remember { mutableStateOf(0) }
    when (screenState) {
        0 -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF2F5DAA)).padding(vertical = 20.dp), Alignment.Center) {
                    Text(text = "เลือกประเภทการจอง", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { screenState = 1 }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A8C8E))) {
                        Text("จองคิวให้ตัวเอง", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { screenState = 2 }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A8C8E))) {
                        Text("จองคิวให้ผู้อื่น", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        1 -> SelfBooking(navController, viewModel)
        2 -> OtherBooking(navController, viewModel)
    }
}

@Composable
fun SelfBooking(navController: NavHostController, viewModel: AppointmentViewModel) {
    var selectedDate by rememberSaveable { mutableStateOf("") }
    var symptom by remember { mutableStateOf("") }
    var selectedTimeSlot by remember { mutableStateOf("09:00 - 12:00") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(context, { _, y, m, d ->
        selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F2F2))) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Color(0xFF2F5DAA)).padding(vertical = 20.dp), Alignment.Center
        ) {
            Text("จองคิวให้ตัวเอง", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(16.dp)) {

            Text("วันที่จอง", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                placeholder = { Text("เลือกวันที่") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { IconButton(onClick = { datePickerDialog.show() }) { Icon(Icons.Default.DateRange, null) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            var selectedType by remember { mutableStateOf("") }
            val type = listOf("Walk-in", "Online")

            Text("เลือกรูปแบบการจอง", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                type.forEach { it ->

                    val isSelected = it == selectedType

                    OutlinedButton(
                        onClick = { selectedType = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF2F5DAA)
                            else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected)
                                Color(0xFFE3EDFF)
                            else Color.Transparent
                        )
                    ) {
                        Text(
                            text = it,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            var selectedTime by remember { mutableStateOf("") }
            val time = listOf("09:00 - 12:00", "13:00 - 16:00")

            Text("เลือกช่วงเวลา", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                time.forEach { it ->

                    val isSelected = it == selectedTime

                    OutlinedButton(
                        onClick = { selectedTime = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF2F5DAA)
                            else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected)
                                Color(0xFFE3EDFF)
                            else Color.Transparent
                        )
                    ) {
                        Text(
                            text = it,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("อาการเบื้องต้น", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = symptom,
                onValueChange = { symptom = it },
                placeholder = { Text("เช่น ปวดหัว อาเจียน") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                if(selectedDate.isEmpty()) {
                    Toast.makeText(context, "กรุณาเลือกวันที่", Toast.LENGTH_SHORT).show(); return@Button }
                val appointment = Appointment(
                    idAppointment = null,
                    patient_iduser = iduser,
                    booking_by_user_id = iduser,
                    Appointment_date = selectedDate,
                    time_slot = selectedTimeSlot,
                    initial_symptom = symptom,
                    queue_number = null,
                    status = "Pending",
                    booking_type = selectedType,
                    // 🔴 ส่วนที่เพิ่มเข้ามาใหม่สำหรับหมอ
                    blood_presure_sys = null,  // ใส่ null เพราะตอนจองยังไม่มีค่านี้
                    blood_presure_dia = null,  // ใส่ null
                    heart_rate = null,
                    weight = null,
                    height = null,
                    temperature = null
                )
                viewModel.createAppointment(appointment)
                navController.popBackStack()
            }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A8C8E))
            ) {
                Text("ยืนยันการจอง", color = Color.White)
            }
        }
    }
}

@Composable
fun TimeSlotButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) Color(0xFF2F5DAA) else Color.Gray
    val bgColor = if (isSelected) Color(0xFFE3F2FD) else Color.Transparent
    Box(
        modifier = Modifier
            .width(160.dp)
            .border(1.5.dp, borderColor, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp),

        contentAlignment = Alignment.Center
    ) {
        Text(text = label, textAlign = TextAlign.Center, color = if(isSelected) Color(0xFF2F5DAA) else Color.Gray, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun OtherBooking(navController: NavHostController, viewModel: AppointmentViewModel) {
    var selectedDate by rememberSaveable { mutableStateOf("") }
    var symptom by remember { mutableStateOf("") }
    var selectedTimeSlot by remember { mutableStateOf("09:00 - 12:00") }
    var selectedMember by remember { mutableStateOf<FamilyMemberDetail?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(context, { _, y, m, d ->
        selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

    LaunchedEffect(Unit) { viewModel.loadFamilyMembers(iduser) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F2F2))) {
        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF2F5DAA)).padding(vertical = 20.dp), Alignment.Center) {
            Text("จองคิวให้ผู้อื่น", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.padding(16.dp)) {

            Text("ผุ้ป่วยที่จะรักษา", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedTextField(
                    value = if(selectedMember != null) "${selectedMember?.firstName} ${selectedMember?.lastName}" else if(viewModel.familyMembers.isEmpty()) "ไม่มีสมาชิกในครอบครัว" else "เลือกสมาชิก",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { IconButton(onClick = { expanded = true }) { Icon(Icons.Default.ArrowDropDown, null) }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    viewModel.familyMembers.forEach { member ->
                        if (member.iduser != UserSession.iduser) {
                            DropdownMenuItem(
                                text = { Text("${member.firstName} ${member.lastName}") },
                                onClick = { selectedMember = member; expanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("วันที่จอง", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                placeholder = { Text("เลือกวันที่") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { IconButton(onClick = { datePickerDialog.show() }) { Icon(Icons.Default.DateRange, null) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            var selectedType by remember { mutableStateOf("") }
            val type = listOf("Walk-in", "Online")

            Text("เลือกรูปแบบการจอง", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                type.forEach { it ->

                    val isSelected = it == selectedType

                    OutlinedButton(
                        onClick = { selectedType = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF2F5DAA)
                            else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected)
                                Color(0xFFE3EDFF)
                            else Color.Transparent
                        )
                    ) {
                        Text(
                            text = it,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            var selectedTime by remember { mutableStateOf("") }
            val time = listOf("09:00 - 12:00", "13:00 - 16:00")

            Text("เลือกช่วงเวลา", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                time.forEach { it ->

                    val isSelected = it == selectedTime

                    OutlinedButton(
                        onClick = { selectedTime = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF2F5DAA)
                            else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected)
                                Color(0xFFE3EDFF)
                            else Color.Transparent
                        )
                    ) {
                        Text(
                            text = it,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("อาการเบื้องต้น", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = symptom,
                onValueChange = { symptom = it },
                placeholder = { Text("เช่น ปวดหัว อาเจียน") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                if(selectedMember == null || selectedDate.isEmpty()) {
                    Toast.makeText(context, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show(); return@Button
                }
                    val appointment = Appointment(
                        idAppointment = null,
                        patient_iduser = iduser,
                        booking_by_user_id = iduser,
                        Appointment_date = selectedDate,
                        time_slot = selectedTimeSlot,
                        initial_symptom = symptom,
                        queue_number = null,
                        status = "Pending",
                        booking_type = selectedType,

                        blood_presure_sys = null,
                        blood_presure_dia = null,
                        heart_rate = null,
                        weight = null,
                        height = null,
                        temperature = null
                    )
                viewModel.createAppointment(appointment)
                navController.popBackStack()
            },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A8C8E)))
            {
                Text("ยืนยันการจอง", color = Color.White)
            }
        }
    }
}

@Composable
fun AppointmentCard(text: String) {
    Card(modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(12.dp)), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(0.5.dp, Color.LightGray)) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(text = text, fontSize = 15.sp, color = Color.Black)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        if (UserSession.role == "Doctor") {
            NavigationBarItem(icon = { Icon(Icons.Default.Dashboard, null) }, label = { Text("ห้องตรวจ") }, selected = currentRoute == "doctor_home", onClick = { navController.navigate("doctor_home") })
            NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("โปรไฟล์") }, selected = currentRoute == "profile", onClick = { navController.navigate("profile") })
        } else if (UserSession.role == "Nurse") {
            NavigationBarItem(
                icon = { Icon(Icons.Default.List, null) },
                label = { Text("จัดการคิว") },
                selected = currentRoute == "queue",
                onClick = { navController.navigate("queue") }
            )

            NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("โปรไฟล์") }, selected = currentRoute == "profile", onClick = { navController.navigate("profile") })
        } else {
            val homeRoute = "home/${UserSession.firstName}/${UserSession.lastName}"
            NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("หน้าแรก") }, selected = currentRoute.startsWith("home"), onClick = { navController.navigate(homeRoute) })
            NavigationBarItem(icon = { Icon(Icons.Default.DateRange, null) }, label = { Text("จองคิว") }, selected = currentRoute == "booking", onClick = { navController.navigate("booking") })
            NavigationBarItem(icon = { Icon(Icons.Default.List, null) }, label = { Text("ประวัติ") }, selected = currentRoute == "history", onClick = { navController.navigate("history") })
            NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("โปรไฟล์") }, selected = currentRoute == "profile", onClick = { navController.navigate("profile") })
        }
    }
}