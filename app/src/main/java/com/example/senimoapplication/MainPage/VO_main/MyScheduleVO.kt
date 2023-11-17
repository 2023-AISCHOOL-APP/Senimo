package com.example.senimoapplication.MainPage.VO_main
import android.os.Parcel
import android.os.Parcelable

class MyScheduleVO(
    val myscheduleTitle: String = "",
    val myscheduleContent: String = "",
    val myscheduleDate: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(myscheduleTitle)
        parcel.writeString(myscheduleContent)
        parcel.writeString(myscheduleDate)
    }

    override fun describeContents(): Int {
        return 0
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
