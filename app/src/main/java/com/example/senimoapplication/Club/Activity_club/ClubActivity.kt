package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.viewpager.widget.ViewPager
import com.example.senimoapplication.Club.VO.InterestedResVO
import com.example.senimoapplication.Club.fragment.BoardFragment
import com.example.senimoapplication.Club.fragment.ChatFragment
import com.example.senimoapplication.Club.fragment.GalleryFragment
import com.example.senimoapplication.Club.fragment.HomeFragment
import com.example.senimoapplication.Club.adapter.FragmentAdapter
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.R
import com.example.senimoapplication.server.Server
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.FieldMap

class ClubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club)

        // Intent관리
        val joinedMemberCnt = intent.getStringExtra("joinedMemberCnt")
        val selectedSchecode = intent.getStringExtra("previousSchedule")
        if (joinedMemberCnt != null || selectedSchecode != null) {
            navigateToHomeFragment(joinedMemberCnt, selectedSchecode)
        }

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

        // 모임명 표시
        val clickedMeeting = intent.getParcelableExtra<MeetingVO>("clickedMeeting")
        if (clickedMeeting != null) {
            Log.d("ClubActivity", clickedMeeting.toString())
            val title = clickedMeeting.title
            tvClubName.text = if (title.length > 13) {
                title.substring(0, 13) + "..."
            } else {
                title
            }
        }

        // 1) 앱 바 기능 구현
        // 뒤로가기 버튼
        icBack.setOnClickListener {
            val intent = Intent(this@ClubActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val intent = Intent(this@ClubActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // 모임명 변경 (데이터 연동 필요)
        // tvClubName.text = 서버에서 받아온 값

        // 모임 찜 기능 (데이터 연동 필요)
        var cnt = 0
        img.setOnClickListener {
            cnt++
            val clickedMeeting = intent.getParcelableExtra<MeetingVO>("clickedMeeting")
            val userId = clickedMeeting?.userId.toString()
            val clubCode = clickedMeeting?.club_code.toString()

            if (clickedMeeting != null) {
                val clubCode = clickedMeeting.club_code
                val params = mapOf("club_code" to clubCode, "user_id" to userId)

                if (cnt % 2 == 1) {
                    img.setImageResource(R.drawable.ic_fullheart)
                } else {
                    img.setImageResource(R.drawable.ic_lineheart)
                }
                updateInterestStatus(params)
            }
        }
    }
    private fun navigateToHomeFragment(joinedMemberCnt: String?, selectedSchecode: String?) {

        val homeFragment = HomeFragment().apply {
            arguments = Bundle().apply {
                putString("JOINED_MEMBER_COUNT", joinedMemberCnt)
                putString("SELECTED_SCHE_CODE", selectedSchecode)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentClubHome, homeFragment)
            .commit()
    }

    fun updateInterestStatus(@FieldMap params: Map<String, String>) {
        val service = Server(this).service
        service.updateInterestStatus(params).enqueue(object : Callback<InterestedResVO> {
            override fun onResponse(call: Call<InterestedResVO>, response: Response<InterestedResVO>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == true) {
                        // 성공적으로 업데이트되었을 때의 처리
                        Log.d("InterestedUpdate", "관심 모임 설정 성공")
                    } else {
                        // 서버로부터 실패 응답을 받았을 때의 처리
                        Log.d("InterestedUpdate", "관심 모임 설정 실패")
                    }
                } else {
                    // 서버로부터 에러 응답을 받았을 때의 처리
                    Log.d("InterestedUpdate", "서버에러 - 관심 모임 설정 실패")
                }
            }

            override fun onFailure(call: Call<InterestedResVO>, t: Throwable) {
                // 통신 실패 시 처리
                Log.e("FavoriteUpdate", "통신 실패", t)
            }
        })
    }
}