package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

data class JoinClubResVO (
  @SerializedName("rows")
  val rows: String,

  @SerializedName("club_code")
  val clubCode : String,
  @SerializedName("user_id")
  val userId : String
)