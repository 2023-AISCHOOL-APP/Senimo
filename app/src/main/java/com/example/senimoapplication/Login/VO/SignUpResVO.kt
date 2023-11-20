package com.example.senimoapplication.Login.VO

import com.google.gson.annotations.SerializedName

data class SignUpResVO (
  @SerializedName("rows")
  val rows: String,

  @SerializedName("user_id")
  val userId: String?,
  @SerializedName("user_pw")
  val userPw: String?,
  @SerializedName("user_name")
  val userName: String?,
  @SerializedName("gender")
  val gender: String?,
  @SerializedName("birth_year")
  val birthYear: Int?,
  @SerializedName("user_gu")
  val userGu: String?,
  @SerializedName("user_dong")
  val userDong: String?,
  @SerializedName("user_introduce")
  val userIntroduce: String?
)