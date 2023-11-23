//package com.example.senimoapplication.server.Token
//
//import android.content.Context
//import android.util.Log
//import com.example.senimoapplication.server.Token.PreferenceManager
//import com.example.senimoapplication.server.Server
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
////호출 방법
////val tokenManager = TokenManager(this)
////tokenManager.refreshTokenIfNeeded()
//class TokenManager(private val context: Context) {
//    fun refreshTokenIfNeeded() {
//        val refreshToken = PreferenceManager.getRefreshToken(context)
//        refreshToken?.let {
//            val service = Server(context).service
//            val call = service.refreshToken(it)
//            call.enqueue(object : Callback<TokenResponse> {
//                override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
//                    if (response.isSuccessful) {
//                        response.body()?.let { tokens ->
//                            tokens.accessToken?.let { accessToken ->
//                                Log.d("TokenManager", "New Access Token: ${tokens.accessToken}")
//                                PreferenceManager.setAccessToken(context, accessToken)
//                            }
//                        }
//                    } else {
//                        // 토큰 재발급 실패 처리
//                        Log.d("TokenManger","토큰 재발급실패")
//                    }
//                }
//
//                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                    // 통신 실패 처리
//                    Log.d("TokenManger","통신실패",t)
//                }
//            })
//        }
//    }
//}