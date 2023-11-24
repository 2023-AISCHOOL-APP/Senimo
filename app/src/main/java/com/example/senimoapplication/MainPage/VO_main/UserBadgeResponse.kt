package com.example.senimoapplication.MainPage.VO_main

import com.google.gson.annotations.SerializedName

data class UserBadgeResponse (
    val result: List<UserBadgeInfo>
)

data class UserBadgeInfo(
    @SerializedName("badge_code")
    val badgeCode : String,
    @SerializedName("badge_name")
    val badgeName : String,
    @SerializedName("badge_get_dt")
    val badgeGetDt : String,

    // 사용자 정보 필드
    @SerializedName("user_id")
    val userId : String,
    @SerializedName("user_name")
    val userName : String,
    @SerializedName("gender")
    val gender : String,
    @SerializedName("birth_year")
    val birthYear : Int,
    @SerializedName("user_gu")
    val userGu : String,
    @SerializedName("user_dong")
    val userDong : String,
    @SerializedName("user_introduce")
    val userIntroduce : String,
    @SerializedName("user_img")
    val userImg : String

)