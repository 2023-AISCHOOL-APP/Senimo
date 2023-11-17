package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityLoginBinding

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
    }

    binding.tvToSignUpL.setOnClickListener {
      val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
      startActivity(intent)
      finish()
    }
  }
}