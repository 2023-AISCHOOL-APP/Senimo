package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< Updated upstream
=======
import android.widget.Button
import android.widget.Toast
>>>>>>> Stashed changes
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
      val userId = binding.etSignUpId.text.toString()
      val userPw = binding.etSignUpPw.text.toString()
      val userPwCheck = binding.etSignUpPwCheck.text.toString()

      if (userId.isNotEmpty() && userPw.isNotEmpty() && userPw == userPwCheck) {
        val intent = Intent(this@SignUpActivity, EnterMyInfoActivity::class.java)
        intent.putExtra("user_Id", userId)
        intent.putExtra("user_Pw", userPw)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        finish()
      } else {
        // 사용자에게 유효한 데이터 입력 요청 메시지 표시
        Toast.makeText(this@SignUpActivity, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
      }
    }

    binding.imgBackToIntro.setOnClickListener {
      val intent = Intent(this@SignUpActivity, IntroActivity::class.java)
      startActivity(intent)
      finish()
    }
  }
}
