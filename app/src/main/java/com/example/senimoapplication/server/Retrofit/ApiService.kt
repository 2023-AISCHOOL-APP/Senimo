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
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Club.VO.QuitClubResVO
import com.example.senimoapplication.Club.VO.RoleResVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.VO.UpdateMemberVO
import com.example.senimoapplication.Club.VO.WritePostResVO
import com.example.senimoapplication.Club.VO.WriteReviewResVO
import com.example.senimoapplication.Club.VO.getPostResVO
import com.example.senimoapplication.Club.VO.getReviewResVO
import com.example.senimoapplication.Club.VO.loadGalleryVO
import com.example.senimoapplication.Login.VO.SignUpResVO
import com.example.senimoapplication.MainPage.VO_main.ChatListVO
import com.example.senimoapplication.MainPage.VO_main.BadgeRes
import com.example.senimoapplication.MainPage.VO_main.CombinedDataResVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.MainPage.VO_main.UserDropOutResVO
import com.example.senimoapplication.MainPage.VO_main.getMyPageVO
import com.example.senimoapplication.server.Token.TokenResponse
import com.example.senimoapplication.server.Token.TokenValidationResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import com.google.gson.JsonObject
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
import retrofit2.http.Query


interface ApiService {
    @GET("/getMeetings")
    fun getMeetings(): Call<List<MeetingVO>>

    @Multipart
    @POST("/upload")
    fun uploadImage(@Part image: MultipartBody.Part): Call<ResponseBody>

    @GET("/getLatestSchedule")
    fun getLatestSchedule(@Query("userId") userId: String?): Call<List<ScheduleVO>>

    @GET("/getSchedules/{club_code}")
    fun getSchedules(@Path("club_code") clubCode: String): Call<AllSchedulesResVO>

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
    fun signUp(
        @Field("user_id") userId: String,
        @Field("user_pw") userPw: String,
        @Field("user_name") userName: String,
        @Field("gender") gender: String,
        @Field("birth_year") birthYear: Int,
        @Field("user_gu") userGu: String,
        @Field("user_dong") userDong: String,
        @Field("user_introduce") userIntroduce: String?
    ): Call<SignUpResVO>

    @POST("/validateToken")
    fun validateToken(@Header("Authorization") token: String): Call<TokenValidationResponse>

    @FormUrlEncoded
    @POST("/checkUserId")
    fun checkId(@Field("user_id") userId: String): Call<SignUpResVO>

    @Multipart
    @POST("/makeSche")
    fun createSchedule(
        @Part("makeSche") scheduleVO: ScheduleVO,
        @Part image: MultipartBody.Part?
    ): Call<MakeScheResVo>

    @Multipart
    @POST("/updateSche")
    fun updateSchedule(
        @Part("updateSche") updateVO: ScheduleVO,
        @Part image: MultipartBody.Part?
    ): Call<MakeScheResVo>

    @Multipart
    @POST("/modifyMeeting")
    fun modifyMeeting(
        @Part("modifyMeeting") meetingVO: MeetingVO,
        @Part image: MultipartBody.Part?
    ): Call<MeetingVO>

    @FormUrlEncoded
    @POST("/updateInterestedClub")
    fun updateInterestStatus(
        @Field("club_code") clubCode: String,
        @Field("user_id") userId: String
    ): Call<InterestedResVO>

    @FormUrlEncoded
    @POST("/initialInterestedClub")
    fun initialInterestedClub(
        @Field("club_code") clubCode: String,
        @Field("user_id") userId: String
    ): Call<InterestedResVO>

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
    fun joinSche(
        @Field("user_id") userId: String?,
        @Field("sche_code") scheCode: String?
    ): Call<JoinScheResVO>

    @FormUrlEncoded
    @POST("/cancelJoinSche")
    fun cancelJoinSche(
        @Field("user_id") userId: String?,
        @Field("sche_code") scheCode: String?
    ): Call<CancelJoinScheResVO>

    @FormUrlEncoded
    @POST("/joinClub")
    fun joinClub(
        @Field("club_code") clubCode: String?,
        @Field("user_id") userId: String?
    ): Call<JoinClubResVO>

    @FormUrlEncoded
    @POST("/quitClub")
    fun quitClub(
        @Field("club_code") clubCode: String?,
        @Field("user_id") userId: String?
    ): Call<QuitClubResVO>

    @POST("/editMyProfile")
    fun updateUserProfile(@Body profile: MyPageVO): Call<getMyPageVO>

    @GET("/getUserProfile")
    fun getUserProfile(@Query("userId") userId: String?): Call<MyPageVO>

    // 게시판 작성하기
    @Multipart
    @POST("/writePost")
    fun writePost(
        @Part("writePostResVO") writePostResVO: WritePostResVO,
        @Part image: MultipartBody.Part?
    ): Call<WritePostResVO>

    @Multipart
    @POST("/updatePost") // 서버의 게시물 수정
    fun updatePost(
        @Part("postVO") postVO: PostVO,// 수정된 게시물 내용
        @Part imagePart: MultipartBody.Part? // 첨부 이미지 (optional)
    ): Call<PostVO>

    // 여러 개의 사진을 업로드하는 메소드
    @Multipart
    @POST("/uploadPhotos")
    fun uploadPhotos(
        @Part("galleryInfo") galleryVO: GalleryVO,
        @Part photos: List<MultipartBody.Part>
    ): Call<GalleryVO>

    // 사진첩 정보 조회
    @GET("/getGallery")
    fun getGallery(@Query("club_code") clubCode: String?) : Call<List<loadGalleryVO>>

    @GET("/getPostContent/{club_code}")
    fun getPostContent(@Path("club_code") clubCode: String?): Call<getPostResVO>

    @GET("/getReview/{post_code}")
    fun getReview(@Path("post_code") postCode: String?): Call<getReviewResVO>

    @FormUrlEncoded
    @POST("/deletePost")
    fun deletePost(@Field("post_code") postCode: String): Call<DeletePostResVO>

    @GET("/getCombinedData/{user_id}")
    fun getCombinedData(@Path("user_id") userId: String?): Call<CombinedDataResVO>

    @POST("/writeReview")
    fun writeReview(@Body writeReviewResVO: WriteReviewResVO): Call<WriteReviewResVO>

    @FormUrlEncoded
    @POST("/deleteSche")
    fun deleteSche(@Field("sche_code") scheCode: String?): Call<DeleteScheResVO>

    @GET("/loadChatRooms")
    fun getChatRooms(@Query("userId") userId: String) : Call<List<ChatListVO>>

    @GET("/getMeeting/{sche_code}")
    fun getMeeting(@Path("sche_code") scheCode: String): Call<MeetingVO>

    @FormUrlEncoded
    @POST("userDropOut")
    fun userDropOut(@Field("user_id") userId: String?): Call<UserDropOutResVO>

    @FormUrlEncoded
    @POST("/getUserRole")
    fun getUserRole(
        @Field("club_code") clubCode: String?,
        @Field("user_id") userId: String?
    ): Call<RoleResVO>

    @GET("/getUserBadge/{user_id}")
    fun getUserBadge(@Path("user_id") userId: String?): Call<BadgeRes>

}


