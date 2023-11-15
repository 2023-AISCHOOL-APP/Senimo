package com.example.senimoapplication.server

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Server {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://192.168.70.234:3333") // 도운IP
        .baseUrl("http://192.168.70.29:3333") // 희준IP
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

