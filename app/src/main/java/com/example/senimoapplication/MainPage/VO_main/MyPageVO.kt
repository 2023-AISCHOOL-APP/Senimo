package com.example.senimoapplication.MainPage.VO_main

import android.os.Parcel
import android.os.Parcelable

class MyPageVO(val img: String = "",            // 사진
               val name: String = "",      // 닉네임 또는 이름
               val gu: String = "",          // 지역 구
               val birth: Int = 0,             // 생년 (0000년생)
               val gender : String = "" ,      // 성별
               val intro: String = ""          // 소개글

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(img)
        parcel.writeString(name)
        parcel.writeString(gu)
        parcel.writeInt(birth)
        parcel.writeString(gender)
        parcel.writeString(intro)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyPageVO> {
        override fun createFromParcel(parcel: Parcel): MyPageVO {
            return MyPageVO(parcel)
        }

        override fun newArray(size: Int): Array<MyPageVO?> {
            return arrayOfNulls(size)
        }
    }
}
