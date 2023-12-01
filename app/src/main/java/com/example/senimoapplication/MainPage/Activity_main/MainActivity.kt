package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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

//import com.example.senimoapplication.server.Token.TokenManager

class MainActivity : AppCompatActivity() {

  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // 바인딩 객체 획득
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    // 액티비티 화면 출력
    setContentView(view)

    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    setupBottomNavigation()
    handleSelectedTabFromIntent()

    // ChatFragment에서 ChatMainFragment로 이동하는 함수 설정
    binding.imgMBackbtnToFrag1.setOnClickListener {
      navigateBackToChatMainFragment()
    }

    var backPressedTime:Long = 0

    val callback = object : OnBackPressedCallback(true){
      override fun handleOnBackPressed() {
        val currentTabId = binding.bnvMain.selectedItemId
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fl)

        if (currentFragment is ChatFragment) {
          navigateBackToChatMainFragment()
        } else {
          if (currentTabId == R.id.M_tab1) {
            if(System.currentTimeMillis() - backPressedTime >= 2000){
              backPressedTime = System.currentTimeMillis()
              Toast.makeText(this@MainActivity, "뒤로가기 버튼을 한번 더 누르면 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
            } else if (System.currentTimeMillis() - backPressedTime < 2000){
              finish()
            }
          } else {
            binding.bnvMain.selectedItemId = R.id.M_tab1
          }
        }
      }
    }
    this.onBackPressedDispatcher.addCallback(this, callback)

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
        if (tabId == item.itemId) {
          // 선택한 탭의 아이콘을 색상이 변경된 버전으로 설정
          menuItem.icon = getDrawable(selectedIcon[tabId] ?: R.drawable.ic_home_gray)
        } else {
          // 다른 탭의 아이콘을 기본 상태로 설정
          menuItem.icon = getDrawable(defaultIcons[tabId] ?: R.drawable.ic_home_gray)
        }
      }

      // 환경설정 아이콘
      binding.imgMSettingbtn.setOnClickListener {
        val intent = Intent(this@MainActivity, SettingActivity::class.java)
        startActivity(intent)
        finish()
      }

      when (item.itemId) {
        R.id.M_tab1 -> {
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            HomeMainFragment()
          ).commit()


          binding.tvMToptitle.text = "시니모"
          binding.imgMAlertbtn.visibility = View.VISIBLE
          binding.imgMSettingbtn.visibility = View.INVISIBLE
          binding.imgMBackbtnToFrag1.visibility = View.INVISIBLE
        }

        R.id.M_tab2 -> {
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            MymeetingFragment()
          ).commit()

          // 여기서 TextView의 텍스트 변경과 ImageView 숨기기
          binding.tvMToptitle.text = "내 모임"
          binding.imgMAlertbtn.visibility = View.INVISIBLE
          binding.imgMSettingbtn.visibility = View.INVISIBLE
          binding.imgMBackbtnToFrag1.visibility = View.INVISIBLE

        }

        R.id.M_tab3 -> {
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            ChatMainFragment()
          ).commit()

          binding.tvMToptitle.text = "채팅"
          binding.imgMAlertbtn.visibility = View.INVISIBLE
          binding.imgMSettingbtn.visibility = View.INVISIBLE
          binding.imgMBackbtnToFrag1.visibility = View.INVISIBLE


        }


        R.id.M_tab4 -> {
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
    if (selectedTab != null) {
      when (selectedTab) {
        "M_tab1" -> binding.bnvMain.selectedItemId = R.id.M_tab1
        "M_tab2" -> binding.bnvMain.selectedItemId = R.id.M_tab2
        "M_tab3" -> binding.bnvMain.selectedItemId = R.id.M_tab3
        "M_tab4" -> binding.bnvMain.selectedItemId = R.id.M_tab4
      }
    }
  }

  // ChatMainFragment 에서 ChatFragment로 이동시키게 하는 함수 만들기
  // 내 모임리스트 -> 내 모임 방
  // tab3 에서 fl화면 전환
  fun navigateToChatFragment(chatListVO: ChatListVO) {
    // BottomNavigation 숨기기
    binding.bnvMain.visibility = View.GONE

    supportFragmentManager.beginTransaction().replace(
      R.id.fl,
      ChatFragment.newInstance(chatListVO)
    ).commit()

    // ChatFragment에 있을 때 뒤로가기 버튼 보이게 설정
    binding.imgMBackbtnToFrag1.visibility = View.VISIBLE
  }

  // ChatFragment에서 ChatMainFragment로 이동하는 함수
  fun navigateBackToChatMainFragment() {
    // BottomNavigation 표시
    binding.bnvMain.visibility = View.VISIBLE

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