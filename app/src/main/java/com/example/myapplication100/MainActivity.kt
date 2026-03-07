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

    Scaffold(
        bottomBar = {
            val showBottomBar = currentRoute.startsWith("home") ||
                    currentRoute == "history" ||
                    currentRoute == "profile" ||
                    currentRoute == "booking" ||
                    currentRoute == "doctor_home" ||
                    currentRoute == "nurse_home"

            if (showBottomBar) { BottomBar(nav) }
        }
    ) { pad ->
        NavHost(nav, startDestination = "login", Modifier.padding(pad)) {
            composable("login") { LoginScreen(nav) }
            composable("register1") { RegisterStep1Screen(nav) }
            composable("register2") { RegisterStep2Screen(nav) }

            composable("home/{fname}/{lname}") { HomeScreen(nav, viewModel) }
            composable("booking") { BookingScreen(nav, viewModel) }
            composable("history") { HistoryScreen(UserSession.iduser) }
            composable("profile") { ProfileScreen() }

            composable("doctor_home") { DoctorHomeScreen(nav, viewModel) }
            composable("doctor_treatment/{appointmentId}") { entry ->
                val id = entry.arguments?.getString("appointmentId")?.toIntOrNull() ?: 0
                DoctorTreatmentScreen(nav, id, viewModel)
            }
            composable("nurse_home") { NurseHomeScreen(nav, viewModel) }
        }
    }
}

@Composable
fun HomeScreen(nav: NavHostController, viewModel: AppointmentViewModel) {
    val appointments by viewModel.appointments.collectAsState()
    val currentQueue by viewModel.currentQueue
    val remainingQueue by viewModel.remainingQueue

    // 🟢 ย้ายมาไว้ตรงนี้สัส! หน้าแรกต้องเป็นคนดึงข้อมูล
    LaunchedEffect(Unit) {
        while (true) {
            // ดึงนัดหมายเฉพาะของฉัน (ห้ามแสดงย้อนหลัง ซึ่งกูแก้ SQL ใน Node.js ให้แล้ว)
            viewModel.loadAppointments(UserSession.iduser)

            // ดึงเลขคิวปัจจุบัน (ไอ้เลขส้มๆ)
            viewModel.loadCurrentQueueStatus()

            delay(5000)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF21539D))) {
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
            Text(text = "หน้าแรก", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(45.dp).clip(CircleShape).background(Color(0xFFE0E0E0)), Alignment.Center) {
                                Icon(Icons.Default.Groups, null, tint = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("ยินดีต้อนรับ, ${UserSession.firstName}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                        Icon(Icons.Default.Notifications, null, tint = Color(0xFF21539D), modifier = Modifier.size(28.dp))
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    Card(modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp)), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFFFA726)).padding(vertical = 10.dp), Alignment.Center) {
                                Text("กำลังตรวจคิว", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(text = currentQueue, fontSize = 80.sp, fontWeight = FontWeight.Black, color = Color(0xFF21539D))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("เหลือก่อนถึงคิวคุณ ", fontSize = 16.sp)
                                Text("$remainingQueue", color = Color.Red, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                                Text(" คิว", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    Button(onClick = { nav.navigate("booking") }, modifier = Modifier.fillMaxWidth().height(70.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F7F8B))) {
                        Icon(Icons.Default.CalendarMonth, null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("จองคิวใหม่", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = Color.Red)
                        Text("นัดหมายของฉัน (เฉพาะคุณ)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (appointments.isEmpty()) {
                    item { Text("ไม่มีรายการนัดหมายวันนี้", modifier = Modifier.padding(20.dp), color = Color.Gray) }
                } else {
                    items(appointments) { app ->
                        AppointmentCard("${app.Appointment_date} | เวลา: ${app.time_slot}\nอาการ: ${app.initial_symptom}\nสถานะ: ${app.status}")
                        Spacer(modifier = Modifier.height(12.dp))
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
        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF2F5DAA)).padding(vertical = 20.dp), Alignment.Center) {
            Text("จองคิวให้ตัวเอง", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(value = selectedDate, onValueChange = {}, label = { Text("วันที่จอง") }, readOnly = true, modifier = Modifier.fillMaxWidth(), trailingIcon = { IconButton(onClick = { datePickerDialog.show() }) { Icon(Icons.Default.DateRange, null) } })
            Spacer(modifier = Modifier.height(16.dp))
            Text("เลือกช่วงเวลา", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                TimeSlotButton("09:00 - 12:00\n(เช้า)", selectedTimeSlot == "09:00 - 12:00") { selectedTimeSlot = "09:00 - 12:00" }
                TimeSlotButton("13:00 - 16:00\n(บ่าย)", selectedTimeSlot == "13:00 - 16:00") { selectedTimeSlot = "13:00 - 16:00" }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = symptom, onValueChange = { symptom = it }, label = { Text("อาการเบื้องต้น") }, modifier = Modifier.fillMaxWidth().height(120.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if(selectedDate.isEmpty()) { Toast.makeText(context, "กรุณาเลือกวันที่", Toast.LENGTH_SHORT).show(); return@Button }
                val appointment = Appointment(null, iduser, iduser, selectedDate, selectedTimeSlot, symptom, null, null, "Pending", "Walk-in")
                viewModel.createAppointment(appointment)
                navController.popBackStack()
            }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A8C8E))) {
                Text("ยืนยันการจอง", color = Color.White)
            }
        }
    }
}

@Composable
fun TimeSlotButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) Color(0xFF2F5DAA) else Color.Gray
    val bgColor = if (isSelected) Color(0xFFE3F2FD) else Color.Transparent
    Box(modifier = Modifier.width(160.dp).border(1.5.dp, borderColor, RoundedCornerShape(50)).clip(RoundedCornerShape(50)).background(bgColor).clickable { onClick() }.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
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
            Box {
                OutlinedTextField(value = if(selectedMember != null) "${selectedMember?.firstName} ${selectedMember?.lastName}" else "เลือกสมาชิก", onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth(), trailingIcon = { IconButton(onClick = { expanded = true }) { Icon(Icons.Default.ArrowDropDown, null) } })
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    viewModel.familyMembers.forEach { member ->
                        DropdownMenuItem(text = { Text("${member.firstName} ${member.lastName}") }, onClick = { selectedMember = member; expanded = false })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = selectedDate, onValueChange = {}, label = { Text("วันที่จอง") }, readOnly = true, modifier = Modifier.fillMaxWidth(), trailingIcon = { IconButton(onClick = { datePickerDialog.show() }) { Icon(Icons.Default.DateRange, null) } })
            Spacer(modifier = Modifier.height(16.dp))
            Text("เลือกช่วงเวลา", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                TimeSlotButton("09:00 - 12:00\n(เช้า)", selectedTimeSlot == "09:00 - 12:00") { selectedTimeSlot = "09:00 - 12:00" }
                TimeSlotButton("13:00 - 16:00\n(บ่าย)", selectedTimeSlot == "13:00 - 16:00") { selectedTimeSlot = "13:00 - 16:00" }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = symptom, onValueChange = { symptom = it }, label = { Text("อาการเบื้องต้น") }, modifier = Modifier.fillMaxWidth().height(120.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if(selectedMember == null || selectedDate.isEmpty()) { Toast.makeText(context, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show(); return@Button }
                val appointment = Appointment(null, selectedMember!!.iduser, iduser, selectedDate, selectedTimeSlot, symptom, null, null, "Pending", "Walk-in")
                viewModel.createAppointment(appointment)
                navController.popBackStack()
            }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A8C8E))) {
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
            NavigationBarItem(icon = { Icon(Icons.Default.Payments, null) }, label = { Text("การเงิน") }, selected = currentRoute == "nurse_home", onClick = { navController.navigate("nurse_home") })
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