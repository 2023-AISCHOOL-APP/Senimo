package com.example.senimoapplication.Club.VO

class ChatVO (
    val clubCode : String? = "", // 모임구분코드
    val imageUri : String? = null, // 유저 프로필 이미지
    val userId : String? = "", // 유저 Id
    val userName : String? = "", // 유저 이름
    val messageTime : String = "", // 메시지 발송시간
    val message : String = "", // 메시지내용

)
