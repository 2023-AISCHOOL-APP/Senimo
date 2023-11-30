package com.example.senimoapplication.Club.Activity_club

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.senimoapplication.MainPage.VO_main.BadgeRes
import com.example.senimoapplication.MainPage.VO_main.BadgeVO
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityUserProfileBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class userProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent관리
        val selectedUserId = intent.getStringExtra("selected_user")
        Log.d("userProfile", "선택한 사용자 정보 : $selectedUserId")

        fetchUserData(selectedUserId)
        getUserBadge(selectedUserId)

        // 뒤로가기 버튼
        binding.icBack.setOnClickListener {
            finish()
        }





    }
    // 선택한 사용자 정보 가져오기
    fun fetchUserData(userId : String?) {
            val service = Server(this@userProfileActivity).service
            service.getUserProfile(userId).enqueue(object : Callback<MyPageVO> {
                override fun onResponse(call: Call<MyPageVO>, response: Response<MyPageVO>) {
                    if(response.isSuccessful) {
                        val userProfile = response.body()
                        binding.tvUserName.text = userProfile?.name
                        binding.tvUserIntro.text = userProfile?.intro
                        binding.tvUserLoca.text = userProfile?.gu
                        Glide.with(this@userProfileActivity)
                            .load(userProfile?.img)
                            .placeholder(R.drawable.animation_loading)
                            .error(R.drawable.ic_profile_circle)
                            .centerCrop()
                            .into(binding.imgUser)

                        val birthYearText = "${userProfile?.birth}"
                        binding.tvUserBirthY.text = if (birthYearText.length > 4) { // 출생년도
                            birthYearText.substring(0, 4) + "년생"
                        } else {
                            birthYearText + "년생"
                        }

                        // 성별 데이터 변환
                        val genderTransformed = when (userProfile?.gender) {
                            "F", "여" -> "여"
                            "M", "남" -> "남"
                            else -> userProfile?.gender
                        }
                        binding.tvUserGender.text = genderTransformed // 성별



                    } else {
                        Log.e("MypageFragment", "응답실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<MyPageVO>, t: Throwable) {
                    Log.e("MypageFragment", "네트워크 요청 실패", t)
                }
            })
    }

    // 선택한 사용자 뱃지 정보 가져오기
    private fun getUserBadge(userId: String?) {
        val service = Server(this@userProfileActivity).service
        val call = service.getUserBadge(userId)

        call.enqueue(object : Callback<BadgeRes> {
            override fun onResponse(call: Call<BadgeRes>, response: Response<BadgeRes>) {
                if (response.isSuccessful) {
                    val badgeList: List<BadgeVO>? = response.body()?.badges
                    val badgeCount: Int = response.body()?.badgeCnt ?: 0 // badgeCnt 가져오기
                    Log.d("MyPageFragment 뱃지 리스트1", badgeList.toString())
                    Log.d("MyPageFragment 뱃지 개수1", badgeCount.toString())
                    if (badgeList != null) {
                        // 뱃지 이미지 변경 함수 호출
                        updateBadgeImages(badgeList)
                        // tvMBadgeCnt에 badgeCnt 값 설정
                        binding.tvBadgeCnt.text = badgeCount.toString()
                    }
                } else {
                    Log.d("MyPageFragment 뱃지 리스트3", "응답 실패: ${response.code()} ${response.message()}")
                    Log.d("MyPageFragment 뱃지 리스트3", "${userId}")
                }
            }

            override fun onFailure(call: Call<BadgeRes>, t: Throwable) {
                Log.e("MyPageFragment 뱃지 리스트", "뱃지 가져오기 통신 실패", t)
            }

        })
    }

    // 해당하는 badge_code0가 있는 경우 배지 이미지를 변경(활성화)
    private fun updateBadgeImages(badgeList: List<BadgeVO>) {
        for (badge in badgeList) {
            when (badge.badgeCode) {
                "badge_code01" -> {
                    binding.imgBadge1.setImageResource(R.drawable.ic_badge1_on)
                }
                "badge_code02" -> {
                    binding.imgBadge2.setImageResource(R.drawable.ic_badge2_on)
                }
                "badge_code03" -> {
                    binding.imgBadge3.setImageResource(R.drawable.ic_badge3_on)
                }
                "badge_code04" -> {
                    binding.imgBadge4.setImageResource(R.drawable.ic_badge4_on)
                }
                "badge_code05" -> {
                    binding.imgBadge5.setImageResource(R.drawable.ic_badge5_on)
                }
                "badge_code06" -> {
                    binding.imgBadge6.setImageResource(R.drawable.ic_badge6_on)
                }
                "badge_code07" -> {
                    binding.imgBadge7.setImageResource(R.drawable.ic_badge7_on)
                }
                "badge_code08" -> {
                    binding.imgBadge8.setImageResource(R.drawable.ic_badge8_on)
                }
                "badge_code09" -> {
                    binding.imgBadge9.setImageResource(R.drawable.ic_badge9_on)
                }
            }
        }
    }

}