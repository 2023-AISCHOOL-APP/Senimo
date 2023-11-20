package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.Login.VO.SignUpResVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivitySignUpBinding
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
      val userId = binding.etSignUpId.text.toString()

      checkUserId(userId)
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

  fun checkUserId(userId: String){
    val service = Server(this).service
    val call = service.checkId(userId)

    call.enqueue(object : Callback<SignUpResVO> {
      override fun onResponse(call: Call<SignUpResVO>, response: Response<SignUpResVO>) {
        Log.d("checkId", response.toString())
        if (response.isSuccessful) {
          val checkIdRes = response.body()
          if (checkIdRes != null && checkIdRes.rows == "success") {
            Toast.makeText(this@SignUpActivity, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
          } else {
            Toast.makeText(this@SignUpActivity, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show()
            binding.etSignUpId.text.clear()
          }
        }
      }

      override fun onFailure(call: Call<SignUpResVO>, t: Throwable) {
        Log.e("SignUpActivity", "네트워크 요청 실패", t)
      }

    })
  }
}
