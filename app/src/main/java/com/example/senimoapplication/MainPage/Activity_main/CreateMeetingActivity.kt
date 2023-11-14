package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityCreateMeetingBinding

class CreateMeetingActivity : AppCompatActivity() {

    lateinit var binding : ActivityCreateMeetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMeetingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 스피너 초기화
        val spinner = findViewById<Spinner>(R.id.sp_M_gulist)

        // stings.xml에서 문자열 배열을 가져오기   (광산구, 남구, 동구, 북구, 서구)
        val districtArray = resources.getStringArray(R.array.districts)

        // 어댑터 생성하고 스피너에 설정하기
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districtArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        binding.ImgMBackbtnToFrag2.setOnClickListener {
            val intent = Intent(this@CreateMeetingActivity, MainActivity::class.java)
            intent.putExtra("selected_tab","tab2")  // "tab2"는 Fragment2를 나타냅니다
            startActivity(intent)
            finish()
        }


    }
}