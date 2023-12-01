package com.example.senimoapplication.server.Token

data class TokenValidationResponse(
        val success: Boolean,
        val message: String,
        val user: UserDatas // 서버로부터 반환된 사용자 정보
)

data class UserDatas(
    val user_id: String,
    val user_name: String,
    val birth_year: Int,
    val gender: String,
    val user_gu: String,
    val user_dong: String,
    val user_introduce:String,
    val user_img:String
)

