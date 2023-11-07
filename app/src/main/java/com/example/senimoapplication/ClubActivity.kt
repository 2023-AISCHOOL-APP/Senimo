package com.example.senimoapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.senimoapplication.fragment.BoardFragment
import com.example.senimoapplication.fragment.ChatFragment
import com.example.senimoapplication.fragment.GalleryFragment
import com.example.senimoapplication.fragment.HomeFragment
import com.google.android.material.tabs.TabLayout

class ClubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club)

        var viewPager = findViewById(R.id.viewPager) as ViewPager
        var tabLayout = findViewById(R.id.tabLayout) as TabLayout

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(HomeFragment(),"모임 홈")
        fragmentAdapter.addFragment(BoardFragment(), "게시판")
        fragmentAdapter.addFragment(GalleryFragment(), "사진첩")
        fragmentAdapter.addFragment(ChatFragment(), "채팅")

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        val icBack = findViewById<ImageView>(R.id.icBack)
        val img = findViewById<ImageView>(R.id.imgLike)
        val tvClubName = findViewById<TextView>(R.id.tvClubName)





        // 1) 앱 바 기능 구현
        // 뒤로가기 버튼
        icBack.setOnClickListener {
            val intent = Intent(this@ClubActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        // 모임명 변경 (데이터 연동 필요)
        // tvClubName.text = 서버에서 받아온 값

        // 모임 찜 기능 (데이터 연동 필요)
        var cnt = 0
        img.setOnClickListener {
            cnt++
            if (cnt%2 == 1) {
                img.setImageResource(R.drawable.ic_like)
                // 서버로 데이터 보내기
            }else{
                img.setImageResource(R.drawable.ic_emptylike)
                // 서버로 데이터 보내기
            }
        }



    }
}