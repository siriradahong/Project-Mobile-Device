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
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MedicineCard() {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(Color(0xFFE5EFFF))

            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("รายการยาปัจจุบัน", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4E4E4E))
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowUp, null, tint = Color.Gray)
        }

        Spacer(Modifier.height(12.dp))

        Column(
            Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(17.dp))
                .background(Color.White)
                .padding(16.dp)
        ){
            Text("ยาฆ่าเชื้อ :", fontWeight = FontWeight.Medium)
            Text("ทานก่อนอาหาร เช้า/กลางวัน/เย็น")
            Spacer(Modifier.height(8.dp))
            Text("ยาแก้เจ็บคอ :", fontWeight = FontWeight.Medium)
            Text("ทานก่อนอาหาร เช้า/กลางวัน/เย็น")
        }
    }
}
