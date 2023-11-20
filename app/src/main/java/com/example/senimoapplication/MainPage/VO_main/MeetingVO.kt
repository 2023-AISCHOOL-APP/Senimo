package com.example.senimoapplication.MainPage.VO_main

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MeetingVO(
    @SerializedName("club_location")
    val gu: String? = null,
    @SerializedName("club_name")
    var title: String,
    @SerializedName("club_introduce")
    var content: String,
    @SerializedName("keyword_name")
    var keyword: String,
    @SerializedName("attend_user_cnt")
    var attendance: Int,
    @SerializedName("max_cnt")
    var allMember: Int,
    @SerializedName("club_img")
    var imageUri: String? = null, // 이미지 경로를 String 타입으로 저장
    var club_code: String,
    @SerializedName("user_id")
    var userId: String? = null


) : Parcelable {
    constructor(parcel: Parcel) : this(
        gu = parcel.readString() ?: "",
        title = parcel.readString() ?: "",
        content = parcel.readString() ?: "",
        keyword = parcel.readString() ?: "",
        attendance = parcel.readInt(),
        allMember = parcel.readInt(),
        imageUri = parcel.readString() ?:"",
        club_code = parcel.readString() ?:"",
        userId = parcel.readString() ?:""

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gu)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(keyword)
        parcel.writeInt(attendance)
        parcel.writeInt(allMember)
        parcel.writeString(imageUri)
        parcel.writeString(club_code)
        parcel.writeString(userId)
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

