package com.example.senimoapplication.MainPage.VO_main

import com.google.gson.annotations.SerializedName

data class UserDropOutResVO (
  @SerializedName("rows")
  val rows: String,

  @SerializedName("user_id")
  val userId: String
)