package com.example.senimoapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    val btnLogin = findViewById<Button>(R.id.btnLogin)

    btnLogin.setOnClickListener {
      val intent = Intent(this@LoginActivity, EnterMyInfoActivity::class.java)

      startActivity(intent)

      finish()
    }
  }
}