package com.example.myapplication100
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HistoryItem(date: String, expanded: Boolean) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(Color(0xFFE5EFFF))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("รายการเมื่อ : $date", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4E4E4E))
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.Gray)
        }

        if (expanded) {
            Spacer(Modifier.height(12.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(17.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text("อาการ : ตัวร้อน คัดจมูก เจ็บคอ ไอมีเสมหะ", color = Color(0xFF4E4E4E))
                Spacer(Modifier.height(6.dp))
                Text("ผลวินิจฉัย : ไข้หวัดใหญ่", color = Color(0xFF4E4E4E))
                Spacer(Modifier.height(6.dp))
                Text("ยาที่ได้รับ : ยาฆ่าเชื้อ ยาแก้หวัด ยาแก้เจ็บคอ", color = Color(0xFF4E4E4E))
            }
        }
    }
}