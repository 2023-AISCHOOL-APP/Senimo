package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GalleryVO (
    // 서버로 보내는 값
    // 회원아이디(user_id), 모임코드(club_code), 이미지 이름 (img_name)
    @SerializedName("user_img")
    val userImg : String = "",
    @SerializedName("club_role")
    val clubRole : Int = 0,
    @SerializedName("user_name")
    val userName : String ="",
    @SerializedName("uploaded_dt")
    val uploadedDt : String = " ",
    @SerializedName("photo_likes")
    val photoLikes : Int = 0,
    @SerializedName("img_name")
    val imgUri : Int = 0,
) : Serializable