package com.example.senimoapplication.MainPage.VO_main

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BadgeVO(
  @SerializedName("badge_get_code")
  val badgeGetCode: String = "",
  @SerializedName("badge_code")
  val badgeCode: String = "",
  @SerializedName("user_id")
  val userId: String = ""

): Parcelable {
    constructor(parcel: Parcel) : this(
      badgeGetCode = parcel.readString() ?: "",
      badgeCode = parcel.readString() ?: "",
      userId = parcel.readString() ?: "",
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(badgeGetCode)
    parcel.writeString(badgeCode)
    parcel.writeString(userId)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<BadgeVO> {
    override fun createFromParcel(parcel: Parcel): BadgeVO {
      return BadgeVO(parcel)
    }

    override fun newArray(size: Int): Array<BadgeVO?> {
      return arrayOfNulls(size)
    }
  }
}

data class BadgeRes(
  val badges: List<BadgeVO>,
  val badgeCnt: Int
)