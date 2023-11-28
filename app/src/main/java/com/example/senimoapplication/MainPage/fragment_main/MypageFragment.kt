package com.example.senimoapplication.MainPage.fragment_main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.senimoapplication.MainPage.Activity_main.EditMyPageActivity
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.MainPage.VO_main.getMyPageVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentMypageBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    private lateinit var myProfile: MyPageVO // MyPageVO 객체를 담을 변수

    private val INTRO_MAX_TEXT_LENGTH = 64 // 클래스 레벨로 상수 이동 : 최대 글자 수
    private var fullIntroText: String? = null

    private val editProfileResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            fetchUserData()
//             val updatedProfile = result.data?.extras?.getParcelable<getMyPageVO>("updatedProfileData")
//            // val updatedProfile = result.data?.extras?.getParcelable<getMyPageVO>("myProfileData")
//             updatedProfile?.let {
//                 // myProfile 객체 업데이트
//                 myProfile = it.result
//                 updateUIWithProfile(myProfile) // UI 업데이트 함수 호출
//                 Log.d("MypageFragment", "수정된 프로필 받음: $myProfile")
//
//             }?: Log.e("MypageFragment", "수정된 프로필 데이터가 null임")
        } else {
            Log.e("MypageFragment", "결과가 OK가 아님. 결과 코드: ${result.resultCode}")
        }

    }

    private fun updateUIWithProfile(it: MyPageVO) {
        Glide.with(this)
            .load(it.img)
            .placeholder(R.drawable.animation_loading)
            .error(R.drawable.ic_profile_circle)
            .centerCrop()
            .into(binding.imgMMypageImg)

        binding.tvMUserName.text = it.name                               // 이름
        binding.tvMUserGu.text = it.gu                                  // 구

        val birthYearText = "${it.birth}"
        binding.tvMBirthYear.text = if (birthYearText.length > 4) {           // 출생년도
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
        binding.tvMGender.text = genderTransformed                        // 성별

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
        binding.tvMUserIntro.text = introText                               // 소개글

        binding.tvMUserIntroMore.setOnClickListener {
//            binding.tvMUserIntro.text = myProfile.intro
//            binding.tvMUserIntroMore.visibility = View.INVISIBLE
//            val latestUserData = PreferenceManager.getUser(requireContext())
//            binding.tvMUserIntro.text = latestUserData?.user_introduce
//            binding.tvMUserIntro.text = introText
//            binding.tvMUserIntroMore.visibility = View.INVISIBLE
            binding.tvMUserIntro.text = fullIntroText // 전체 소개글 사용
            binding.tvMUserIntroMore.visibility = View.INVISIBLE

        }

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        // 초기 데이터 로드
        fetchUserData()

        // 뱃지 상태를 나타내는 가데이터
        // val badges = listOf(true, false, true, true, false, false, false, false, false)

//        val userData = PreferenceManager.getUser(requireContext())
//
//        myProfile = MyPageVO(
//            img = userData?.user_img ?: "",
//            name = userData?.user_name ?: "",
//            gu = userData?.user_gu ?: "",
//            dong = userData?.user_dong ?: "",
//            birth = userData?.birth_year ?: 0,
//            gender = userData?.gender ?: "",
//            intro = userData?.user_introduce ?: "",
//            // badges = badges // 예시로 badges 목록을 추가합니다.
//        )
//        updateUIWithProfile(myProfile) // 초기 UI 설정

        // 뱃지 상태에 따라 이미지 리소스 업데이트
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



        binding.tvMMoveEdit.setOnClickListener {
            // 프로필 편집 화면으로 이동
            val userData = PreferenceManager.getUser(requireContext())
            val intent = Intent(requireContext(),EditMyPageActivity::class.java)
//            // userData 객체를 Intent에 추가
//            intent.putExtra("myProfileData", userData)
            // intent.putExtra("myProfileData", myProfile)
            intent.putExtra("introLength", userData?.user_introduce?.length ?: 0)
            editProfileResultLauncher.launch(intent)
            // startActivity(intent)
            activity?.finish()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // updateUIWithProfile()
        fetchUserData() // 화면이 다시 보여질 때마다 데이터를 새로고침
    }

//    private fun updateUIWithProfile() {
//        val userData = PreferenceManager.getUser(requireContext())
//        val userId = userData?.user_id
//        userId?.let {
//            // 여기에서 프로필 정보를 UI 요소에 설정
//            Glide.with(this)
//                .load(userData?.user_img)
//                .placeholder(R.drawable.animation_loading)
//                .error(R.drawable.ic_profile_circle)
//                .centerCrop()
//                .into(binding.imgMMypageImg)
//
//            binding.tvMUserName.text = userData?.user_name                               // 이름
//            binding.tvMUserGu.text = userData?.user_gu                                   // 구
//
//            val birthYearText = "${userData?.birth_year}"
//            binding.tvMBirthYear.text = if (birthYearText.length > 4) {           // 출생년도
//                birthYearText.substring(0, 4) + "년생"
//            } else {
//                birthYearText + "년생"
//            }
//
//            // 성별 데이터 변환
//            val genderTransformed = when (userData?.gender) {
//                "F", "여" -> "여"
//                "M", "남" -> "남"
//                else -> userData?.gender
//            }
//            binding.tvMGender.text = genderTransformed                        // 성별
//
//
//            val INTRO_MAX_TEXT_LENGTH = 64
//            val introText = if ((userData?.user_introduce?.length ?: 0) > INTRO_MAX_TEXT_LENGTH) {
//                binding.tvMUserIntroMore.visibility = View.VISIBLE
//                userData?.user_introduce?.substring(0, INTRO_MAX_TEXT_LENGTH) + "..."
//            } else {
//                binding.tvMUserIntroMore.visibility = View.INVISIBLE
//                userData?.user_introduce
//            }
//            binding.tvMUserIntro.text = introText                               // 소개글
//
//            // 뱃지 카운트 업데이트
////        val badgeCount = profile.badges.count { it } // 'true'인 항목의 개수 세기
////        binding.tvMBadgeCnt.text = badgeCount.toString()
//
//            Log.d("MypageFragment", "프로필 데이터 업데이트 되었음!")
//            Log.d("MypageFragment", "이미지 : ${userData?.user_img}")
//            Log.d("MypageFragment", "이름: ${userData?.user_name}")
//            Log.d("MypageFragment", "구: ${userData?.user_gu}")
//            Log.d("MypageFragment", "출생년도: ${userData?.birth_year}")
//            Log.d("MypageFragment", "성별: ${genderTransformed}")
//            Log.d("MypageFragment", "소개글: $introText")
//        }
//        }


    // 사용자 프로필 정보 업데이트 함수
    fun fetchUserData() {
        val userId = PreferenceManager.getUser(requireContext())?.user_id
        userId?.let {
            val service = Server(requireContext()).service
            service.getUserProfile(it).enqueue(object : Callback<MyPageVO>{
                override fun onResponse(call: Call<MyPageVO>, response: Response<MyPageVO>) {
                    if(response.isSuccessful) {
                        val userProfile = response.body()
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
//        if (userId != null) {
//            val service = Server(requireContext()).service
//            service.getUserProfile(userId).enqueue(object : Callback<MyPageVO> {
//                override fun onResponse(call: Call<MyPageVO>, response: Response<MyPageVO>) {
//                    if(response.isSuccessful) {
//                        Log.d("MypageFragment","응답 성공 : ${response.body().toString()}")
//                        val userProfile = response.body()
//                        userProfile?.let { updateUIWithProfile(it) }
//
//                    } else {
//                        Log.e("MypageFragment", "응답 실패 : ${response.code()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<MyPageVO>, t: Throwable) {
//                    Log.e("MypageFragment", "네트워크 요청 실패", t)
//                }
//
//            })
//        } else {
//            Log.e("MypageFragment", "User ID is null")
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}