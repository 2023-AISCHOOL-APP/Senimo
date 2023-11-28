package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.senimoapplication.Login.Activity_login.IntroActivity
import com.example.senimoapplication.Login.Activity_login.LoginActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityMemberDropOutBinding

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

        binding.btnSetDropout.setOnClickListener {
            if(isMainBackground) {
                val intent = Intent(this@MemberDropOutActivity,IntroActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this@MemberDropOutActivity,"탈퇴되었습니다.",Toast.LENGTH_SHORT).show()
            }

        }

        binding.imgMBackbtnToSetting.setOnClickListener {
            val intent = Intent(this@MemberDropOutActivity,SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}