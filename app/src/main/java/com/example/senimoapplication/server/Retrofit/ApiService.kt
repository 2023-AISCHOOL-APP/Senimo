package com.example.senimoapplication.server.Retrofit

import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Login.VO.LoginResVO
import com.example.senimoapplication.Login.VO.SignUpResVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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
    fun createMeeting(): Call<Void>

    @FormUrlEncoded
    @POST("/login")
    fun loginUser(@Field("user_id") userId: String,
                  @Field("user_pw") userPw: String): Call<LoginResVO>

    @FormUrlEncoded
    @POST("/signup")
    fun signUp(@Field("user_id") userId: String,
               @Field("user_pw") userPw: String,
               @Field("user_name") userName: String,
               @Field("gender") gender: String,
               @Field("birth_year") birthYear: Int,
               @Field("user_gu") userGu: String,
               @Field("user_dong") userDong: String,
               @Field("user_introduce") userIntroduce: String?): Call<SignUpResVO>
}

