package com.example.senimoapplication.MainPage.VO_main

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ChatListVO(
    @SerializedName("user_img")
    val meetingImg: String = "",
    @SerializedName("club_name")
    val meetingTitle: String = "",
    @SerializedName("chat_content")
    val recentlyMessage: String = "",
    @SerializedName("chat_dt")
    val time: String = "",
    val newMessagesCount: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(meetingImg)
        parcel.writeString(meetingTitle)
        parcel.writeString(recentlyMessage)
        parcel.writeString(time)
        parcel.writeInt(newMessagesCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatListVO> {
        override fun createFromParcel(parcel: Parcel): ChatListVO {
            return ChatListVO(parcel)
        }

        override fun newArray(size: Int): Array<ChatListVO?> {
            return arrayOfNulls(size)
        }
    }
}
