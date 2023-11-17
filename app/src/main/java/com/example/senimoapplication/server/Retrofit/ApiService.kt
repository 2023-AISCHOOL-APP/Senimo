package com.example.senimoapplication.server.Retrofit

import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path



interface ApiService {
    @GET("/getMeetings")
    fun getMeetings(): Call<List<MeetingVO>>

//    @Multipart
//    @POST("/db/postImg")
//    fun postImg(@Part photo: MultipartBody.Part): Call<ResponseDC> // 사진을 업로드 하는 함수
//
//    @GET("/photos")
//    fun getPhotos(): Call<List<Photo>> // 서버에서 사진 목록을 가져오는 함수입니다.

    @GET("/getSche_intro/{sche_code}")
    fun getScheIntro(@Path("sche_code") sche_code: String): Call<ScheduleVO> // 특정 일정 ID를 사용하여 상세 정보 가져오기

    @GET("/getClubInfo/{club_code}")
    fun getClubInfo(@Path("club_code") club_code: String): Call<ClubInfoVO>

    @POST("/postcreateMeeting")
    fun createMeeting(): Call<List<MeetingVO>>
}

