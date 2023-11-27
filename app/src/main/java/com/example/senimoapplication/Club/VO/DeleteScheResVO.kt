package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

data class DeleteScheResVO (
  @SerializedName("rows")
  val rows: String,

  @SerializedName("sche_code")
  val scheCode : String
)