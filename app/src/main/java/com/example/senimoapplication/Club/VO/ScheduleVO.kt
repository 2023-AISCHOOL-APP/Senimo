package com.example.senimoapplication.Club.VO


import com.google.gson.annotations.SerializedName

data class ScheduleVO(
    @SerializedName("club_name")
    val clubName: String="",
    @SerializedName("sche_title")
    val scheduleName: String ="",
    @SerializedName("sche_content")
    val scheduleIntro: String="",
    @SerializedName("sche_date")
    val scheduleDate: String = "",
    @SerializedName("fee")
    val scheduleFee: Int =0,
    @SerializedName("sche_location")
    val scheduleLoca: String="",
    @SerializedName("max_num")
    val allMembers: Int =0,
    @SerializedName("attend_user_cnt")
    val attendance: Int= 0,
    val state: String = "모집중",
    @SerializedName("sche_img_url")
    var imageUri: String // 이미지 경로를 String 타입으로 저장
)


//    @SerializedName("sche_img")
//    val scheImg: Int = 0,
//    val state: String = "모집중")




