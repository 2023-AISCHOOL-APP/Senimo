package com.example.senimoapplication.Club.VO

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ClubInfoVO(
    // 모임명, 이미지, 전체회원수, 정원, 카테고리, 모임소개, 활동지역

    @SerializedName("club_img_url")
    var clubImageUri: String? = null, // 이미지 경로를 String 타입으로 저장
    @SerializedName("joined_user_cnt")
    val joinedUserCnt : Int,
    @SerializedName("max_cnt")
    val maxCnt : Int,
    @SerializedName("club_name")
    val clubName : String,
    @SerializedName("club_location")
    val clubLocation : String,
    @SerializedName("keyword_name")
    val keywordName : String,
    @SerializedName("club_introduce")
    val clubIntroduce : String,


//    val clubImg : String="",
//    val joinedUserCnt : Int =0,
//    val maxCnt : Int=0,
//    val clubName : String="",
//    val clubLocation : String="",
//    val keywordName : String="",
//    val clubIntroduce : String=""

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        clubImageUri = parcel.readString() ?: "",
        joinedUserCnt = parcel.readInt(),
        maxCnt = parcel.readInt(),
        clubName = parcel.readString() ?: "",
        keywordName = parcel.readString() ?:"",
        clubLocation = parcel.readString() ?: "",
        clubIntroduce = parcel.readString() ?:"",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(gu)

        parcel.writeString(clubImageUri)
        parcel.writeInt(maxCnt)
        parcel.writeInt(joinedUserCnt)
        parcel.writeString(clubName)
        parcel.writeString(clubLocation)
        parcel.writeString(keywordName)
        parcel.writeString(clubIntroduce)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClubInfoVO> {
        override fun createFromParcel(parcel: Parcel): ClubInfoVO {
            return ClubInfoVO(parcel)
        }

        override fun newArray(size: Int): Array<ClubInfoVO?> {
            return arrayOfNulls(size)
        }
    }
}