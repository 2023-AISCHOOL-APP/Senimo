package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.Login.Activity_login.IntroActivity
import com.example.senimoapplication.Login.Activity_login.LoginActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityMemberDropOutBinding

class MemberDropOutActivity : AppCompatActivity() {

    lateinit var binding: ActivityMemberDropOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_drop_out)

        binding = ActivityMemberDropOutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSetDropout.setOnClickListener {
            val intent = Intent(this@MemberDropOutActivity,IntroActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@MemberDropOutActivity,"탈퇴되었습니다.",Toast.LENGTH_SHORT).show()

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
}