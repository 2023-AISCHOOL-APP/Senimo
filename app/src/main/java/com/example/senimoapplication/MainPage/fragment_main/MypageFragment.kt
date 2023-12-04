package com.example.senimoapplication.MainPage.fragment_main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.senimoapplication.Common.showBadgeDialogBox
import com.example.senimoapplication.MainPage.Activity_main.EditMyPageActivity
import com.example.senimoapplication.MainPage.VO_main.BadgeRes
import com.example.senimoapplication.MainPage.VO_main.BadgeVO
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentMypageBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    private lateinit var myProfile: MyPageVO // MyPageVO 객체를 담을 변수

    private val INTRO_MAX_TEXT_LENGTH = 64 // 클래스 레벨로 상수 이동 : 최대 글자 수
    private var fullIntroText: String? = null
    private var userProfile : MyPageVO? = null
    private val editProfileResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("EditInfo동작2","호출은 왔나?")
        Log.d("EditInfo동작2",result.toString())
        Log.d("EditInfo동작2",Activity.RESULT_OK.toString())
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedProfileData = result.data?.getParcelableExtra<MyPageVO>("updatedProfileData")
            Log.d("EditInfo동작2",updatedProfileData.toString())
            updatedProfileData?.let {

                Log.d("EditInfo동작1","2")
                updateUIWithProfile(it)
                Log.d("EditInfo동작1","3")
                // 필요하다면 추가 작업 수행
            }
            //fetchUserData()
        } else {
            Log.e("MypageFragment", "결과가 OK가 아님. 결과 코드: ${result.resultCode}")
        }
    }

    private fun updateUIWithProfile(it: MyPageVO) {
        Log.d("EditInfo동작1","4")
        Glide.with(this)
            .load(it.img)
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 무시
            .skipMemoryCache(true) // 메모리 캐시 무시
            .placeholder(R.drawable.animation_loading)
            .error(R.drawable.ic_profile_circle)
            .centerCrop()
            .into(binding.imgMMypageImg)

        binding.tvMUserName.text = it.name // 이름
        binding.tvMUserGu.text = it.gu // 구

        val birthYearText = "${it.birth}"
        binding.tvMBirthYear.text = if (birthYearText.length > 4) { // 출생년도
            birthYearText.substring(0, 4) + "년생"
        } else {
            birthYearText + "년생"
        }

        // 성별 데이터 변환
        val genderTransformed = when (it.gender) {
            "F", "여" -> "여"
            "M", "남" -> "남"
            else -> it.gender
        }
        binding.tvMGender.text = genderTransformed // 성별

        val INTRO_MAX_TEXT_LENGTH = 64
        val introText = if ((it.intro.length ?: 0) > INTRO_MAX_TEXT_LENGTH) {
            binding.tvMUserIntroMore.visibility = View.VISIBLE
            fullIntroText = it.intro // 전체 소개글 저장
            it.intro.substring(0, INTRO_MAX_TEXT_LENGTH) + "..."
        } else {
            binding.tvMUserIntroMore.visibility = View.INVISIBLE
            fullIntroText = it.intro // 전체 소개글 저장
            it.intro
        }
        binding.tvMUserIntro.text = introText // 소개글

        binding.tvMUserIntroMore.setOnClickListener {
            binding.tvMUserIntro.text = fullIntroText // 전체 소개글 사용
            binding.tvMUserIntroMore.visibility = View.INVISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        // 초기 데이터 로드
        fetchUserData()

        // fragment_mypage.xml에 있는 배지 이미지뷰
        for (i in 1..9) {
            val badgeId = resources.getIdentifier("img_M_Badge$i","id", requireContext().packageName)
            val imgMBage = view?.findViewById<ImageView>(badgeId)

            imgMBage?.setOnClickListener {
                // 배지 상태에 따라 badgeGetInfo 설정
                val badgeGetInfo = when (imgMBage.tag) {
                    "on" -> "보유 중"
                    "off" -> "획득 방법"
                    else -> null
                }

                // 배지에 대한 정보 설정
                val badgeNames = arrayOf(
                    "시니모뉴비", "용기있는뉴비", "새싹모임러", "이구역모임왕", "호기심탐험가",
                    "모임의기둥", "프로참석러", "내가바로모임장", "뱃지마스터"
                )
                val badgeName = badgeNames[i - 1]  // 배열 인덱스는 0부터 시작하므로 i - 1

                // text1 설정
                val text1 = if (badgeGetInfo == "보유 중") {
                    arrayOf(
                        "시니모에 가입하셨군요!", "오프라인 모임을 처음 참여하셨군요!", "모임에 처음 가입하셨군요!",
                        "모임을 5개나 가입하셨군요!", "모임을 5개나 찜하셨군요!", "오프라인 모임을 5번 참여하셨군요!",
                        "오프라인 모임을 15번 참여하셨군요!", "모임을 생성하셨군요!", "배지를 모두 얻으셨군요!"
                    )[i - 1]
                } else {
                    arrayOf(
                        "시니모에 처음 가입하시면", "처음으로 오프라인 모임에 참여하시면", "처음으로 모임에 가입하시면",
                        "가입한 모임의 개수가 5개이시면", "5개의 모임에 찜을 하신 경우", "오프라인 모임에 5번 참여하시면",
                        "오프라인 모임에 15번 참여하시면", "모임을 생성하시면", "8개 배지를 모두 얻으시면"
                    )[i - 1]
                }

                // text2 설정
                val text2 = if (badgeGetInfo == "보유 중") {
                    arrayOf(
                        "회원가입을 환영합니다. :)", "이제부터 당신은 용기있는뉴비!", "이제부터 당신은 새싹모임러!",
                        "이제부터 당신은 이구역모임왕!", "이제부터 당신은 호기심탐험가!", "이제부터 당신은 모임의기둥!",
                        "이제부터 당신은 프로참석러!", "이제부터 당신은 모임장입니다!", "이제부터 당신은 배지마스터!"
                    )[i - 1]
                } else {
                    arrayOf(
                        "시니모뉴비 배지가 주어져요.", "용기있는뉴비 배지가 주어져요.", "새싹모임러 배지가 주어져요.",
                        "이구역모임왕 배지가 주어져요.", "호기심탐험가 배지가 주어져요.", "모임의기둥 배지가 주어져요.",
                        "프로참석러 배지가 주어져요.", "내가바로모임장 배지가 주어져요.", "배지마스터 배지가 주어져요."
                    )[i - 1]
                }

                // 다이얼로그 내용 변경
                val badgeImageRes = if (badgeGetInfo == "보유 중") "ic_badge${i}_on" else "ic_badge${i}_off"
                showBadgeDialogBox(requireActivity(), badgeImageRes, badgeName, badgeGetInfo, text1, text2)
            }
        }

        binding.tvMMoveEdit.setOnClickListener {
            // 프로필 편집 화면으로 이동
            val userData = PreferenceManager.getUser(requireContext())
            val intent = Intent(requireContext(),EditMyPageActivity::class.java)
            intent.putExtra("introLength", userData?.user_introduce?.length ?: 0)
            intent.putExtra("newprofile",userProfile)
            Log.d("EditInfo동작1","6")
            editProfileResultLauncher.launch(intent)
            activity?.finish()
        }

        getUserBadge()

        return view
    }

//    override fun onResume() {
//        super.onResume()
//        fetchUserData() // 화면이 다시 보여질 때마다 데이터를 새로고침
//    }

    // 사용자 프로필 정보 업데이트 함수
    fun fetchUserData() {
        Log.d("EditInfo동작1","5")
        val userId = PreferenceManager.getUser(requireContext())?.user_id
        userId?.let {
            val service = Server(requireContext()).service
            service.getUserProfile(it).enqueue(object : Callback<MyPageVO>{
                override fun onResponse(call: Call<MyPageVO>, response: Response<MyPageVO>) {
                    if(response.isSuccessful) {
                        userProfile = response.body()
                        Log.e("MypageFragment", userProfile.toString())
                        userProfile?.let { profile ->
                            updateUIWithProfile(profile)
                        }
                    } else {
                        Log.e("MypageFragment", "응답실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<MyPageVO>, t: Throwable) {
                    Log.e("MypageFragment", "네트워크 요청 실패", t)
                }
            })
        } ?: Log.e("MypageFragment", "User ID is null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserBadge() {
        val userData = PreferenceManager.getUser(requireContext())
        val userId = userData?.user_id.toString()

        val service = Server(requireContext()).service
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
                        binding.tvMBadgeCnt.text = badgeCount.toString()
                    }
                } else {
                    Log.d("MyPageFragment 뱃지 리스트3", "응답 실패: ${response.code()} ${response.message()}")
                    Log.d("MyPageFragment 뱃지 리스트3", userId)
                }
            }

            override fun onFailure(call: Call<BadgeRes>, t: Throwable) {
                Log.e("MyPageFragment 뱃지 리스트", "뱃지 가져오기 통신 실패", t)
            }
        })
    }

    // 해당하는 badge_code0가 있는 경우 배지 이미지를 변경(활성화)
    private fun updateBadgeImages(badgeList: List<BadgeVO>) {
        // 모든 배지를 먼저 비활성화 상태로 설정
        for (i in 1..9) {
            val badgeImageView = binding.root.findViewById<ImageView>(
                resources.getIdentifier("img_M_Badge$i", "id", requireContext().packageName)
            )
            badgeImageView.setImageResource(
                resources.getIdentifier("ic_badge${i}_off", "drawable", requireContext().packageName)
            )
            badgeImageView.tag = "off"
        }

        // 사용자가 가진 배지를 활성화 상태로 설정
        badgeList.forEach { badge ->
            val badgeNumber = badge.badgeCode.removePrefix("badge_code").toIntOrNull() ?: return@forEach
            val badgeImageView = binding.root.findViewById<ImageView>(
                resources.getIdentifier("img_M_Badge$badgeNumber", "id", requireContext().packageName)
            )
            badgeImageView.setImageResource(
                resources.getIdentifier("ic_badge${badgeNumber}_on", "drawable", requireContext().packageName)
            )
            badgeImageView.tag = "on"
        }

        // 활성화된 배지를 활성화 상태로 설정
        for (badge in badgeList) {
            when (badge.badgeCode) {
                "badge_code01" -> {
                    binding.imgMBadge1.setImageResource(R.drawable.ic_badge1_on)
                }
                "badge_code02" -> {
                    binding.imgMBadge2.setImageResource(R.drawable.ic_badge2_on)
                }
                "badge_code03" -> {
                    binding.imgMBadge3.setImageResource(R.drawable.ic_badge3_on)
                }
                "badge_code04" -> {
                    binding.imgMBadge4.setImageResource(R.drawable.ic_badge4_on)
                }
                "badge_code05" -> {
                    binding.imgMBadge5.setImageResource(R.drawable.ic_badge5_on)
                }
                "badge_code06" -> {
                    binding.imgMBadge6.setImageResource(R.drawable.ic_badge6_on)
                }
                "badge_code07" -> {
                    binding.imgMBadge7.setImageResource(R.drawable.ic_badge7_on)
                }
                "badge_code08" -> {
                    binding.imgMBadge8.setImageResource(R.drawable.ic_badge8_on)
                }
                "badge_code09" -> {
                    binding.imgMBadge9.setImageResource(R.drawable.ic_badge9_on)
                }
            }
        }
    }

    private fun activateBadge(badgeImageView: ImageView, badgeNumber: Int) {
        badgeImageView.setImageResource(
            resources.getIdentifier("ic_badge${badgeNumber}_on", "drawable", requireContext().packageName)
        )
        badgeImageView.tag = "on"
    }
}