package com.example.senimoapplication.server.Token



data class TokenResponse(val accessToken: String? =null,
                         val refreshToken: String? =null,
                         val userId:String? =null,
                         val rows:String? =null,
                         val user: UserDatas)// 서버로부터 반환된 사용자 정보

