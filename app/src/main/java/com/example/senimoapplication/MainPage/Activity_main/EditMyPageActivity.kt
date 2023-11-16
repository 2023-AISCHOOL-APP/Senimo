package com.example.senimoapplication.MainPage.Activity_main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.MainPage.fragment_main.MypageFragment
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityEditMyPageBinding

class EditMyPageActivity : AppCompatActivity() {

    private lateinit var myProfile: MyPageVO // MyPageVO 객체를 담을 변수


    lateinit var binding: ActivityEditMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // myProfile 변수 초기화
        myProfile = MyPageVO(
            "drawable/golf_img.jpg",
            "",
            "",
            0,
            "",
            ""
        )

        binding = ActivityEditMyPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // 뒤로가기 버튼
        binding.imgMBackbtnToFrag4.setOnClickListener {
            val intent = Intent(this@EditMyPageActivity,MainActivity::class.java)
            // 탭 4를 선택한 것으로 설정
            intent.putExtra("selected_tab","M_tab4")
            startActivity(intent)
            finish()
        }


    }

}