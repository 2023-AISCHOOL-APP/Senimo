package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

data class MakeScheResVo (
  @SerializedName("rows")
  val rows: String,
  @SerializedName("sche_code")
  val scheCode : String?,
  @SerializedName("club_code")
  val clubCode: String?,
  @SerializedName("sche_title")
  val scheTitle: String?,
  @SerializedName("sche_content")
  val scheContent: String?,
  @SerializedName("sche_date")
  val scheDate: String?,
  @SerializedName("sche_location")
  val scheLocation: String?,
  @SerializedName("max_num")
  val maxNum: Int?,
  @SerializedName("fee")
  val scheFee: Int?,
  @SerializedName("sche_img")
  val scheImg: String? =null
)