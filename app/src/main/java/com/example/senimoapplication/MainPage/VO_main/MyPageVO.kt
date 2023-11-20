package com.example.senimoapplication.MainPage.VO_main

import android.os.Parcel
import android.os.Parcelable

class MyPageVO(val img: String = "",            // 사진
               val name: String = "",      // 닉네임 또는 이름
               val gu: String = "",          // 지역 구
               val birth: Int = 0,             // 생년 (0000년생)
               val gender : String = "" ,      // 성별
               val intro: String = "",          // 소개글
               val badges : List<Boolean> = listOf() // 뱃지 활성화 상태 리스트 추가

) : Parcelable {
    constructor(parcel: Parcel) : this(
        img = parcel.readString() ?: "",
        name = parcel.readString() ?: "",
        gu = parcel.readString() ?: "",
        birth = parcel.readInt(),
        gender = parcel.readString() ?: "",
        intro = parcel.readString() ?: "",
        badges = List(parcel.readInt()) { parcel.readByte() != 0.toByte() }  // 뱃지 리스트 읽기

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(img)
        parcel.writeString(name)
        parcel.writeString(gu)
        parcel.writeInt(birth)
        parcel.writeString(gender)
        parcel.writeString(intro)
        parcel.writeInt(badges.size) // 뱃지 리스트의 크기를 저장
        badges.forEach { parcel.writeByte(if (it) 1 else 0) } // 각 뱃지의 상태 저장
    }

    override fun describeContents(): Int {
        return 0
    }

    // toString() 메서드
    override fun toString(): String {
        return "MyPageVO(img='$img', name='$name', gu='$gu', birth=$birth, gender='$gender', intro='$intro', badges=$badges)"
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
