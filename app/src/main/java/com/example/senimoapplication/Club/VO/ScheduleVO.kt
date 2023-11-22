package com.example.senimoapplication.Club.VO


import com.google.gson.annotations.SerializedName

data class ScheduleVO(
    @SerializedName("club_name")
    val clubName: String="",
    @SerializedName("sche_title")
    val scheTitle: String ="",
    @SerializedName("sche_content")
    val scheContent: String="",
    @SerializedName("sche_date")
    val scheDate: String = "",
    @SerializedName("fee")
    val scheFee: Int =0,
    @SerializedName("sche_location")
    val scheLoca: String="",
    @SerializedName("max_num")
    val maxNum: Int =0,
    @SerializedName("attend_user_cnt")
    val attendUserCnt: Int= 0,
    val state: String = "모집중",
    @SerializedName("sche_img")
    var scheImg: String // 이미지 경로를 String 타입으로 저장
)


//    @SerializedName("sche_img")
//    val scheImg: Int = 0,
//    val state: String = "모집중")




