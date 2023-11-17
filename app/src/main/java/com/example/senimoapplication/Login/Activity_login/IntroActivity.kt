package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.R

class IntroActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_intro)

    val btnLogin = findViewById<Button>(R.id.btnLoginI)
    val btnSignUp = findViewById<Button>(R.id.btnSignUp)
    var backPressedTime:Long = 0

    val callback = object : OnBackPressedCallback(true){
      override fun handleOnBackPressed() {
        if(System.currentTimeMillis() - backPressedTime >= 2000){
          backPressedTime = System.currentTimeMillis()
          Toast.makeText(this@IntroActivity, "뒤로가기 버튼을 한번 더 누르면 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
        }else if (System.currentTimeMillis() - backPressedTime < 2000){
          finish()
        }
      }
    }
    this.onBackPressedDispatcher.addCallback(this, callback)

    btnLogin.setOnClickListener {
      val intent = Intent(this@IntroActivity, LoginActivity::class.java)
      startActivity(intent)
      finish()
    }

    btnSignUp.setOnClickListener {
      val intent = Intent(this@IntroActivity, SignUpActivity::class.java)

      startActivity(intent)
      finish()
    }
  }
}