package com.example.senimoapplication.MainPage.fragment_main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.senimoapplication.MainPage.Activity_main.EditMyPageActivity
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.MainPage.VO_main.UserBadgeResponse
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentMypageBinding
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    private lateinit var myProfile: MyPageVO // MyPageVO 객체를 담을 변수

    private val INTRO_MAX_TEXT_LENGTH = 52 // 클래스 레벨로 상수 이동 : 최대 글자 수

    private val editProfileResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedProfile = result.data?.getParcelableExtra<MyPageVO>("updatedProfileData")
            updatedProfile?.let {
                // myProfile 객체 업데이트
                myProfile = it
                updateUIWithProfile(it) // UI 업데이트 함수 호출
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        // 뱃지 상태를 나타내는 가데이터
//        val badges = listOf(true, false, true, true, false, false, false, false, false)
//
//        myProfile = MyPageVO(
//            "content://media/external/file/25",
//            "체리마루",
//            "북구",
//            "누문동",
//            1995,
//            "여성",
//            "안녕하세요~호호호호호홍가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라",
//            badges // 뱃지 상태 추가
//        )
//
//        updateUIWithProfile(myProfile) // 초기 UI 설정

//        // 뱃지 상태에 따라 이미지 리소스 업데이트
//        val badgeImageViews = listOf(
//            binding.imgMBadge1,
//            binding.imgMBadge2,
//            binding.imgMBadge3,
//            binding.imgMBadge4,
//            binding.imgMBadge5,
//            binding.imgMBadge6,
//            binding.imgMBadge7,
//            binding.imgMBadge8,
//            binding.imgMBadge9
//        )
//
//        badgeImageViews.forEachIndexed { index, imageView ->
//            val badgeActive = myProfile.badges.getOrNull(index) ?: false
//            val resource = if (badgeActive) {
//                resources.getIdentifier("ic_badge${index + 1}_on", "drawable", context?.packageName)
//            } else {
//                resources.getIdentifier("ic_badge${index + 1}_off", "drawable", context?.packageName)
//            }
//            imageView.setImageResource(resource)
//
//        }


        // 로그 사용하여 데이터 확인
//        Log.d(
//            "ProfileData",
//            "이미지 : ${myProfile.img},닉네임 : ${myProfile.name}, 구 : ${myProfile.gu}, 동 : ${myProfile.dong},생년 : ${myProfile.birth}, 성별 : ${myProfile.gender}, 소개 : ${myProfile.intro}"
//        )

        // 서버로부터 데이터 요청 및 처리
        fetchUserDataAndUpdateUI("T1")

        binding.tvMUserIntroMore.setOnClickListener {
            binding.tvMUserIntro.text = myProfile.intro
            binding.tvMUserIntroMore.visibility = View.INVISIBLE
        }

        binding.tvMMoveEdit.setOnClickListener {
            val intent = Intent(requireContext(),EditMyPageActivity::class.java)
            // myProfile 객체를 Intent에 추가
            intent.putExtra("myProfileData", myProfile)
            intent.putExtra("introLength", myProfile.intro.length)
             editProfileResultLauncher.launch(intent)
//            startActivity(intent)
//            activity?.finish()
        }



        return view
    }

    // 서버로부터 사용자 데이터를 가져오고 UI를 업데이트하는 함수
    private fun fetchUserDataAndUpdateUI(userId : String) {
        val service = Server(requireContext()).service
        service.getUserBadges(userId).enqueue(object : Callback<UserBadgeResponse> {
            override fun onResponse(
                call: Call<UserBadgeResponse>,
                response: Response<UserBadgeResponse>
            ) {
                Log.d("badgeData", response.toString())
                // 서버 응답이 null인지 확인
                if (response.isSuccessful) {
                    Log.d("badgeData2", response.toString())
                    val userBadgeResponse = response.body()
                    userBadgeResponse?.let {
                        myProfile = createMyPageVOFromResponse(it)
                        updateUIWithProfile(myProfile)
                    }
                } else {
                    // 오류 처리
                    Log.e("MypageFragment", "통신은 성공했으나 요청에 실패했습니다: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserBadgeResponse>, t: Throwable) {
                // 네트워크 요청 실패 시 처리
                Log.e("MypageFragment", "네트워크 요청 실패", t)
            }


        })
    }

    // UserBadgeResponse로부터 MypageVO 생성
    private fun createMyPageVOFromResponse(response: UserBadgeResponse): MyPageVO {
        // 사용자 정보가 담긴 첫 번째 객체를 가져옵니다.
        val userInfo = response.result.firstOrNull() ?: return MyPageVO()

        // badgeCodeToIndexMap는 뱃지 코드와 해당하는 MyPageVO 내 뱃지 리스트의 인덱스를 매핑합니다.
        val badgeCodeToIndexMap = mapOf(
            "badge_code01" to 0,
            "badge_code02" to 1,
            "badge_code03" to 2,
            "badge_code04" to 3,
            "badge_code05" to 4,
            "badge_code06" to 5,
            "badge_code07" to 6,
            "badge_code08" to 7,
            "badge_code09" to 8
        )

        // 뱃지의 기본 상태를 모두 비활성화(false)로 설정합니다.
        val badges = MutableList(9) { false }

        // 서버로부터 받은 각 뱃지 정보에 대해 해당 뱃지를 활성화합니다.
        response.result.forEach { badgeInfo ->
            badgeCodeToIndexMap[badgeInfo.badgeCode]?.let { Index ->
                badges[Index] = true
            }
        }

        // 성별 데이터 변환
        val genderTransformed = when (userInfo.gender) {
            "F", "여" -> "여"
            "M", "남" -> "남"
            else -> userInfo.gender
        }

        // MyPageVO 객체를 생성하여 반환합니다.
        return MyPageVO(
            img = userInfo.userImg,
            name = userInfo.userName,
            gu = userInfo.userGu,
            dong = userInfo.userDong,
            birth = userInfo.birthYear,
            gender = genderTransformed,
            intro = userInfo.userIntroduce,
            badges = badges
        )
    }

    private fun updateUIWithProfile(profile: MyPageVO) {
        // 여기에서 프로필 정보를 UI 요소에 설정
        Glide.with(this)
            .load(profile.img)
            .placeholder(R.drawable.animation_loading)
            .error(R.drawable.ic_profile_circle)
            .centerCrop()
            .into(binding.imgMMypageImg)

        binding.tvMUserName.text = profile.name                               // 이름
        binding.tvMUserGu.text = profile.gu                                   // 구

//        val birthYearText = "${profile.birth}"
//        binding.tvMBirthYear.text = if (birthYearText.length > 4) {           // 출생년도
//            birthYearText.substring(0, 4) + "년생"
//        } else {
//            birthYearText + "년생"
//        }

        binding.tvMBirthYear.text = "${profile.birth}년생"                  // 출생년도

//        val genderText = if (profile.gender.length >2) {
//            profile.gender.substring(0, 2)
//        } else {
//            profile.gender
//        }
//        binding.tvMGender.text = genderText                            // 성별

        binding.tvMGender.text = profile.gender

        val introText = if (profile.intro.length > INTRO_MAX_TEXT_LENGTH) {
            binding.tvMUserIntroMore.visibility = View.VISIBLE
            profile.intro.substring(0, INTRO_MAX_TEXT_LENGTH) + "..."
        } else {
            binding.tvMUserIntroMore.visibility = View.INVISIBLE
            profile.intro
        }
        binding.tvMUserIntro.text = introText                               // 소개글

        // 뱃지 상태에 따라 이미지 리소스 업데이트
        updateBadges(profile.badges)

//        // 뱃지 카운트 업데이트
//        val badgeCount = profile.badges.count { it } // 'true'인 항목의 개수 세기
//        binding.tvMBadgeCnt.text = badgeCount.toString()

        // 로그 출력
        Log.d("MypageFragment", "프로필 데이터 업데이트 되었음!")
        Log.d("MypageFragment", "이미지 : ${profile.img}")
        Log.d("MypageFragment", "이름: ${profile.name}")
        Log.d("MypageFragment", "구: ${profile.gu}")
        Log.d("MypageFragment", "동: ${profile.dong}")
        Log.d("MypageFragment", "출생년도: ${profile.birth}")
        Log.d("MypageFragment", "성별: ${profile.gender}")
        Log.d("MypageFragment", "소개글: $introText")
        Log.d("MypageFragment", "뱃지 개수: ${updateBadges(profile.badges)}")
    }

    private fun updateBadges(badges: List<Boolean>) {
        val badgeImageViews = listOf(
            binding.imgMBadge1,
            binding.imgMBadge2,
            binding.imgMBadge3,
            binding.imgMBadge4,
            binding.imgMBadge5,
            binding.imgMBadge6,
            binding.imgMBadge7,
            binding.imgMBadge8,
            binding.imgMBadge9
        )

        badgeImageViews.forEachIndexed { index, imageView ->
            val badgeActive = myProfile.badges.getOrNull(index) ?: false
            val resource = if (badgeActive) {
                resources.getIdentifier("ic_badge${index + 1}_on", "drawable", context?.packageName)
            } else {
                resources.getIdentifier("ic_badge${index + 1}_off", "drawable", context?.packageName)
            }
            imageView.setImageResource(resource)

        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


