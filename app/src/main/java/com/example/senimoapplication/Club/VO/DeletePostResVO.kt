package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

data class DeletePostResVO (
  @SerializedName("rows")
  val rows: String,

  @SerializedName("post_code")
  val postCode : String
)