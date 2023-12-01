package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityLoginBinding
import com.example.senimoapplication.server.Token.PreferenceManager
import com.example.senimoapplication.server.Token.TokenResponse
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

  lateinit var binding: ActivityLoginBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    binding = ActivityLoginBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val callback = object : OnBackPressedCallback(true){
      override fun handleOnBackPressed() {
        val intent = Intent(this@LoginActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()
      }
    }
    this.onBackPressedDispatcher.addCallback(this, callback)

    binding.btnLoginL.setOnClickListener {
      val userId = binding.etLoginId.text.toString()
      val userPw = binding.etLoginPw.text.toString()
      loginUser(userId, userPw)
    }

    binding.tvToSignUpL.setOnClickListener {
      val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
      startActivity(intent)
      finish()
    }
  }

  fun loginUser(userId: String, userPw: String){
    val service = Server(this).service
    val call = service.loginUser(userId, userPw)
    call.enqueue(object : Callback<TokenResponse> {
      override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
        Log.d("LoginInfo", "Response: $response")
        if (response.isSuccessful) {
          response.body()?.let { tokens ->
            if (tokens.accessToken != null && tokens.refreshToken != null && tokens.rows == "success") {
              Log.d("LoginInfoTokens", tokens.toString())
              Log.d("LoginInfo", "Access Token: ${tokens.accessToken}")
              Log.d("LoginInfo", "Refresh Token: ${tokens.refreshToken}")
              // 토큰 저장
              PreferenceManager.setAccessToken(this@LoginActivity, tokens.accessToken)
              PreferenceManager.setRefreshToken(this@LoginActivity, tokens.refreshToken)
              PreferenceManager.setUser(this@LoginActivity, tokens.user)

              Log.d("LoginInfo", "UserDatas: ${tokens.user}")
              // 환영 메시지 표시
              val welcomeMessage = "${tokens.user.user_id}님 환영합니다"
              Toast.makeText(this@LoginActivity, welcomeMessage, Toast.LENGTH_LONG).show()
              // 메인 액티비티로 이동
              val intent = Intent(this@LoginActivity, MainActivity::class.java)
              startActivity(intent)
              finishAffinity()
            } else {
              Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
          }
        } else {
          Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
        }
      }

      override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
        Log.e("loginActivity", "네트워크 요청 실패", t)
      }
    })
  }
}