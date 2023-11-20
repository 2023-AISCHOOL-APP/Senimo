package com.example.senimoapplication.MainPage.VO_main
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MyScheduleVO(
    @SerializedName("sche_img_url")
    // var imageUri: String? = null, // 이미지 경로를 String 타입으로 저장
    var imageUri: Int, // 이미지 경로를 String 타입으로 저장
    @SerializedName("sche_title")
    val myscheduleTitle: String = "",
    @SerializedName("sche_content")
    val myscheduleContent: String = "",
    @SerializedName("sche_date")
    val myscheduleDate: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        // imageUri = parcel.readString() ?:"",
        imageUri = parcel.readInt(),
        myscheduleTitle = parcel.readString() ?: "",
        myscheduleContent = parcel.readString() ?: "",
        myscheduleDate = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        // parcel.writeString(imageUri?.toString())
        parcel.writeInt(imageUri) // 이미지 리소스 ID를 Int로 씀
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
