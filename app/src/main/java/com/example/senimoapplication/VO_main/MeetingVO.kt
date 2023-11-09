package com.example.senimoapplication.VO_main

import android.os.Parcel
import android.os.Parcelable

data class MeetingVO(
    val title: String = "",
    val content: String = "",
    val keyword: String = "",
    val attendance : Int = 0,
    val allMember : Int = 0,
    // val person: String = "",
    val imageUri: String = "", // 이미지의 URI 또는 경로를 저장할 필드
    // val backicon : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        content = parcel.readString() ?: "",
        keyword = parcel.readString() ?: "",
        attendance = parcel.readInt(),
        allMember = parcel.readInt(),
        // person = parcel.readString() ?: "",
        imageUri = parcel.readString() ?: "",
        // backicon = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(keyword)
        parcel.writeInt(attendance)
        parcel.writeInt(allMember)
        // parcel.writeString(person)
        parcel.writeString(imageUri) // 이미지 경로 또는 URI를 저장
        // parcel.writeString(backicon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MeetingVO> {
        override fun createFromParcel(parcel: Parcel): MeetingVO {
            return MeetingVO(parcel)
        }

        override fun newArray(size: Int): Array<MeetingVO?> {
            return arrayOfNulls(size)
        }
    }
}