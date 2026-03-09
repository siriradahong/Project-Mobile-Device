package com.example.myapplication100.NurseScreen


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication100.DataClass.Appointment_Examination.VitalsRequestnurse
import com.example.myapplication100.LoginRegis.RetrofitClient
import kotlinx.coroutines.launch

class VitalsViewModel : ViewModel() {

    fun saveVitals(
        context: Context,
        vitals: VitalsRequestnurse,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            try {

                val response = RetrofitClient.instance.saveVitals(vitals)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    Toast.makeText(context,"เกิดข้อผิดพลาด",Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {

                Toast.makeText(context,"เกิดข้อผิดพลาด",Toast.LENGTH_SHORT).show()

            }

        }

    }

}