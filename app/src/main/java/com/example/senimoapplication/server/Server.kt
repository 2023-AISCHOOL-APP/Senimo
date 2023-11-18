package com.example.senimoapplication.server

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Server {
    val retrofit = Retrofit.Builder()

        // .baseUrl("http://192.168.70.207:3333") // 도운IP cmd 들어가서 ipconfig 입력후 ipv4주소입력
         .baseUrl("http://192.168.70.20:3333")
        // senimo 서버로 vscode 열어서 터미널에 nodemon server.js 입력, 안되면 node server.js 입력
        //.baseUrl("http://172.16.11.223:3333") // 희준IP
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}

