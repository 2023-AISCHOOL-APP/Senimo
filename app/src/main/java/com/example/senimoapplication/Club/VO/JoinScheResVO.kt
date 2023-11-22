package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

data class JoinScheResVO (
  @SerializedName("rows")
  val rows: String,

  @SerializedName("user_id")
  val userId : String,
  @SerializedName("sche_code")
  val scheCode : String
)