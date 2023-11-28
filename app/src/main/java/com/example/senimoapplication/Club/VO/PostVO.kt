package com.example.senimoapplication.Club.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PostVO (
    @SerializedName("post_code")
    val postCode: String = "",
    @SerializedName("user_img")
    val userImg: String?= null,
    @SerializedName("user_name")
    val userName: String = "",
    @SerializedName("created_dt")
    val createdDt: String = "",
    @SerializedName("post_content")
    val postContent: String = "",
    @SerializedName("post_img")
    val postImg: String? = null,
    @SerializedName("club_role")
    val clubRole: Int = 0,
    @SerializedName("review_count")
    val reviewCount: Int = 0,
    @SerializedName("user_id")
    val userId: String = "",
    val imageChanged: Boolean = false

): Parcelable {
    constructor(parcel: Parcel) : this(
        postCode = parcel.readString() ?: "",
        userImg = parcel.readString() ?: "",
        userName = parcel.readString() ?: "",
        createdDt = parcel.readString() ?: "",
        postContent = parcel.readString() ?: "",
        postImg = parcel.readString() ?: "",
        clubRole = parcel.readInt(),
        reviewCount = parcel.readInt(),
        userId = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postCode)
        parcel.writeString(userImg)
        parcel.writeString(userName)
        parcel.writeString(createdDt)
        parcel.writeString(postContent)
        parcel.writeString(postImg)
        parcel.writeInt(clubRole)
        parcel.writeInt(reviewCount)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostVO> {
        override fun createFromParcel(parcel: Parcel): PostVO {
            return PostVO(parcel)
        }

        override fun newArray(size: Int): Array<PostVO?> {
            return arrayOfNulls(size)
        }
    }
}


data class getPostResVO(
    val data: List<PostVO>
)