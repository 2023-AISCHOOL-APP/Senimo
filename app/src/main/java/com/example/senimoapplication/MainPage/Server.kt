package com.example.senimoapplication.MainPage

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Server {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.70.234:3000") // 실제 서버 주소
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

