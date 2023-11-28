package com.example.senimoapplication.Club.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.Nullable

data class ScheduleVO(
    @SerializedName("sche_code")
    val scheCode: String="",
    @SerializedName("club_code")
    val clubCode: String="",
    @SerializedName("sche_title")
    val scheTitle: String ="",
    @SerializedName("sche_content")
    val scheContent: String="",
    @SerializedName("sche_date")
    val scheDate: String = "",
    @SerializedName("sche_location")
    val scheLoca: String="",
    @SerializedName("fee")
    val scheFee: Int =0,
    @SerializedName("max_num")
    val maxNum: Int =0,
    @SerializedName("joined_members")
    var joinedMembers: Int= 0,
    @SerializedName("sche_img")
    var scheImg: String? = null,
    @SerializedName("club_name")
    var clubName : String? = null

)  : Parcelable {
    constructor(parcel: Parcel) : this(
        scheCode = parcel.readString() ?: "",
        clubCode = parcel.readString() ?: "",
        scheTitle = parcel.readString() ?: "",
        scheContent = parcel.readString() ?: "",
        scheDate = parcel.readString() ?: "",
        scheLoca = parcel.readString() ?: "",
        scheFee = parcel.readInt(),
        maxNum = parcel.readInt(),
        joinedMembers = parcel.readInt(),
        scheImg = parcel.readString() ?:"",
        clubName = parcel.readString() ?:""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(scheCode)
        parcel.writeString(clubCode)
        parcel.writeString(scheTitle)
        parcel.writeString(scheContent)
        parcel.writeString(scheDate)
        parcel.writeString(scheLoca)
        parcel.writeInt(scheFee)
        parcel.writeInt(maxNum)
        parcel.writeInt(joinedMembers)
        parcel.writeString(scheImg)
        parcel.writeString(clubName)


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScheduleVO> {
        override fun createFromParcel(parcel: Parcel): ScheduleVO {
            return ScheduleVO(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleVO?> {
            return arrayOfNulls(size)
        }
    }
}

data class AllSchedulesResVO(
    val data: List<ScheduleVO>
)
