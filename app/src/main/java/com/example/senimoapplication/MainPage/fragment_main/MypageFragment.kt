package com.example.senimoapplication.MainPage.fragment_main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.senimoapplication.MainPage.Activity_main.EditMyPageActivity
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentMypageBinding
import com.google.android.material.imageview.ShapeableImageView

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

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root
        // val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        // 뷰에 데이터 설정
//        val imgMypageImg = view.findViewById<ShapeableImageView>(R.id.img_M_MypageImg)
//        val tvMoveEdit = view.findViewById<TextView>(R.id.tv_M_MoveEdit)
//        val tvuserIntroMore = view.findViewById<TextView>(R.id.tv_M_UserIntroMore)
//        val tvUserName =  view.findViewById<TextView>(R.id.tv_M_UserName)
//        val tvUserGu = view.findViewById<TextView>(R.id.tv_M_UserGu)
//        val tvBirthYear = view.findViewById<TextView>(R.id.tv_M_BirthYear)
//        val tvGender = view.findViewById<TextView>(R.id.tv_M_gender)
//        val tvUserIntro = view.findViewById<TextView>(R.id.tv_M_UserIntro)

        myProfile = MyPageVO(
            "user1_profile.jpg",
            "체리마루",
            "남구",
            1995,
            "여성",
            "안녕하세요~호호호호호홍가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라"
        )

        updateUIWithProfile(myProfile) // 초기 UI 설정



        // 소개글 가져오기
        val intro = myProfile.intro

        // 글자 수가 최대 길이보다 길 경우 생략 부호(...) 추가하여 자르기
        val introTruncatedName = if (intro.length > INTRO_MAX_TEXT_LENGTH) {
            intro.substring(0, INTRO_MAX_TEXT_LENGTH) + "..."
        } else {
            intro // 글자 수 최대 길이 이하인 경우 그대로 표시
        }

        // 데이터 설정
        binding.imgMMypageImg.setImageResource(R.drawable.tea_img)
        binding.tvMUserName.text = myProfile.name
        binding.tvMUserGu.text = myProfile.gu
        binding.tvMBirthYear.text = "${myProfile.birth.toString()}년생"
        binding.tvMGender.text = myProfile.gender
        binding.tvMUserIntro.text = introTruncatedName  // 글자 수 제한

        // 로그 사용하여 데이터 확인
        Log.d(
            "ProfileData",
            "닉네임 : ${myProfile.name}, 구 : ${myProfile.gu}, 생년 : ${myProfile.birth}, 성별 : ${myProfile.gender}, 소개 : ${myProfile.intro}"
        )

        binding.tvMUserIntroMore.setOnClickListener {
            binding.tvMUserIntro.text = myProfile.intro
            binding.tvMUserIntroMore.visibility = View.INVISIBLE
        }

        binding.tvMMoveEdit.setOnClickListener {
            val intent = Intent(requireContext(),EditMyPageActivity::class.java)
            // myProfile 객체를 Intent에 추가
            intent.putExtra("myProfileData", myProfile)
            editProfileResultLauncher.launch(intent)
//            startActivity(intent)
//            activity?.finish()
        }

        return view
    }

    private fun updateUIWithProfile(profile: MyPageVO) {
        // 여기에서 프로필 정보를 UI 요소에 설정
        if (profile.img.startsWith("content://")) {
            Glide.with(this)
                .load(profile.img)
                .centerCrop()
                .into(binding.imgMMypageImg)
        } else {
            // 기본 이미지 설정
            binding.imgMMypageImg.setImageResource(R.drawable.ic_profile_circle)
        }            // 이미지 설정
        binding.tvMUserName.text = profile.name                               // 이름
        binding.tvMUserGu.text = profile.gu                                   // 구

        val birthYearText = "${profile.birth}"
        binding.tvMBirthYear.text = if (birthYearText.length > 4) {           // 출생년도
            birthYearText.substring(0, 4) + "년생"
        } else {
            birthYearText + "년생"
        }

        val genderText = if (profile.gender.length >2) {
            profile.gender.substring(0, 2)
        } else {
            profile.gender
        }
        binding.tvMGender.text = genderText                            // 성별

        val introText = if (profile.intro.length > INTRO_MAX_TEXT_LENGTH) {
            binding.tvMUserIntroMore.visibility = View.VISIBLE
            profile.intro.substring(0, INTRO_MAX_TEXT_LENGTH) + "..."
        } else {
            binding.tvMUserIntroMore.visibility = View.INVISIBLE
            profile.intro
        }
        binding.tvMUserIntro.text = introText                               // 소개글

        // 로그 출력
        Log.d("MypageFragment", "프로필 데이터 업데이트 되었음!")
        Log.d("MypageFragment", "이름: ${profile.name}")
        Log.d("MypageFragment", "구: ${profile.gu}")
        Log.d("MypageFragment", "출생년도: ${profile.birth}")
        Log.d("MypageFragment", "성별: ${profile.gender}")
        Log.d("MypageFragment", "소개글: $introText")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


