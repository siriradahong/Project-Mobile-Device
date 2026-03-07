package com.example.myapplication100

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VitalsViewModel : ViewModel() {

    fun saveVitals(
        context: Context,
        vitals: VitalsRequest,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            try {

                RetrofitClient.api.saveVitals(vitals)

                Toast.makeText(context, "บันทึกข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show()

                onSuccess()

            } catch (e: Exception) {

                Toast.makeText(context, "เกิดข้อผิดพลาด", Toast.LENGTH_SHORT).show()

                Log.e("VITALS", "Error", e)

            }

        }

    }

}