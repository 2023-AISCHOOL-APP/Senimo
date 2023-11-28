package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.example.senimoapplication.server.Token.PreferenceManager
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.FieldMap

class ClubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club)

        // Intent 관리
        val clickedMeeting = intent.getParcelableExtra<MeetingVO>("clickedMeeting")

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
        val imgLike = findViewById<ImageView>(R.id.imgLike)
        val tvClubName = findViewById<TextView>(R.id.tvClubName)

        // 앱 바 기능 구현
        if (clickedMeeting != null) {
            Log.d("ClubActivity", clickedMeeting.toString())
            // 타이틀 변경
            val title = clickedMeeting.title
            tvClubName.text = if (title.length > 13) {
                title.substring(0, 13) + "..."
            } else {
                title
            }

            // 찜 기능
            val clubCode = clickedMeeting.club_code
            val userData = PreferenceManager.getUser(this@ClubActivity)
            val userId = userData!!.user_id
            Log.d("ClubActivity", "찜 기능 통신 데이터 $clubCode, $userId")

            // 이미지 상태를 서버에서 가져오고 업데이트
            initialInterestedClub(clubCode, userId, imgLike)

            imgLike.setOnClickListener {
                Log.d("ClubActivity", "찜 기능 재통신 $clubCode, $userId")
                updateInterestStatus(clubCode, userId, imgLike)
            }

            // 뒤로가기 버튼
            icBack.setOnClickListener {
                val intent = Intent(this@ClubActivity, MainActivity::class.java)
                startActivity(intent)
                Log.d("ClubActivity", "뒤로가기 버튼")
                finish()
            }
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
    }

    fun initialInterestedClub(clubCode: String, userId : String, imgLike: ImageView) {
        val service = Server(this).service
        service.initialInterestedClub(clubCode, userId).enqueue(object : Callback<InterestedResVO> {
            override fun onResponse(call: Call<InterestedResVO>, response: Response<InterestedResVO>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()?.status
                    if (apiResponse == true) {
                        // 관심 모임 추가
                        imgLike.setImageResource(R.drawable.ic_fullheart)
                        Log.d("ClubActivity", "$apiResponse : 관심 모임 설정 ")
                    } else {
                        // 관심 모임 삭제
                        imgLike.setImageResource(R.drawable.ic_lineheart)
                        Log.d("ClubActivity", "$apiResponse : 관심 모임 삭제")
                    }
                } else {
                    // 서버로부터 에러 응답을 받았을 때의 처리
                    imgLike.setImageResource(R.drawable.ic_lineheart)
                    Log.d("InterestedUpdate", "서버에러 - 관심 모임 설정 실패")
                    Toast.makeText(applicationContext, "네트워크 오류 - 다시 한 번 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<InterestedResVO>, t: Throwable) {
                // 통신 실패 시 처리
                imgLike.setImageResource(R.drawable.ic_heart)
                Log.e("FavoriteUpdate", "통신 실패", t)
            }
        })
    }

    fun updateInterestStatus(clubCode: String, userId : String, imgLike: ImageView) {
        val service = Server(this).service
        service.updateInterestStatus(clubCode, userId).enqueue(object : Callback<InterestedResVO> {
            override fun onResponse(call: Call<InterestedResVO>, response: Response<InterestedResVO>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()?.status
                    if (apiResponse == true) {
                        // 관심 모임 추가
                        imgLike.setImageResource(R.drawable.ic_fullheart)
                        Log.d("ClubActivity", "$apiResponse : 관심 모임 설정 ")
                    } else {
                        // 관심 모임 삭제
                        imgLike.setImageResource(R.drawable.ic_lineheart)
                        Log.d("ClubActivity", "$apiResponse : 관심 모임 삭제")
                    }
                } else {
                    // 서버로부터 에러 응답을 받았을 때의 처리
                    imgLike.setImageResource(R.drawable.ic_lineheart)
                    Log.d("InterestedUpdate", "서버에러 - 관심 모임 설정 실패")
                    Toast.makeText(applicationContext, "네트워크 오류 - 다시 한 번 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<InterestedResVO>, t: Throwable) {
                // 통신 실패 시 처리
                imgLike.setImageResource(R.drawable.ic_heart)
                Log.e("FavoriteUpdate", "통신 실패", t)
            }
        })
    }
}