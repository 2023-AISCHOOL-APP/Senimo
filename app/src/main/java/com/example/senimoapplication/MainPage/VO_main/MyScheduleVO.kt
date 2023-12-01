package com.example.senimoapplication.MainPage.VO_main

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MyScheduleVO(
    @SerializedName("club_name")
    val clubName: String = "",
    @SerializedName("sche_title")
    val scheTitle: String = "",
    @SerializedName("sche_content")
    val scheContent: String = "",
    @SerializedName("sche_date")
    val scheDate: String = "",
    @SerializedName("fee")
    val scheFee: Int = 0,
    @SerializedName("sche_location")
    val scheLoca: String = "",
    @SerializedName("max_num")
    val maxNum: Int = 0,
    @SerializedName("joined_members")
    val attendUserCnt: Int = 0,
    val state: String = "모집중",
    @SerializedName("sche_img")
    var scheImg: String, // 이미지 경로를 String 타입으로 저장
    @SerializedName("sche_code")
    val scheCode : String = "",
) : Parcelable {

    constructor(parcel: Parcel) : this(
        scheTitle = parcel.readString() ?: "",
        scheContent = parcel.readString() ?: "",
        scheDate = parcel.readString() ?: "",
        scheFee = parcel.readInt(),
        scheLoca = parcel.readString() ?: "",
        maxNum = parcel.readInt(),
        attendUserCnt = parcel.readInt(),
        state = parcel.readString() ?: "",
        scheImg = parcel.readString() ?: "",
        scheCode = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(clubName)
        parcel.writeString(scheTitle)
        parcel.writeString(scheContent)
        parcel.writeString(scheDate)
        parcel.writeInt(scheFee)
        parcel.writeString(scheLoca)
        parcel.writeInt(maxNum)
        parcel.writeInt(attendUserCnt)
        parcel.writeString(state)
        parcel.writeString(scheImg)
        parcel.writeString(scheCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "MyScheduleVO(clubName='$clubName', scheTitle='$scheTitle', scheContent='$scheContent', scheDate='$scheDate', scheFee=$scheFee, scheLoca='$scheLoca', maxNum=$maxNum, attendUserCnt=$attendUserCnt, state='$state', scheImg='$scheImg', scheCode = '$scheCode')"
    }

    companion object CREATOR : Parcelable.Creator<MyScheduleVO> {
        override fun createFromParcel(parcel: Parcel): MyScheduleVO {
            return MyScheduleVO(parcel)
        }

        override fun newArray(size: Int): Array<MyScheduleVO?> {
            return arrayOfNulls(size)
        }
    }
}
