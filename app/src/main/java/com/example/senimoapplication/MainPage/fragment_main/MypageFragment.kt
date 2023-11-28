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

        binding.tvMMoveEdit.setOnClickListener {
            // 프로필 편집 화면으로 이동
            val userData = PreferenceManager.getUser(requireContext())
            val intent = Intent(requireContext(),EditMyPageActivity::class.java)
            intent.putExtra("introLength", userData?.user_introduce?.length ?: 0)
            editProfileResultLauncher.launch(intent)
            activity?.finish()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        fetchUserData() // 화면이 다시 보여질 때마다 데이터를 새로고침
    }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}