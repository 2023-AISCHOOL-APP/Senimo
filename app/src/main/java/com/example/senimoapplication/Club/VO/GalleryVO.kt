package com.example.senimoapplication.Club.VO

import com.google.gson.annotations.SerializedName
import java.io.Serializable
class GalleryVO (
    @SerializedName("user_img")
    val userImg: String = "",
    @SerializedName("club_role")
    val clubRole: Int = 0,
    @SerializedName("user_name")
    val userName: String = "",
    @SerializedName("uploaded_dt")
    val uploadedDt: String = " ",
    @SerializedName("img_name")
    val imgUri: Int = 0
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

//class GalleryVO (
//    // 서버로 보내는 값
//    // 회원아이디(user_id), 모임코드(club_code), 이미지 이름 (img_name)
//    @SerializedName("user_img")
//    val userImg : String = "",
//    @SerializedName("club_role")
//    val clubRole : Int = 0,
//    @SerializedName("user_name")
//    val userName : String ="",
//    @SerializedName("uploaded_dt")
//    val uploadedDt : String = " ",
//    //@SerializedName("photo_likes")
//    //var photoLikes : Int = 0,
//    @SerializedName("img_name")
//    val imgUri : Int = 0,
//    //@SerializedName("photo_likes_state") // 이거 데이터베이스에 추가해야함
//    //var isLiked: Boolean = false
//) : Serializable