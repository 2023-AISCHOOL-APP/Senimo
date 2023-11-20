package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.Login.VO.SignUpResVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivitySignUpBinding
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.io.path.PathWalkOption

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

    // 비밀번호와 비밀번호 확인 필드에 TextWatcher 추가
    val passwordTextWatcher = object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // 텍스트가 변경되기 전에 호출
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // 텍스트가 변경될 때마다 호출
        checkPasswordsMatch()
      }

      override fun afterTextChanged(s: Editable?) {
        // 텍스트가 변경된 후에 호출
      }
    }
    binding.etSignUpPw.addTextChangedListener(passwordTextWatcher)
    binding.etSignUpPwCheck.addTextChangedListener(passwordTextWatcher)
  }

  private fun checkPasswordsMatch() {
    val password = binding.etSignUpPw.text.toString()
    val confirmPassword = binding.etSignUpPwCheck.text.toString()

    if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
      if (password == confirmPassword) {
        binding.tvUserPwWarning.visibility = View.VISIBLE
        binding.tvUserPwWarning.text = "비밀번호가 일치합니다."
        binding.tvUserPwWarning.setTextColor(getResources().getColor(R.color.Pass))
      } else {
        binding.tvUserPwWarning.visibility = View.VISIBLE
        binding.tvUserPwWarning.text = "비밀번호가 일치하지 않습니다."
        binding.tvUserPwWarning.setTextColor(getResources().getColor(R.color.main))
      }
    } else {
      binding.tvUserPwWarning.visibility = View.GONE
    }
  }

  fun checkUserId(userId: String){
    val service = Server(this).service
    val call = service.checkId(userId)

    call.enqueue(object : Callback<SignUpResVO> {
      override fun onResponse(call: Call<SignUpResVO>, response: Response<SignUpResVO>) {
        Log.d("checkId", response.toString())


        val checkIdRes = response.body()
        if (checkIdRes != null) {
          when (checkIdRes.rows) {
            "success" -> {
              binding.tvUserIdWarning.visibility = View.VISIBLE
              binding.tvUserIdWarning.text = "사용 가능한 ID입니다."
              binding.tvUserIdWarning.setTextColor(getResources().getColor(R.color.Pass))
            }
            "dup" -> {
              binding.tvUserIdWarning.visibility = View.VISIBLE
              binding.tvUserIdWarning.text = "중복된 아이디입니다."
              binding.tvUserIdWarning.setTextColor(getResources().getColor(R.color.main))
              binding.etSignUpId.text.clear()
            }
            "error" -> {
              Toast.makeText(this@SignUpActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
          }
          Log.d("checkId", "Rows: ${checkIdRes.rows}")
        } else {
          Log.d("checkId", "응답이 null입니다.")
        }

      }

      override fun onFailure(call: Call<SignUpResVO>, t: Throwable) {
        Log.e("SignUpActivity", "네트워크 요청 실패", t)
      }

    })
  }
}
