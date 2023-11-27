package com.example.senimoapplication.server.Retrofit

import com.example.senimoapplication.Club.VO.AllMemberResVO
import com.example.senimoapplication.Club.VO.AllScheduleMemberResVO
import com.example.senimoapplication.Club.VO.AllSchedulesResVO
import com.example.senimoapplication.Club.VO.CancelJoinScheResVO
import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.Club.VO.DeleteMemberVO
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.Club.VO.DeletePostResVO
import com.example.senimoapplication.Club.VO.DeleteScheResVO
import com.example.senimoapplication.Club.VO.MakeScheResVo
import com.example.senimoapplication.Club.VO.InterestedResVO
import com.example.senimoapplication.Club.VO.JoinClubResVO
import com.example.senimoapplication.Club.VO.JoinScheResVO
import com.example.senimoapplication.Club.VO.QuitClubResVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.VO.UpdateMemberVO
import com.example.senimoapplication.Club.VO.WritePostResVO
import com.example.senimoapplication.Club.VO.WriteReviewResVO
import com.example.senimoapplication.Club.VO.getPostResVO
import com.example.senimoapplication.Club.VO.getReviewResVO
import com.example.senimoapplication.Login.VO.SignUpResVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.MainPage.VO_main.MyScheduleVO
import com.example.senimoapplication.MainPage.VO_main.UserBadgeResponse
import com.example.senimoapplication.MainPage.VO_main.getMyPageVO
import com.example.senimoapplication.MainPage.VO_main.modifyResult
import com.example.senimoapplication.server.Token.TokenResponse
import com.example.senimoapplication.server.Token.TokenValidationResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("/getMeetings")
    fun getMeetings(): Call<List<MeetingVO>>


    @Multipart
    @POST("/upload")
    fun uploadImage(@Part image: MultipartBody.Part) : Call<ResponseBody>

    @GET("/getLatestSchedule")
    fun getLatestSchedule(@Query("userId") userId: String?): Call<List<ScheduleVO>>

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

    @GET("/getSchedules/{club_code}")
    fun getSchedules(@Path("club_code") clubCode: String) : Call<AllSchedulesResVO>

    @GET("/getClubInfo/{club_code}")
    fun getClubInfo(@Path("club_code") club_code: String): Call<ClubInfoVO>

    @Multipart
    @POST("/postCreateMeeting")
    fun createMeeting(
        @Part("meeting") meeting: MeetingVO, // JSON 형식의 MeetingVO @part 매개변수는 RequestBody , //서버 측에서는 이 "meeting" 파트를 찾아 그 내용을 읽고 처리
        @Part image: MultipartBody.Part? // 이미지 파일 데이터를 전달하는 역할
    ): Call<MeetingVO>

    @FormUrlEncoded
    @POST("/login")
    fun loginUser(
        @Field("user_id") userId: String,
        @Field("user_pw") userPw: String
    ): Call<TokenResponse> //Call<LoginResVO>

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

    @FormUrlEncoded
    @POST("/checkUserId")
    fun checkId(@Field("user_id") userId: String): Call<SignUpResVO>

    @FormUrlEncoded
    @POST("/makeSche")
    fun createSchedule(
        @Field("club_code") clubCode: String,
        @Field("sche_title") scheTitle: String,
        @Field("sche_content") scheContent: String,
        @Field("sche_date") scheDate: String,
        @Field("sche_location") scheLocation: String,
        @Field("max_num") maxNum: Int,
        @Field("fee") scheFee: Int,
        @Field("sche_img") scheImg: String?
    ): Call<MakeScheResVo>

    @Multipart
    @POST("/modifyMeeting")
    fun modifyMeeting(@Part("modifyMeeting") meetingVO: MeetingVO,
                  @Part image: MultipartBody.Part?
    ) : Call<MeetingVO>

    @FormUrlEncoded
    @POST("/updateInterestedClub")
    fun updateInterestStatus(@FieldMap params: Map<String, String>): Call<InterestedResVO>

    @POST("/getAllMembers/{clubCode}")
    fun getAllMembers(@Path("clubCode") clubCode: String): Call<AllMemberResVO>

    @POST("/updateMember")
    fun updateMember(@Body updateMemberVO: UpdateMemberVO): Call<JsonObject>

    @POST("/updateLeader")
    fun updateLeader(@Body updateMemberVO: UpdateMemberVO): Call<JsonObject>

    @POST("/deleteMember")
    fun deleteMember(@Body deleteMemberVO: DeleteMemberVO): Call<JsonObject>

    @GET("/getScheduleMembers/{sche_code}")
    fun getScheduleMembers(@Path("sche_code") scheCode: String): Call<AllScheduleMemberResVO>

    @FormUrlEncoded
    @POST("/joinSche")
    fun joinSche(@Field("user_id") userId: String?,
                 @Field("sche_code") scheCode: String?
    ) : Call<JoinScheResVO>

    @FormUrlEncoded
    @POST("/cancelJoinSche")
    fun cancelJoinSche(@Field("user_id") userId: String?,
                       @Field("sche_code") scheCode: String?
    ) : Call<CancelJoinScheResVO>

    @FormUrlEncoded
    @POST("/joinClub")
    fun joinClub(@Field("club_code") clubCode: String?,
                 @Field("user_id") userId: String?
    ) : Call<JoinClubResVO>

    @FormUrlEncoded
    @POST("/quitClub")
    fun quitClub(@Field("club_code") clubCode: String?,
                 @Field("user_id") userId: String?
    ) : Call<QuitClubResVO>


    @GET("/getUserBadges")
    fun getUserBadges(@Query("userId") userId: String?): Call<UserBadgeResponse>

    @POST("/editMyProfile")
    fun updateUserProfile(@Body profile: MyPageVO): Call<getMyPageVO>

    @GET("/getUserProfile")
    fun getUserProfile(@Query("userId") userId: String?): Call<MyPageVO>



    @FormUrlEncoded
    @POST("/writePost")
    fun writePost(@Field("user_id") userId: String,
                  @Field("club_code") clubCode: String,
                  @Field("post_content") postContent: String?,
                  @Field("post_img") postImg: String?) : Call<WritePostResVO>

//    @Multipart
//    @POST("/postCreateMeeting")
//    fun createMeeting(
//        @Part("meeting") meeting: MeetingVO, // JSON 형식의 MeetingVO @part 매개변수는 RequestBody , //서버 측에서는 이 "meeting" 파트를 찾아 그 내용을 읽고 처리
//        @Part image: MultipartBody.Part? // 이미지 파일 데이터를 전달하는 역할
//    ): Call<MeetingVO>

    // 게시판 작성하기
    @Multipart
    @POST("/writePost")
    fun writePost(@Part("writePostResVO") writePostResVO: WritePostResVO,
                  @Part image: MultipartBody.Part?
    ) : Call<WritePostResVO>

    // 여러 개의 사진을 업로드하는 메소드
    @Multipart
    @POST("/uploadPhotos")
    fun uploadPhotos(
        @Part("galleryInfo") galleryVO: GalleryVO,
        @Part photos: List<MultipartBody.Part>
    ): Call<GalleryVO>

    @GET("/getPostContent/{club_code}")
    fun getPostContent(@Path("club_code") clubCode: String?): Call<getPostResVO>

    @GET("/getReview/{post_code}")
    fun getReview(@Path("post_code") postCode: String?): Call<getReviewResVO>

    @FormUrlEncoded
    @POST("/deletePost")
    fun deletePost(@Field("post_code") postCode: String): Call<DeletePostResVO>

    @POST("/writeReview")
    fun writeReview(@Body writeReviewResVO: WriteReviewResVO): Call<WriteReviewResVO>

    @FormUrlEncoded
    @POST("/deleteSche")
    fun deleteSche(@Field("sche_code") scheCode: String?): Call<DeleteScheResVO>
}



