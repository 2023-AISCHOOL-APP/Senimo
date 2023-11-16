package com.example.senimoapplication.MainPage.Retrofit

import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/getMeetings")
    fun getMeetings(): Call<List<MeetingVO>>

    @GET("/getClubInfo/{club_code}")
    fun getClubInfo(@Path("club_code") club_code: String): Call<ClubInfoVO>
}

