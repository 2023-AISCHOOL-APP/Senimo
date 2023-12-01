package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName
import java.io.Serializable
class GalleryVO(
    @SerializedName("user_img")
    val userImg: String? = null,
    @SerializedName("club_role")
    val clubRole: Int = 0,
    @SerializedName("user_name")
    val userName: String? = null,
    @SerializedName("uploaded_dt")
    val uploadedDt: String = "",
    @SerializedName("img_name")
    val imgUri: Int = 0,
    @SerializedName("club_code")
    var clubcode: String? = null,
    @SerializedName("img_thumb_name")
    val imgThumbName: List<String?>,
    @SerializedName("user_id")
    var userId: String? = null,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GalleryVO

        return imgUri == other.imgUri
    }

    override fun hashCode(): Int {
        return imgUri
    }
}