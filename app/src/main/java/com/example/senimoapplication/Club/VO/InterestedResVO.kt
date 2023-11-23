package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName

class InterestedResVO (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)