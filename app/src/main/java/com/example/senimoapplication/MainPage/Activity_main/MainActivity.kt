package com.example.senimoapplication.MainPage.Activity_main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.fragment.ChatFragment
import com.example.senimoapplication.MainPage.VO_main.ChatListVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.databinding.ActivityMainBinding
import com.example.senimoapplication.MainPage.fragment_main.ChatMainFragment
import com.example.senimoapplication.MainPage.fragment_main.HomeMainFragment
import com.example.senimoapplication.MainPage.fragment_main.MymeetingFragment
import com.example.senimoapplication.MainPage.fragment_main.MypageFragment
import com.example.senimoapplication.R

class MainActivity : AppCompatActivity() {

  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // 바인딩 객체 획득
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    // 액티비티 화면 출력
    setContentView(view)

    setupBottomNavigation()
    handleSelectedTabFromIntent()

    // ChatFragment에서 ChatMainFragment로 이동하는 함수 설정
    binding.imgMBackbtnToFrag1.setOnClickListener {
      navigateBackToChatMainFragment()
    }
  }

  /** 하단 바 네비게이션 함수 */
  private fun setupBottomNavigation() {
    // 아이콘 초기 설정 : M_tab1을 ic_home_color로 설정
    binding.bnvMain.itemIconTintList = null // 원래 이미지 색으로 보이게 하기
    binding.bnvMain.menu.findItem(R.id.M_tab1).icon = getDrawable(R.drawable.ic_home_color)

    binding.bnvMain.setOnItemSelectedListener { item ->

      // 기본 아이콘들
      val defaultIcons = mapOf(
        R.id.M_tab1 to R.drawable.ic_home_gray,
        R.id.M_tab2 to R.drawable.ic_mymeeting_gray,
        R.id.M_tab3 to R.drawable.ic_chat_gray,
        R.id.M_tab4 to R.drawable.ic_mypage_gray,
      )

      // 선택한 아이콘
      val selectedIcon = mapOf(
        R.id.M_tab1 to R.drawable.ic_home_color,
        R.id.M_tab2 to R.drawable.ic_mymeeting_color,
        R.id.M_tab3 to R.drawable.ic_chat_color,
        R.id.M_tab4 to R.drawable.ic_mypage_color,
      )

      // 아이콘 변경
      for (tabId in defaultIcons.keys) {
        val menuItem = binding.bnvMain.menu.findItem(tabId)
        if(tabId == item.itemId){
          // 선택한 탭의 아이콘을 색상이 변경된 버전으로 설정
          menuItem.icon = getDrawable(selectedIcon[tabId] ?: R.drawable.ic_home_gray)
        } else {
          // 다른 탭의 아이콘을 기본 상태로 설정
          menuItem.icon = getDrawable(defaultIcons[tabId] ?: R.drawable.ic_home_gray)
        }
      }

      // 모임 일정 리스트 가데이터
      val myscheduleList : List<ScheduleVO> = listOf(

        ScheduleVO("모임명","가나다라마바사아자차카타파하","","2023-11-22T12:00:08.123Z",30000, "광주 동구 제봉로 대성학원 3층",26, 20,"모집마감",""),
        ScheduleVO("모임명","시험 공부 준비합시다~!!","","2023-11-22T12:00:08.123Z",20000, "광주 동구 제봉로 대성학원 3층",10, 30,"모집중",""),
        ScheduleVO("모임명","빼빼로 만들자~","광주역 3번출구에서 만나요!!!!","2023-12-22T12:00:08.123Z",15000, "광주역앞10,",10,6 ,"모집중","")

        // 변수가 서로 다름 지금 지혜누나는 fee를 loca 위치에 써놓고, attendance에 모집중 모집마감이라 써져있음 ,"모임명",추가함 schedulvo에 club_code 추가해서

      )

      // 가입한 모임 리스트 가데이터
      val joinList : List<MeetingVO> = listOf(
        MeetingVO("동구","가나다라마바사아자차카타파하", "같이 골프 합시다~", "운동", 7,20,R.drawable.golf_img.toString(),""),
        MeetingVO("북구","동명동 티 타임dddddddddddddddddddd", "우리 같이 차 마셔요~ddddddddddddddddddddd", "취미", 5,10,R.drawable.tea_img.toString(),""),
        MeetingVO("광산구","열정 모임!!", "열정만 있다면 모두 가능합니다~", "자기계발", 8,10,R.drawable.tea_img.toString(),""),

        )



      // 관심 모임 리스트 가데이터
      val interestList : List<MeetingVO> = listOf(
        MeetingVO("남구","운암동 수영 모임", "헤엄 헤엄~", "운동", 8,30,R.drawable.tea_img.toString(),""),
        MeetingVO("북구","동명동 티 타임", "우리 같이 차 마셔요~", "취미", 5,10,R.drawable.tea_img.toString(),""),
        MeetingVO("광산구","열정 모임!!", "열정만 있다면 모두 가능합니다~", "자기계발", 8,10,R.drawable.tea_img.toString(),""),
      )


      // 환경설정 아이콘
      binding.imgMSettingbtn.setOnClickListener {
        val intent = Intent(this@MainActivity,SettingActivity::class.java)
        startActivity(intent)
        finish()
      }

      when(item.itemId){
        R.id.M_tab1 ->{
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            HomeMainFragment()
          ).commit()


          binding.tvMToptitle.text = "시니모"
          binding.imgMAlertbtn.visibility = View.VISIBLE
          binding.imgMSettingbtn.visibility = View.INVISIBLE
          binding.imgMBackbtnToFrag1.visibility = View.INVISIBLE
        }
        R.id.M_tab2 ->{
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            MymeetingFragment(myscheduleList, joinList, interestList)
          ).commit()

          // 여기서 TextView의 텍스트 변경과 ImageView 숨기기
          binding.tvMToptitle.text = "내 모임"
          binding.imgMAlertbtn.visibility = View.INVISIBLE
          binding.imgMSettingbtn.visibility = View.INVISIBLE
          binding.imgMBackbtnToFrag1.visibility = View.INVISIBLE

        }
        R.id.M_tab3 ->{
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            ChatMainFragment()
          ).commit()

          binding.tvMToptitle.text = "채팅"
          binding.imgMAlertbtn.visibility = View.INVISIBLE
          binding.imgMSettingbtn.visibility = View.INVISIBLE
          binding.imgMBackbtnToFrag1.visibility = View.INVISIBLE



        }


        R.id.M_tab4 ->{
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            MypageFragment()
          ).commit()

          binding.tvMToptitle.text = "내 정보"
          binding.imgMAlertbtn.visibility = View.INVISIBLE
          binding.imgMSettingbtn.visibility = View.VISIBLE
          binding.imgMBackbtnToFrag1.visibility = View.INVISIBLE

        }
      }

      true
    }

  }

  /** 선택된 탭 이동 함수 */
  private fun handleSelectedTabFromIntent() {
    // CreateMeetingActivity로부터 선택한 탭 정보 받기
    val selectedTab = intent.getStringExtra("selected_tab")
    if(selectedTab != null){
      when (selectedTab) {
        "M_tab1" -> binding.bnvMain.selectedItemId = R.id.M_tab1
        "M_tab2" -> binding.bnvMain.selectedItemId = R.id.M_tab2
        "M_tab3" -> binding.bnvMain.selectedItemId = R.id.M_tab3
        "M_tab4" -> binding.bnvMain.selectedItemId = R.id.M_tab4
      }
    }
  }

  // 뒤로가기 버튼을 눌렀을 때 M_tab1로 이동하도록 설정
  @SuppressLint("MissingSuperCall")
  override fun onBackPressed() {
    val currentTabId = binding.bnvMain.selectedItemId
    if (currentTabId != R.id.M_tab1) {
      binding.bnvMain.selectedItemId = R.id.M_tab1
    } else {
      super.onBackPressed()
    }
  }

  // ChatMainFragment 에서 ChatFragment로 이동시키게 하는 함수 만들기
  // 내 모임리스트 -> 내 모임 방
  // tab3 에서 fl화면 전환
  fun navigateToChatFragment(chatListVO: ChatListVO) {
    supportFragmentManager.beginTransaction().replace(
      R.id.fl,
      ChatFragment.newInstance(chatListVO)
    ).commit()

    // ChatFragment에 있을 때 뒤로가기 버튼 보이게 설정
    binding.imgMBackbtnToFrag1.visibility = View.VISIBLE
  }

  // ChatFragment에서 ChatMainFragment로 이동하는 함수
  fun navigateBackToChatMainFragment() {
    supportFragmentManager.beginTransaction().replace(
      R.id.fl,
      ChatMainFragment()
    ).commit()
    binding.imgMBackbtnToFrag1.visibility = View.INVISIBLE // 버튼 숨기기
  }

  }


interface OnChatItemClickListener {
  fun navigateToChatFragment(chatListVO: ChatListVO)
}