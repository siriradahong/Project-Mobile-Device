package com.example.myapplication100.DoctorScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication100.DataClass.Appointment_Examination.AppointmentViewModel
import kotlinx.coroutines.delay
import com.example.myapplication100.DataClass.Medicine_Prescription.Medicine
@Composable
fun DoctorHomeScreen(nav: NavHostController, viewModel: AppointmentViewModel) {
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.loadAllAppointments()
            delay(5000)
        }
    }

    val allAppointments by viewModel.allAppointments.collectAsState()

    val currentPatient = allAppointments.firstOrNull { it.status == "In-room" }
        ?: allAppointments.firstOrNull { it.status == "Screening" }

    val nextQueues = allAppointments.filter {
        (it.status == "Screening" || it.status == "Pending") && it.idAppointment != currentPatient?.idAppointment
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF21539D))) {
        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("ห้องตรวจ (วันนี้)", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF4CAF50)) {
                Text("Live", color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 12.sp)
            }
        }

        Surface(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp), color = Color.White) {
            LazyColumn(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Text("กำลังตรวจคนปัจจุบัน", fontWeight = FontWeight.Bold, color = Color.Gray)
                    if (currentPatient != null) {
                        Card(modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp)), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(20.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                                BadgeStatus(currentPatient.status ?: "Unknown")
                                Text(text = currentPatient.queue_number ?: "-", fontSize = 80.sp, fontWeight = FontWeight.Black, color = Color(0xFF21539D))
                                Text(text = "${currentPatient.patient_name ?: "ไม่ระบุชื่อ"} ${currentPatient.patient_lastname ?: ""}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                Text("อาการ: ${currentPatient.initial_symptom ?: "-"}", color = Color.Gray, fontSize = 14.sp)
                                Spacer(Modifier.height(16.dp))
                                Button(onClick = { nav.navigate("doctor_treatment/${currentPatient.idAppointment}") }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21539D)), shape = RoundedCornerShape(12.dp)) {
                                    Icon(Icons.Default.AccountBox, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("บันทึกการรักษา / สั่งยา")
                                }
                            }
                        }
                    } else { NoPatientCard() }
                }
                item {
                    Text("คิวรอตรวจ (${nextQueues.size})", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF21539D))
                }
                if (nextQueues.isEmpty()) {
                    item { Text("ไม่มีคิวรอตรวจ", modifier = Modifier.fillMaxWidth().padding(20.dp), textAlign = TextAlign.Center, color = Color.LightGray) }
                } else {
                    items(nextQueues) { patient ->
                        NextQueueCard(patient.queue_number ?: "-", "${patient.patient_name ?: "คนไข้"} ${patient.patient_lastname ?: ""}")
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeStatus(status: String) {
    val bgColor = when(status) {
        "In-room" -> Color(0xFFFFA726)
        "Screening" -> Color(0xFF42A5F5)
        else -> Color.Gray
    }
    Box(modifier = Modifier.background(bgColor, RoundedCornerShape(10.dp)).padding(horizontal = 16.dp, vertical = 4.dp)) {
        Text(status.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}

@Composable
fun NoPatientCard() {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), shape = RoundedCornerShape(20.dp)) {
        Column(modifier = Modifier.padding(40.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.LightGray)
            Text("ไม่มีคนไข้ในขณะนี้", color = Color.Gray)
        }
    }
}

@Composable
fun NextQueueCard(queue: String, name: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFE3F2FD)) {
                Text(text = queue, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.Bold, color = Color(0xFF21539D))
            }
            Spacer(Modifier.width(16.dp))
            Text(name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
} // --- 🟢 ปิดปีกกาตรงนี้สัส! แยกฟังก์ชันออกมาข้างนอก ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorTreatmentScreen(nav: NavHostController, appointmentId: Int?, viewModel: AppointmentViewModel) {
    val allAppointments by viewModel.allAppointments.collectAsState()
    val medicines by viewModel.medicines.collectAsState()
    val patient = allAppointments.firstOrNull { it.idAppointment == appointmentId }

    // ดึง List ยามาจาก ViewModel
    val prescribedList = viewModel.prescribedMedicines

    var diagnosis by remember { mutableStateOf("") }
    var treatmentNote by remember { mutableStateOf("") }

    val mainBlue = Color(0xFF3F51B5)

    // --- 📅 โซนปฏิทินเลือกวันนัด ---
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        java.time.Instant.ofEpochMilli(it)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                            .toString()
                    } ?: ""
                    viewModel.nextAppointmentDate = selectedDate
                    showDatePicker = false
                }) { Text("ตกลง") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("ยกเลิก") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // โหลดยาตอนเข้าหน้าจอ
    LaunchedEffect(Unit) {
        viewModel.loadMedicines()
        if (viewModel.prescribedMedicines.isEmpty()) {
            viewModel.addMedicineEntry()
        }
    }

    Scaffold(
        topBar = {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF4CAF50))
                }
                Text(" Medical Record (Treatment Note)", color = mainBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("${patient?.patient_name ?: "ชื่อคนไข้"} ${patient?.patient_lastname ?: ""}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("คิว (${patient?.queue_number ?: "A00"})", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = mainBlue)
                }
                Spacer(Modifier.height(16.dp))

                // Vitals
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    VitalBox("BP", patient?.blood_pressure ?: "-", Color.Red)
                    VitalBox("TP", "36.5", Color.Black)
                    VitalBox("HG", "175", Color.Black)
                    VitalBox("WG", "70", Color.Black)
                }
                Spacer(Modifier.height(16.dp))

                // ประวัติแพ้ยา
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF5D6D9B)), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Text(" ประวัติแพ้ยา: ${patient?.drug_allergy ?: "ไม่มี"}", color = Color.White, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(20.dp))
            }

            item {
                Text("การวินิจฉัยและการรักษา", fontWeight = FontWeight.Bold, color = mainBlue)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = diagnosis, onValueChange = { diagnosis = it },
                    label = { Text("ผลการวินิจฉัย") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = treatmentNote, onValueChange = { treatmentNote = it },
                    label = { Text("บันทึกการรักษา / อาการเพิ่มเติม") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                Text("รายการสั่งยา", fontWeight = FontWeight.Bold, color = mainBlue)
            }

            // 💊 รายการยา
            itemsIndexed(prescribedList) { index, item ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    var isExp by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(onClick = { isExp = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                            Text(item.medicineName, maxLines = 1)
                        }
                        DropdownMenu(expanded = isExp, onDismissRequest = { isExp = false }) {
                            medicines.forEach { med ->
                                DropdownMenuItem(
                                    text = { Text("${med.medicineName} (คงเหลือ: ${med.stockQuantity})") },
                                    onClick = {
                                        prescribedList[index] = prescribedList[index].copy(
                                            medicineId = med.idMedicine ?: 0,
                                            medicineName = med.medicineName ?: "ไม่ระบุ"
                                        )
                                        isExp = false
                                    }
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp)) {
                        IconButton(onClick = { if (item.quantity > 1) prescribedList[index] = item.copy(quantity = item.quantity - 1) }) {
                            Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("${item.quantity}", fontWeight = FontWeight.Bold)
                        IconButton(onClick = { prescribedList[index] = item.copy(quantity = item.quantity + 1) }) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp))
                        }
                    }

                    IconButton(onClick = { viewModel.removeMedicineEntry(index) }) {
                        Icon(Icons.Default.Add, null, tint = Color.Red, modifier = Modifier.rotate(45f))
                    }
                }
            }

            item {
                TextButton(onClick = { viewModel.addMedicineEntry() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, null)
                    Text(" เพิ่มรายการยา")
                }
                Spacer(Modifier.height(20.dp))

                // 📅 ช่องเลือกวันนัด (ใช้ปฏิทิน)
                OutlinedTextField(
                    value = viewModel.nextAppointmentDate,
                    onValueChange = { },
                    label = { Text("วันนัดครั้งหน้า") },
                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                    enabled = false,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledBorderColor = mainBlue,
                        disabledLabelColor = mainBlue
                    ),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.rotate(45f))
                        }
                    }
                )
                Spacer(Modifier.height(30.dp))

                // 🔘 ปุ่มปิดเคส (Complete เคสนี้ + ดันเคสถัดไปเป็น In-room)
                Button(
                    onClick = {
                        appointmentId?.let { id ->
                            viewModel.doctorSubmitTreatment(
                                idAppointment = id,
                                diagnosis = diagnosis,
                                treatment = "ตรวจโดยแพทย์",
                                note = treatmentNote
                            )
                            nav.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A828E)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("บันทึกและปิดเคส", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(50.dp))
            }
        }
    }
}
@Composable
fun VitalBox(label: String, value: String, valueColor: Color) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.width(75.dp).height(80.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(8.dp)) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text(value, fontSize = 18.sp, color = valueColor)
        }
    }
}