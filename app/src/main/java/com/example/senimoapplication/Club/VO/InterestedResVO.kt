package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

class InterestedResVO (
    @SerializedName("result")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)