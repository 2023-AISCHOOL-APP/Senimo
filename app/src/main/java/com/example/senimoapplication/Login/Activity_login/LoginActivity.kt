package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.Login.VO.LoginResVO
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityLoginBinding
import com.example.senimoapplication.server.Retrofit.ApiService
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

      val intent = Intent(this@LoginActivity, MainActivity::class.java)
      startActivity(intent)
      finishAffinity()

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

    val userId = binding.etLoginId.text.toString()
    val userPw = binding.etLoginPw.text.toString()

    val service = Server().service
    val call = service.loginUser(userId, userPw)
    call.enqueue(object : Callback<LoginResVO> {
      override fun onResponse(call: Call<LoginResVO>, response: Response<LoginResVO>) {
        Log.d("LoginInfo", response.toString())
        if (response.isSuccessful) {
          val LoginResVO = response.body()
          if (LoginResVO != null && LoginResVO.rows == "success") {
            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
          } else {
            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
          }
        } else {
          Toast.makeText(this@LoginActivity, "서버 응답 실패", Toast.LENGTH_SHORT).show()
        }
      }

      override fun onFailure(call: Call<LoginResVO>, t: Throwable) {
        Log.e("loginActivity", "네트워크 요청 실패", t)
      }
    })
  }
}