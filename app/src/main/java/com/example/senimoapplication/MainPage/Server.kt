package com.example.senimoapplication.MainPage

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Server {
    val retrofit = Retrofit.Builder()
        //.baseUrl("https://192.168.70.234:3333") // 도운IP
        .baseUrl("https://192.168.70.70:3333") // 희준IP
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

