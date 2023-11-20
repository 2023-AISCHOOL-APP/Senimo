package com.example.senimoapplication.server.Token


object UserData {
    var userId: String? = null
}

//로그인 성공 후 사용자의 user_id를 저장하려면 다음과 같이 할 수 있습니다:
//UserData.userId = loginResponse.userId

//다른 액티비티나 프래그먼트에서 사용자의 user_id를 필요로 할 때는 다음과 같이 사용할 수 있습니다:
//val currentUserId = UserData.userId
