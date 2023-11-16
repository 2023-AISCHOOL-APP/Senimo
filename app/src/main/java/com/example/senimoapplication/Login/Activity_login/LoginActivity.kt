package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R

class LoginActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    val btnLoginL = findViewById<Button>(R.id.btnLoginL)

    btnLoginL.setOnClickListener {
      val intent = Intent(this@LoginActivity, MainActivity::class.java)

      startActivity(intent)

      finish()
    }
  }
}