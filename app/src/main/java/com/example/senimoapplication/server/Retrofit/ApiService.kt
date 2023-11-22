package com.example.senimoapplication.server.Retrofit

import com.example.senimoapplication.Club.VO.AllMemberResVO
import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.Club.VO.DeleteMemberVO
import com.example.senimoapplication.Club.VO.InterestedResVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.VO.UpdateMemberVO
import com.example.senimoapplication.Login.VO.SignUpResVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.VO_main.modifyResult
import com.example.senimoapplication.server.Token.TokenResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
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

    @FormUrlEncoded
    @POST("/refreshToken") // 새로운 액세스 토큰 요청
    fun refreshToken(@Field("refreshToken") refreshToken: String): Call<TokenResponse>

    @GET("/getSche_intro/{sche_code}")
    fun getScheIntro(@Path("sche_code") sche_code: String): Call<ScheduleVO> // 특정 일정 ID를 사용하여 상세 정보 가져오기

    @GET("/getClubInfo/{club_code}")
    fun getClubInfo(@Path("club_code") club_code: String): Call<ClubInfoVO>

    @POST("/postcreateMeeting")
    fun createMeeting(@Body meetingVO: MeetingVO): Call<MeetingVO>

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
    @FormUrlEncoded
    @POST("/checkUserId")
    fun checkId(@Field("user_id") userId: String): Call<SignUpResVO>

    @POST("/postModifyMeeting")
    fun modifyMeeting(@Body meetingVO: MeetingVO) : Call<modifyResult>

    @FormUrlEncoded
    @POST("/updateInterestedClub")
    fun updateInterestStatus(@FieldMap params: Map<String, String>): Call<InterestedResVO>

    @POST("/getAllMembers/{clubCode}")
    fun getAllMembers(@Path("clubCode") clubCode: String) : Call<AllMemberResVO>

    @POST("/updateMember")
    fun updateMember(@Body updateMemberVO: UpdateMemberVO): Call<JsonObject>

    @POST("/deleteMember")
    fun deleteMember(@Body deleteMemberVO: DeleteMemberVO): Call<JsonObject>
}



