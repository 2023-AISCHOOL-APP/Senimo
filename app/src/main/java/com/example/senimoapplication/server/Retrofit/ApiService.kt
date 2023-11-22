package com.example.senimoapplication.server.Retrofit

import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Login.VO.SignUpResVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.server.Token.TokenResponse
import com.example.senimoapplication.server.Token.TokenValidationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path



interface ApiService {
    @GET("/getMeetings")
    fun getMeetings(): Call<List<MeetingVO>>

    @Multipart
    @POST("/upload")
    fun uploadImage(@Part image: MultipartBody.Part) : Call<ResponseBody>
//    @Multipart
//    @POST("/db/postImg")
//    fun postImg(@Part photo: MultipartBody.Part): Call<ResponseDC> // 사진을 업로드 하는 함수
//
//    @GET("/photos")
//    fun getPhotos(): Call<List<Photo>> // 서버에서 사진 목록을 가져오는 함수입니다.

//    @FormUrlEncoded
//    @POST("/refreshToken") // 새로운 액세스 토큰 요청
//    fun refreshToken(@Field("refreshToken") refreshToken: String): Call<TokenResponse>

    @GET("/getSche_intro/{sche_code}")
    fun getScheIntro(@Path("sche_code") sche_code: String): Call<ScheduleVO> // 특정 일정 ID를 사용하여 상세 정보 가져오기

    @GET("/getClubInfo/{club_code}")
    fun getClubInfo(@Path("club_code") club_code: String): Call<ClubInfoVO>

    @Multipart
    @POST("/postCreateMeeting")
    fun createMeeting(
        @Part("meeting") meeting: RequestBody, // JSON 형식의 MeetingVO @part 매개변수는 RequestBody , //서버 측에서는 이 "meeting" 파트를 찾아 그 내용을 읽고 처리
        @Part image: MultipartBody.Part? // 이미지 파일 데이터를 전달하는 역할
    ): Call<MeetingVO>

    @FormUrlEncoded
    @POST("/login")
    fun loginUser(@Field("user_id") userId: String,
                  @Field("user_pw") userPw: String):Call<TokenResponse> //Call<LoginResVO>

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

    @POST("/validateToken")
    fun validateToken(@Header("Authorization") token: String): Call<TokenValidationResponse>
}



