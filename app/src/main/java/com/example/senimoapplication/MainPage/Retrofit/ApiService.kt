package com.example.senimoapplication.MainPage.Retrofit

import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import retrofit2.Call
import retrofit2.http.GET
z

interface ApiService {
    @GET("/getMeetings")
    fun getMeetings(): Call<List<MeetingVO>>

    @GET("/scheduleDetail/scheduleId")
    fun getscheduleId(): Call<List<ScheduleVO>>

}

