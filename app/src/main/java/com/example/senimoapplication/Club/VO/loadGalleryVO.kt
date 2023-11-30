package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class loadGalleryVO(
    @SerializedName("img_thumb_name")
    val imgThumbName: String? = "-",
    @SerializedName("user_name")
    val userName: String? = null,
    @SerializedName("club_role")
    val clubRole: Int = 0,
    @SerializedName("photo_dt")
    val uploadedDt: String = "",
    @SerializedName("user_img")
    val userImg: String?= null
) : Serializable
