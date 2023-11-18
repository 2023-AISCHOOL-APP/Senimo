package com.example.senimoapplication.Login.VO

import com.google.gson.annotations.SerializedName

data class LoginResVO(
  @SerializedName("rows")
  val rows: String, // 성공 또는 실패 여부를 나타내는 필드

  @SerializedName("user_id")
  val userId: String?
)