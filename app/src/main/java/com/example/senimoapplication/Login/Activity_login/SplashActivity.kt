package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import com.example.senimoapplication.server.Token.TokenValidationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        Handler().postDelayed({
//            val intent = Intent(this@SplashActivity, IntroActivity::class.java)
//            startActivity(intent)
//            finish()
//        },3000)

        validateAccessToken()

    }


    private fun validateAccessToken() {
        val accessToken = PreferenceManager.getAccessToken(this)
        if (accessToken != null) {
            val service = Server(this).service
            service.validateToken("Bearer ${accessToken}").enqueue(object : Callback<TokenValidationResponse> {
                override fun onResponse(call: Call<TokenValidationResponse>, response: Response<TokenValidationResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Log.d("splashActivity", "여기도착")
                        response.body()?.user?.let { user ->
                            PreferenceManager.setUser(this@SplashActivity, user)
                        }
                        navigateToMainActivity() // 유효한 토큰이면 메인 액티비티로 이동
                    } else {
                        navigateToIntroActivity() // 유효하지 않으면 인트로 액티비티로 이동
                    }

                    val responseBody = response.body()
                    Log.d("splashActivity",responseBody.toString())
                }

                override fun onFailure(call: Call<TokenValidationResponse>, t: Throwable) {
                    navigateToIntroActivity() // 통신 오류 발생 시 인트로 액티비티로 이동
                }
            })
        } else {
            navigateToIntroActivity() // 토큰이 없으면 인트로 액티비티로 이동
        }
    }


    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        val userData = PreferenceManager.getUser(this)
        Log.d("SharedPreferencesTest", "Loaded User Data: ${userData.toString()}")
        Log.d("SharedPreferencesTest", "Loaded User Data: ${userData?.user_id}")
        Log.d("이미지입니다 유저 아이디11",userData?.toString()!!)
        // val userId : String? =null
        //val UsrData = PreferenceManager.getUser((this))
        //userId = userData?    .user_id
        finishAffinity()
    }


    private fun navigateToIntroActivity() {
        Handler().postDelayed({
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

}