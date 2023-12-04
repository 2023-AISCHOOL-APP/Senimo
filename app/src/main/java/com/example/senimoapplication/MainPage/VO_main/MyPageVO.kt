package com.example.senimoapplication.MainPage.VO_main

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MyPageVO(
    @SerializedName("user_img")
    val img: String? = "",            // 사진
    @SerializedName("user_name")
    val name: String? = "",      // 닉네임 또는 이름
    @SerializedName("user_gu")
    val gu: String = "",          // 지역 구
    @SerializedName("user_dong")
    val dong: String = "",          // 지역 동
    @SerializedName("birth_year")
    val birth: Int = 0,             // 생년 (0000년생)
    @SerializedName("gender")
    val gender: String = "",      // 성별
    @SerializedName("user_introduce")
    val intro: String = "",          // 소개글
    @SerializedName("user_id")
    val userId: String = "",
    val imageChanged: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        img = parcel.readString() ?: "",
        name = parcel.readString() ?: "",
        gu = parcel.readString() ?: "",
        dong = parcel.readString() ?: "",
        birth = parcel.readInt(),
        gender = parcel.readString() ?: "",
        intro = parcel.readString() ?: "",
        userId = parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(img)
        parcel.writeString(name)
        parcel.writeString(gu)
        parcel.writeString(dong)
        parcel.writeInt(birth)
        parcel.writeString(gender)
        parcel.writeString(intro)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    // toString() 메서드
    override fun toString(): String {
        return "MyPageVO(img='$img', name='$name', gu='$gu', dong='$dong', birth=$birth, gender='$gender', intro='$intro' , userId='$userId')"
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

data class getMyPageVO(
    val result: MyPageVO
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(MyPageVO::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(result, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<getMyPageVO> {
        override fun createFromParcel(parcel: Parcel): getMyPageVO {
            return getMyPageVO(parcel)
        }

        override fun newArray(size: Int): Array<getMyPageVO?> {
            return arrayOfNulls(size)
        }
    }
}

