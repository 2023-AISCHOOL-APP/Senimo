package com.example.senimoapplication.MainPage.fragment_main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.senimoapplication.MainPage.Activity_main.EditMyPageActivity
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.R
import com.google.android.material.imageview.ShapeableImageView

class MypageFragment : Fragment() {

    private lateinit var myProfile: MyPageVO // MyPageVO 객체를 담을 변수

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        // 뷰에 데이터 설정
        val imgMypageImg = view.findViewById<ShapeableImageView>(R.id.img_M_MypageImg)
        val tvMoveEdit = view.findViewById<TextView>(R.id.tv_M_MoveEdit)
        val tvuserIntroMore = view.findViewById<TextView>(R.id.tv_M_UserIntroMore)
        val tvUserName =  view.findViewById<TextView>(R.id.tv_M_UserName)
        val tvUserGu = view.findViewById<TextView>(R.id.tv_M_UserGu)
        val tvBirthYear = view.findViewById<TextView>(R.id.tv_M_BirthYear)
        val tvGender = view.findViewById<TextView>(R.id.tv_M_gender)
        val tvUserIntro = view.findViewById<TextView>(R.id.tv_M_UserIntro)

        myProfile = MyPageVO(
            "user1_profile.jpg",
            "체리마루",
            "남구",
            1995,
            "여성",
            "안녕하세요~호호호호호홍가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라가나다라마바아자차카타파라라라라라라"
        )

        val INTRO_MAX_TEXT_LENGTH = 52 // 최대 글자 수

        // 소개글 가져오기
        val intro = myProfile.intro

        // 글자 수가 최대 길이보다 길 경우 생략 부호(...) 추가하여 자르기
        val introTruncatedName = if (intro.length > INTRO_MAX_TEXT_LENGTH) {
            intro.substring(0, INTRO_MAX_TEXT_LENGTH) + "..."
        } else {
            intro // 글자 수 최대 길이 이하인 경우 그대로 표시
        }

        // 데이터 설정
        imgMypageImg.setImageResource(R.drawable.tea_img)
        tvUserName.text = myProfile.name
        tvUserGu.text = myProfile.gu
        tvBirthYear.text = "${myProfile.birth.toString()}년생"
        tvGender.text = myProfile.gender
        tvUserIntro.text = introTruncatedName  // 글자 수 제한

        // 로그 사용하여 데이터 확인
        Log.d(
            "ProfileData",
            "닉네임 : ${myProfile.name}, 구 : ${myProfile.gu}, 생년 : ${myProfile.birth}, 성별 : ${myProfile.gender}, 소개 : ${myProfile.intro}"
        )

        tvuserIntroMore.setOnClickListener {
            tvUserIntro.text = myProfile.intro
            tvuserIntroMore.visibility = View.INVISIBLE
        }

        tvMoveEdit.setOnClickListener {
            val intent = Intent(requireContext(),EditMyPageActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

}


