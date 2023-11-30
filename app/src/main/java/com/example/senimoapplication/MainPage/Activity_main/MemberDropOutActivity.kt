package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.Common.showDropOutDialogBox
import com.example.senimoapplication.Login.Activity_login.IntroActivity
import com.example.senimoapplication.MainPage.VO_main.UserDropOutResVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityMemberDropOutBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberDropOutActivity : AppCompatActivity() {

    lateinit var binding: ActivityMemberDropOutBinding
    var isChecked = false
    var isMainBackground = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_drop_out)

        binding = ActivityMemberDropOutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userData = PreferenceManager.getUser(this)
        val userName = userData?.user_name.toString()

        binding.tvDropOutUserName.text = userName

        // 초기 상태 설정
        binding.imgMCheck.setImageResource(R.drawable.ic_checkbox_gray30)
        binding.btnSetDropout.setBackgroundResource(R.drawable.button_shape_gray30)

        binding.imgMCheck.setOnClickListener {
            if (isChecked) {
                binding.imgMCheck.setImageResource(R.drawable.ic_checkbox_gray30)
                binding.btnSetDropout.setBackgroundResource(R.drawable.button_shape_gray30)
                isMainBackground = false
            } else {
                binding.imgMCheck.setImageResource(R.drawable.ic_checkbox_color)
                binding.btnSetDropout.setBackgroundResource(R.drawable.button_shape_main)
                isMainBackground = true
            }
            isChecked = !isChecked
        }

        val userId = userData?.user_id.toString()
        binding.btnSetDropout.setOnClickListener {
            // 클릭 시 showBoardDialogBox 함수 호출
            if(isMainBackground) {
                showDropOutDialogBox(
                this@MemberDropOutActivity,
                "시니모를 탈퇴하시겠어요?",
                "탈퇴하기",
                "회원탈퇴가 완료되었습니다."
            ){
                    userDropOut(userId)
                }
            }

        }

        binding.imgMBackbtnToSetting.setOnClickListener {
            val intent = Intent(this@MemberDropOutActivity,SettingActivity::class.java)
            startActivity(intent)
            finish()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val intent = Intent(this@MemberDropOutActivity,SettingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    fun userDropOut(userId: String?) {
        val service = Server(this).service
        val call = service.userDropOut(userId)

        call.enqueue(object : Callback<UserDropOutResVO> {
            override fun onResponse(
                call: Call<UserDropOutResVO>,
                response: Response<UserDropOutResVO>
            ) {
                if (response.isSuccessful) {
                    val dropOutRes = response.body()
                    if (dropOutRes != null && dropOutRes.rows == "success") {
                        Log.d("회원 탈퇴", "${userId} 탈퇴 성공")
                        // 회원탈퇴 후 처리
                        val intent = Intent(this@MemberDropOutActivity, IntroActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("회원 탈퇴", "${userId} 탈퇴 실패")
                    }
                }
            }

            override fun onFailure(call: Call<UserDropOutResVO>, t: Throwable) {
                Log.e("회원 탈퇴", "회원 탈퇴 네트워크 요청 실패", t)
            }

        })
    }
}