package com.example.senimoapplication.Club.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class CommentVO (
    @SerializedName("user_img")
    val userImg: String = "",
    @SerializedName("user_name")
    val userName: String = "",
    @SerializedName("review_content")
    val reviewContent: String = "",
    @SerializedName("reviewed_dt")
    val reviewedDt: String = "",
) : Parcelable{
  constructor(parcel: Parcel) : this(
    userImg = parcel.readString() ?: "",
    userName = parcel.readString() ?: "",
    reviewContent = parcel.readString() ?: "",
    reviewedDt = parcel.readString() ?: "",
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(userImg)
    parcel.writeString(userName)
    parcel.writeString(reviewContent)
    parcel.writeString(reviewedDt)
  }
  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<CommentVO> {
    override fun createFromParcel(parcel: Parcel): CommentVO {
      return CommentVO(parcel)
    }

    override fun newArray(size: Int): Array<CommentVO?> {
      return arrayOfNulls(size)
    }
  }
}

data class getReviewResVO(
  val data: List<CommentVO>
)