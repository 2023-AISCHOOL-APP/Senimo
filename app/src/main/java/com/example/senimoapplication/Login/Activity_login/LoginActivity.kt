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
import com.example.senimoapplication.server.Token.UserData
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

      // 임시로 서버가 안될때 그다음페이지로 넘어가게 넣어놓음
//      val intent = Intent(this@LoginActivity, MainActivity::class.java)
//      startActivity(intent)
//      finishAffinity()
      //
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

//    val userId = binding.etLoginId.text.toString()
//    val userPw = binding.etLoginPw.text.toString()

    val service = Server(this).service
    val call = service.loginUser(userId, userPw)
    call.enqueue(object : Callback<TokenResponse> {
      override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
        Log.d("LoginInfo", "Response: $response")
        if (response.isSuccessful) {

          response.body()?.let { tokens ->
            // 여기서 조건문을 추가합니다.
            if (tokens.accessToken != null && tokens.refreshToken != null && tokens.rows == "success") {
              Log.d("LoginInfoTokens", tokens.toString())
              Log.d("LoginInfo", "Access Token: ${tokens.accessToken}")
              Log.d("LoginInfo", "Refresh Token: ${tokens.refreshToken}")
              // 토큰 저장
              PreferenceManager.setAccessToken(this@LoginActivity, tokens.accessToken)
              PreferenceManager.setRefreshToken(this@LoginActivity, tokens.refreshToken)

              UserData.userId = tokens.userId
              Log.d("LoginInfo", "UserData.userId: ${UserData.userId}")
              // 환영 메시지 표시
              val welcomeMessage = "${UserData.userId}님 환영합니다"
              Toast.makeText(this@LoginActivity, welcomeMessage, Toast.LENGTH_LONG).show()
              // 메인 액티비티로 이동
              val intent = Intent(this@LoginActivity, MainActivity::class.java)
              startActivity(intent)
              finishAffinity()
            } else {
              Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
//=======
//          val LoginResVO = response.body()
//          if (LoginResVO != null && LoginResVO.rows == "success") {
//            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(intent)
//            finishAffinity()
//          } else {
//            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
//>>>>>>> d5779f6404859dcb3a0a0721f7b18548ec190d50

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


//  fun refreshTokenIfNeeded() {
//    val refreshToken = PreferenceManager.getRefreshToken(context)
//    refreshToken?.let {
//      val service = Server().service
//      val call = service.refreshToken(it)
//      call.enqueue(object : Callback<TokenResponse> {
//        override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
//          if (response.isSuccessful) {
//            response.body()?.let { tokenResponse ->
//              PreferenceManager.setAccessToken(context, tokenResponse.accessToken)
//            }
//          } else {
//            // 토큰 재발급 실패 처리
//          }
//        }
//
//        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//          // 네트워크 실패 처리
//        }
//      })
//    }
//  }

}