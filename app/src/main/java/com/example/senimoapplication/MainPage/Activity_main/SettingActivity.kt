package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.senimoapplication.Common.showBoardDialogBox
import com.example.senimoapplication.Common.showSettingDialogBox
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingBinding
    private var isToggleOn = true // 초기 알림 토글 상태
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvMLogout.setOnClickListener {
            // 클릭 시 showBoardDialogBox 함수 호출
            showSettingDialogBox(
                this@SettingActivity,
                "로그아웃 하시겠어요?",
                "로그아웃",
                "로그아웃되었습니다."
            )
        }

        // 클릭 시 알림 설정 on/off
        binding.imgMToggle.setOnClickListener {
            // 클릭 시 토글 상태 변경
            isToggleOn = !isToggleOn
            // 토글 상태에 따라 이미지 변경
            if (isToggleOn) {
                binding.imgMToggle.setImageResource(R.drawable.ic_toggle_color)
            } else {
                binding.imgMToggle.setImageResource(R.drawable.ic_toggle_gray)
            }
        }

        binding.tvMMemberDropout.setOnClickListener {
            val intent = Intent(this@SettingActivity, MemberDropOutActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}