package com.example.senimoapplication.server

import android.content.Context
import com.example.senimoapplication.server.Retrofit.ApiService
import com.example.senimoapplication.server.Token.AuthInterceptor
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class Server(private val context: Context) {
    val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        Retrofit.Builder()
            //.baseUrl("https://improved-sadly-snake.ngrok-free.app") // 원격서버 URL
            //.baseUrl("http://192.168.70.207:3333") // 도운IP 같은 와이파로 접근할때
            //.baseUrl("http://192.168.70.205:3333") // 지혜IP
            .baseUrl("http://192.168.0.11:3333") // 희준IP
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
// activity는 val server = Server(this)
// fragment val server = Server(requireContext()) : requireContext()는 현재 Fragment가 Activity에 붙어 있을 때만 호출해야 합니다.
