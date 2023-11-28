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

    
    // EditText에 포커스 리스너 설정
    binding.etSignUpId.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        // 포커스가 없을 때 (커서가 이동될 때)
        binding.tvUserIdWarning.visibility = View.GONE
      } else {
        val userId = binding.etSignUpId.text.toString()
        if (userId.isNotEmpty()) {
          // 중복된 아이디 체크
          checkUserId(userId)
        }
      }
    }

    // 중복 확인 결과에 따라 다음 버튼 활성화/비활성화 설정
   setNextButtonEnabled(false) // 초이게 비활성화 상태로 설정

    // id 중복 확인
    binding.btnIdCheck.setOnClickListener {
      val userId = binding.etSignUpId.text.toString()

      checkUserId(userId)
    }

    // 다음 버튼 클릭 시
    binding.btnToEnterMyInfo.setOnClickListener {
      val userId = binding.etSignUpId.text.toString()
      val userPw = binding.etSignUpPw.text.toString()
      val userPwCheck = binding.etSignUpPwCheck.text.toString()

      if (userId.isNotEmpty() && userPw.isNotEmpty() && userPw == userPwCheck) {
        val intent = Intent(this@SignUpActivity, EnterMyInfoActivity::class.java)
        intent.putExtra("user_Id", userId)
        intent.putExtra("user_Pw", userPw)
        startActivity(intent)
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

    // etSignUpId에 TextWatcher 추가
    binding.etSignUpId.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // 텍스트가 변경되기 전에 호출
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // 텍스트가 변경될 때마다 호출
        if (s.isNullOrEmpty()) {
          // 텍스트가 비어 있으면 tv_UserIdWarning 숨김
          binding.tvUserIdWarning.visibility = View.GONE
        } else {
          // 텍스트가 있으면 다른 로직 수행 (예: 아이디 체크)
          // checkUserId(s.toString())
        }
      }

      override fun afterTextChanged(s: Editable?) {
        // 텍스트가 변경된 후에 호출
      }
    })

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

  // 중복 확인 결과에 따라 다음 버튼 활성화/비활성화 설정
  private fun setNextButtonEnabled(enabled : Boolean) {
    binding.btnToEnterMyInfo.isEnabled = enabled
    if (enabled) {
      // 활성화되면 배경을 원래 상태로 변경
      binding.btnToEnterMyInfo.setBackgroundResource(R.drawable.button_shape_main)
    } else {
      // 비활성화되면 배경을 다른 상태로 변경
      binding.btnToEnterMyInfo.setBackgroundResource(R.drawable.button_shape_gray30)
    }
  }

  private fun checkPasswordsMatch() {
    val password = binding.etSignUpPw.text.toString()
    val confirmPassword = binding.etSignUpPwCheck.text.toString()

    if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
      if (password == confirmPassword) {
        binding.tvUserPwWarning.visibility = View.VISIBLE
        binding.tvUserPwWarning.text = "비밀번호가 일치합니다."
        binding.tvUserPwWarning.setTextColor(getResources().getColor(R.color.Pass))
        setNextButtonEnabled(true) // 비밀번호 일치할 때 다음 버튼 활성화
      } else {
        binding.tvUserPwWarning.visibility = View.VISIBLE
        binding.tvUserPwWarning.text = "비밀번호가 일치하지 않습니다."
        binding.tvUserPwWarning.setTextColor(getResources().getColor(R.color.point))
        setNextButtonEnabled(false) // 비밀번호 불일치할 때 다음 버튼 비활성화
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
              setNextButtonEnabled(true) // 사용 가능한 ID일 때 다음 버튼 활성화
            }
            "dup" -> {
              binding.tvUserIdWarning.visibility = View.VISIBLE
              binding.tvUserIdWarning.text = "중복된 아이디입니다."
              binding.tvUserIdWarning.setTextColor(getResources().getColor(R.color.point))
              setNextButtonEnabled(false) // 사용 가능한 ID일 때 다음 버튼 비활성화
              // binding.etSignUpId.text.clear()
            }
            "error" -> {
              Toast.makeText(this@SignUpActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
              setNextButtonEnabled(false) // 에러 발생 시 다음 버튼 비활성화
            }
          }
          Log.d("checkId", "Rows: ${checkIdRes.rows}")
        } else {
          Log.d("checkId", "응답이 null입니다.")
        }

      }

      override fun onFailure(call: Call<SignUpResVO>, t: Throwable) {
        Log.e("SignUpActivity", "네트워크 요청 실패", t)
        // 네트워크 요청 실패 시 다음 버튼 비활성화
        setNextButtonEnabled(false)
      }

    })
  }
}
