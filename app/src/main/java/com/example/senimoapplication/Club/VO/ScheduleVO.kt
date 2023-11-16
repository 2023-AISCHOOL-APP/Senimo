package com.example.senimoapplication.Club.VO


import com.google.gson.annotations.SerializedName

data class ScheduleVO(
    @SerializedName("club_name")
    val clubName: String="",
    @SerializedName("sche_title")
    val scheduleName: String ="",
    @SerializedName("sche_content")
    val scheduleIntro:String="",
    @SerializedName("sche_date")
    val scheduleDate: String = "",
    @SerializedName("fee")
    val scheduleFee: Int =0,
    @SerializedName("sche_location")
    val scheduleLoca: String="",
    @SerializedName("max_num")
    val allMembers: Int =0,
    @SerializedName("joined_user_count")
    val attendance: Int= 0,
    val state: String = "모집중")



