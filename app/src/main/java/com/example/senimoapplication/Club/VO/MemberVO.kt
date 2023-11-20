package com.example.senimoapplication.Club.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MemberVO (
    @SerializedName("user_name")
    val userName : String ="",
    @SerializedName("club_role")
    val clubRole : Int = 3,
    @SerializedName("user_img")
    val imgUri : String? = null

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
            userName = parcel.readString() ?: "",
            clubRole = parcel.readInt(),
            imgUri = parcel.readString() ?: "",
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(gu)

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
    constructor(userName: String, clubRole : Int) : this(userName, clubRole, null)
    }


