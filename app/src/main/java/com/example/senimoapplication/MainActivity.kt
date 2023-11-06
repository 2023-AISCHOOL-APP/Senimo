package com.example.senimoapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.senimoapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // 바인딩 객체 획득
    val binding = ActivityMainBinding.inflate(layoutInflater)
    // 액티비티 화면 출력
    setContentView(binding.root)

    // 뷰 객체 이용
    //binding.bnvMain.setOnItemSelectedListener {}




    }

  }