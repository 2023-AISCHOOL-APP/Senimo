package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

data class WritePostResVO(
  @SerializedName("rows")
  val rows: String,

  @SerializedName("user_id")
  val userId : String,
  @SerializedName("club_code")
  val clubCode : String,
  @SerializedName("post_content")
  val postContent : String?,
  @SerializedName("post_img")
  val postImg : String?
)
