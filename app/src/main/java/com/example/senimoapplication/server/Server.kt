package com.example.senimoapplication.server

import com.example.senimoapplication.server.Retrofit.ApiService
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Server {
    val retrofit = Retrofit.Builder()

        .baseUrl("https://improved-sadly-snake.ngrok-free.app") // 도운IP
        //.baseUrl("http://192.168.70.154:3333") // 희준IP
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(ApiService::class.java)
}
