package com.example.myapplication100.LoginRegis

import com.example.myapplication100.DataClass.HospitalApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/" // ใช้ 10.0.2.2 สำหรับ Android Emulator

    val instance: HospitalApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(HospitalApiService::class.java)
    }
}