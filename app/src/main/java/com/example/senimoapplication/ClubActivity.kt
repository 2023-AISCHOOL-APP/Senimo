package com.example.senimoapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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

        icBack.setOnClickListener {
            val intent = Intent(this@ClubActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
}