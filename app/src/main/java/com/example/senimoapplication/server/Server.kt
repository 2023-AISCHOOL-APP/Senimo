package com.example.senimoapplication.server

import com.example.senimoapplication.server.Retrofit.ApiService
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Server {
    val retrofit = Retrofit.Builder()
<<<<<<< Updated upstream
        .baseUrl("https://improved-sadly-snake.ngrok-free.app") // 도운IP
        //.baseUrl("http://192.168.70.154:3333") // 희준IP
=======


        // senimo 서버로 vscode 열어서 터미널에 nodemon server.js 입력, 안되면 node server.js 입력
//        .baseUrl("http://192.168.70.207:3333") // 도운IP
        .baseUrl("http://192.168.70.154:3333") // 희준IP
>>>>>>> Stashed changes
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(ApiService::class.java)
}
