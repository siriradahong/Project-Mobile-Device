package com.example.myapplication100

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication100.DataClass.Family.FamilyResponse
import com.example.myapplication100.LoginRegis.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun FamilyMemberListView(userId: Int) {
    var allFamilies by remember { mutableStateOf<List<FamilyResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // State สำหรับสร้างครอบครัวใหม่
    var showCreateDialog by remember { mutableStateOf(false) }
    var newFamilyName by remember { mutableStateOf("") }
    var citizenIdInput by remember { mutableStateOf("") }
    var createErrorMessage by remember { mutableStateOf("") }

    val clearCreateInputs = {
        newFamilyName = ""
        citizenIdInput = ""
        createErrorMessage = ""
    }

    val loadAllFamilies = {
        scope.launch {
            try {
                val response = RetrofitClient.instance.getAllFamilies()
                if (response.isSuccessful) {
                    allFamilies = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadAllFamilies() }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // --- ส่วนหัวและปุ่มสร้าง ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("รายชื่อครอบครัว", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = { clearCreateInputs(); showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F5DAA)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("สร้าง", color = Color.White, fontSize = 14.sp)
            }
        }

        if (isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.padding(16.dp))
            }
        } else {
            allFamilies.forEach { family ->
                FamilyGroupItem(
                    family = family,
                    currentUserId = userId,
                    onRefresh = { loadAllFamilies() }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    // --- Dialog สร้างครอบครัว (ผู้สร้างไม่เข้ากลุ่ม) ---
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { clearCreateInputs(); showCreateDialog = false },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        val request = mapOf("familyName" to newFamilyName, "targetCitizenId" to citizenIdInput)
                        val res = RetrofitClient.instance.createFamily(request)
                        if (res.isSuccessful) {
                            clearCreateInputs(); showCreateDialog = false; loadAllFamilies()
                        } else {
                            createErrorMessage = "บุคคลนี้มีครอบครัวอยู่แล้ว"
                        }
                    }
                }) { Text("บันทึก") }
            },
            dismissButton = { TextButton(onClick = { clearCreateInputs(); showCreateDialog = false }) { Text("ยกเลิก") } },
            title = { Text("สร้างครอบครัวใหม่") },
            text = {
                Column {
                    if (createErrorMessage.isNotEmpty()) Text(createErrorMessage, color = Color.Red)
                    OutlinedTextField(value = newFamilyName, onValueChange = { newFamilyName = it }, label = { Text("ชื่อครอบครัว") })
                    OutlinedTextField(value = citizenIdInput, onValueChange = { if (it.length <= 13) citizenIdInput = it }, label = { Text("เลขบัตรประชาชนสมาชิก") })
                }
            }
        )
    }
}

@Composable
fun FamilyGroupItem(family: FamilyResponse, currentUserId: Int, onRefresh: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var showUnsavedWarning by remember { mutableStateOf(false) }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var addMemberCitizenId by remember { mutableStateOf("") }
    var addMemberError by remember { mutableStateOf("") }

    // ตัวแปรสำคัญ: ป้องกันการกดซ้ำขณะประมวลผล
    var isProcessing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 1. Alert ดักลืมบันทึกก่อนกดย่อ
    if (showUnsavedWarning) {
        AlertDialog(
            onDismissRequest = { showUnsavedWarning = false },
            title = { Text("แจ้งเตือน") },
            text = { Text("คุณยังไม่ได้บันทึกการแก้ไข ต้องการปิดแถบนี้ใช่หรือไม่?") },
            confirmButton = {
                TextButton(onClick = { isEditMode = false; expanded = false; showUnsavedWarning = false }) {
                    Text("ใช่", color = Color.Red)
                }
            },
            dismissButton = { TextButton(onClick = { showUnsavedWarning = false }) { Text("ยกเลิก") } }
        )
    }

    // 2. Alert เพิ่มสมาชิกพร้อมเช็ค Error
    if (showAddMemberDialog) {
        AlertDialog(
            onDismissRequest = { showAddMemberDialog = false; addMemberError = "" },
            title = { Text("เพิ่มสมาชิก") },
            text = {
                Column {
                    if (addMemberError.isNotEmpty()) Text(addMemberError, color = Color.Red)
                    OutlinedTextField(
                        value = addMemberCitizenId,
                        onValueChange = { if (it.length <= 13) addMemberCitizenId = it; addMemberError = "" },
                        label = { Text("เลขบัตรประชาชน 13 หลัก") }
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = !isProcessing,
                    onClick = {
                        isProcessing = true
                        scope.launch {
                            val res = RetrofitClient.instance.addMemberToFamily(family.idFamily, addMemberCitizenId)
                            if (res.isSuccessful) {
                                showAddMemberDialog = false; addMemberCitizenId = ""; onRefresh()
                            } else {
                                addMemberError = "บุคคลนี้มีครอบครัวอยู่แล้ว"
                            }
                            isProcessing = false
                        }
                    }
                ) { Text("เพิ่ม") }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FF))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: กดย่อ/ขยาย พร้อมระบบดักลืมบันทึก
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    if (isEditMode && expanded) showUnsavedWarning = true else expanded = !expanded
                }.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("ครอบครัว ${family.family_name}", fontWeight = FontWeight.Bold)
                Icon(imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null)
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            family.members.forEach { member ->
                                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("${member.firstName} ${member.lastName}")
                                    // ลบสมาชิกรายคน (ห้ามลบตัวเอง)
                                    if (isEditMode && member.iduser != currentUserId) {
                                        IconButton(
                                            enabled = !isProcessing,
                                            onClick = {
                                                isProcessing = true
                                                scope.launch {
                                                    val res = RetrofitClient.instance.removeMemberFromFamily(member.iduser)
                                                    if (res.isSuccessful) onRefresh()
                                                    isProcessing = false
                                                }
                                            }
                                        ) { Icon(Icons.Default.Delete, tint = if(isProcessing) Color.Gray else Color.Red, contentDescription = null) }
                                    }
                                }
                            }
                            if (isEditMode) {
                                OutlinedButton(onClick = { showAddMemberDialog = true }, modifier = Modifier.fillMaxWidth(), enabled = !isProcessing) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Text("เพิ่มสมาชิกใหม่")
                                }
                            }
                        }
                    }

                    // ปุ่มแก้ไข และ ปุ่มลบครอบครัว
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { isEditMode = !isEditMode },
                            modifier = Modifier.weight(1f),
                            enabled = !isProcessing,
                            colors = ButtonDefaults.buttonColors(containerColor = if (isEditMode) Color(0xFF4CAF50) else Color(0xFF2F5DAA))
                        ) { Text(if (isEditMode) "บันทึกการแก้ไข" else "แก้ไขสมาชิก") }

                        Button(
                            onClick = {
                                isProcessing = true
                                scope.launch {
                                    val res = RetrofitClient.instance.deleteWholeFamilyById(family.idFamily)
                                    if (res.isSuccessful) onRefresh()
                                    isProcessing = false
                                }
                            },
                            enabled = !isProcessing,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) { Text("ลบ") }
                    }
                }
            }
        }
    }
}