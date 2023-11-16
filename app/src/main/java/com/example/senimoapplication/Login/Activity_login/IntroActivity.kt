package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.senimoapplication.R

class IntroActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_intro)

    val btnLogin = findViewById<Button>(R.id.btnLoginI)
    val btnSignUp = findViewById<Button>(R.id.btnSignUp)

    btnLogin.setOnClickListener {
      val intent = Intent(this@IntroActivity, LoginActivity::class.java)

      startActivity(intent)
    }

    btnSignUp.setOnClickListener {
      val intent = Intent(this@IntroActivity, SignUpActivity::class.java)

      startActivity(intent)
    }
  }
}