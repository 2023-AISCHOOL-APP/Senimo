package com.example.senimoapplication.MainPage.Retrofit

import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/getMeetings")
    fun getMeetings(): Call<List<MeetingVO>>

}

