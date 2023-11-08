package com.example.senimoapplication

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.senimoapplication.databinding.ActivityMainBinding
import com.example.senimoapplication.fragment_main.ChatMainFragment
import com.example.senimoapplication.fragment_main.HomeMainFragment
import com.example.senimoapplication.fragment_main.MymeetingFragment
import com.example.senimoapplication.fragment_main.MypageFragment

class MainActivity : AppCompatActivity() {

  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // 바인딩 객체 획득
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    // 액티비티 화면 출력
    setContentView(view)

    // 아이콘 초기 설정 : M_tab1을 ic_home_color로 설정
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


      when(item.itemId){
        R.id.M_tab1 ->{
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            HomeMainFragment()
          ).commit()


          binding.tvMToptitle.text = "시니모"
          binding.ImgMAlertbtn.visibility = View.VISIBLE
          binding.ImgMSettingbtn.visibility = View.INVISIBLE
          binding.ImgMBackbtnToFrag1.visibility = View.INVISIBLE
        }
        R.id.M_tab2 ->{
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            MymeetingFragment()
          ).commit()

          // 여기서 TextView의 텍스트 변경과 ImageView 숨기기
          binding.tvMToptitle.text = "내 모임"
          binding.ImgMAlertbtn.visibility = View.INVISIBLE
          binding.ImgMSettingbtn.visibility = View.INVISIBLE
          binding.ImgMBackbtnToFrag1.visibility = View.INVISIBLE

        }
        R.id.M_tab3 ->{
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            ChatMainFragment()
          ).commit()

          binding.tvMToptitle.text = "채팅"
          binding.ImgMAlertbtn.visibility = View.INVISIBLE
          binding.ImgMSettingbtn.visibility = View.INVISIBLE
          binding.ImgMBackbtnToFrag1.visibility = View.INVISIBLE

        }
        R.id.M_tab4 ->{
          supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            MypageFragment()
          ).commit()

          binding.tvMToptitle.text = "내 정보"
          binding.ImgMAlertbtn.visibility = View.INVISIBLE
          binding.ImgMSettingbtn.visibility = View.VISIBLE
          binding.ImgMBackbtnToFrag1.visibility = View.INVISIBLE

        }
      }

      true
    }

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



  }