package com.example.senimoapplication.Club.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ScheduleMemberVO (
    @SerializedName("user_id")
    val userId : String ="",
    @SerializedName("user_name")
    val userName : String ="",
    @SerializedName("club_role")
    val clubRole : Int =0,
    @SerializedName("user_img")
    val userImg : String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        userId = parcel.readString() ?: "",
        userName = parcel.readString() ?: "",
        clubRole = parcel.readInt(),
        userImg = parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(gu)
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeInt(clubRole)
        parcel.writeString(userImg)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScheduleMemberVO> {
        override fun createFromParcel(parcel: Parcel): ScheduleMemberVO {
            return ScheduleMemberVO(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleMemberVO?> {
            return arrayOfNulls(size)
        }
    }

}
data class AllScheduleMemberResVO(
    val data : List<ScheduleMemberVO>
)