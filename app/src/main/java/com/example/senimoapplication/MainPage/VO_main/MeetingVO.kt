package com.example.senimoapplication.MainPage.VO_main

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MeetingVO(
    @SerializedName("club_location")
    val gu: String? = null,
    @SerializedName("club_name") // 서버 응답의 club_name을 title에 맵핑합니다.
    var title: String,
    @SerializedName("club_introduce") // 서버 응답의 club_introduce를 content에 맵핑합니다.
    var content: String,
    @SerializedName("keyword_name")
    // var keyword: String,
    var keyword: String,
    @SerializedName("attend_user_cnt")
    var attendance: Int,
    @SerializedName("max_cnt")
    var allMember: Int,
    @SerializedName("club_img_url")
    var imageUri: String? = null, // 이미지 경로를 String 타입으로 저장
    var club_code: String



//    val title: String = "",
//    val content: String = "",
//    val keyword: String = "",
//    val attendance : Int = 0,
//    val allMember : Int = 0,
    // val person: String = "",
    // val imageUri: Uri? = "" // 이미지의 URI 또는 경로를 저장할 필드
    // val backicon : String = ""
) : Parcelable {
    // 이미지 경로를 사용하여 Uri를 얻는 메서드 추가

    constructor(parcel: Parcel) : this(
        gu = parcel.readString() ?: "",
        title = parcel.readString() ?: "",
        content = parcel.readString() ?: "",
        keyword = parcel.readString() ?: "",
        attendance = parcel.readInt(),
        allMember = parcel.readInt(),
        // person = parcel.readString() ?: "",
        imageUri = parcel.readString() ?:"",
        // backicon = parcel.readString() ?: ""
        club_code = parcel.readString() ?:""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gu)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(keyword)
        parcel.writeInt(attendance)
        parcel.writeInt(allMember)
        // parcel.writeString(person)
        parcel.writeString(imageUri?.toString()) // 이미지 경로 또는 URI를 저장
        parcel.writeString(club_code?.toString())
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

