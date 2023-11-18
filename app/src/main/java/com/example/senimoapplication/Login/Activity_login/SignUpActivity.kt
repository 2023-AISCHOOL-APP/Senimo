package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

  lateinit var binding: ActivitySignUpBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up)

    binding = ActivitySignUpBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val callback = object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        val intent = Intent(this@SignUpActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()
      }
    }
    this.onBackPressedDispatcher.addCallback(this, callback)


    // id 중복 확인

    binding.btnIdCheck.setOnClickListener {
      val userId = binding.etSignUpId.text?.toString() ?: ""



    }






    binding.btnToEnterMyInfo.setOnClickListener {
      val intent = Intent(this@SignUpActivity, EnterMyInfoActivity::class.java)
      startActivity(intent)
      overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
      finish()
    }

    binding.imgBackToIntro.setOnClickListener {
      val intent = Intent(this@SignUpActivity, IntroActivity::class.java)
      startActivity(intent)
      finish()
    }
  }
}
