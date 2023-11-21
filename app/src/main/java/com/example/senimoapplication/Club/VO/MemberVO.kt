package com.example.senimoapplication.Club.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MemberVO (
    @SerializedName("club_code")
    val clubCode : String ="",
    @SerializedName("user_id")
    val userId : String ="",
    @SerializedName("user_name")
    val userName : String ="",
    @SerializedName("club_role")
    var clubRole : Int = 3,
    @SerializedName("user_img")
    var imgUri : String? = null

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
            clubCode = parcel.readString() ?: "",
            userId = parcel.readString() ?: "",
            userName = parcel.readString() ?: "",
            clubRole = parcel.readInt(),
            imgUri = parcel.readString() ?: "",
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(gu)
            parcel.writeString(clubCode)
            parcel.writeString(userId)
            parcel.writeString(userName)
            parcel.writeInt(clubRole)
            parcel.writeString(imgUri)

        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MemberVO> {
            override fun createFromParcel(parcel: Parcel): MemberVO {
                return MemberVO(parcel)
            }

            override fun newArray(size: Int): Array<MemberVO?> {
                return arrayOfNulls(size)
            }
        }
    constructor(clubCode: String) : this(clubCode, "", "",0, "")
    }

data class MemberRoleResVO(
    val result : Boolean,
    val message : String
)

data class AllMemberResVO(
    val data: List<MemberVO>
)