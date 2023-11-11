package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

class ScheduleVO(
@SerializedName("Club_name")
val scheduleName : String ="",
@SerializedName("SCHE_TITLE")
val scheduleIntro :String="",
@SerializedName("SCHE_DATE")
val scheduleDate : String = "",
@SerializedName("FEE")
val scheduleFee : String="",
@SerializedName("SCHE_LOCATION")
val scheduleLoca : String="",
@SerializedName("참가인원")
val allMembers : Int =0,
@SerializedName("MAX_NUM")
val attendance : Int= 0,
val state : String = "모집중")