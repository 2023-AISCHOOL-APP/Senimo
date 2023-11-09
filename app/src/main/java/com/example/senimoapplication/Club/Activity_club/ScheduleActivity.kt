package com.example.senimoapplication.Club.Activity_club

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.VO.ScheduleMemberVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Club.adapter.ScheduleMemberAdapter
import com.example.senimoapplication.Common.formatDate

class ScheduleActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val icBack = findViewById<ImageView>(R.id.icBack2)
        val icMore = findViewById<ImageView>(R.id.icMore)
        val color = ContextCompat.getColor(this, R.color.white)
        icMore.setImageTintList(ColorStateList.valueOf(color))

        val memberList : ArrayList<ScheduleMemberVO> = ArrayList()
        val rvAttendance : RecyclerView = findViewById(R.id.rvAttendance)
        val adapter = ScheduleMemberAdapter(applicationContext, R.layout.schedule_member_list, memberList)
        rvAttendance.adapter = adapter
        rvAttendance.layoutManager = GridLayoutManager(this,3)

        val btnJoinSchedule = findViewById<Button>(R.id.btnJoinSchedule)
        val tvScheduleIntro = findViewById<TextView>(R.id.tv_C_Schedule_Intro)
        val tvScheduleName = findViewById<TextView>(R.id.tv_C_ScheduleName3)
        val tvDate = findViewById<TextView>(R.id.tv_C_S_Time)

        //가데이터

        tvDate.text = formatDate("2023-11-18 19:00")
        tvScheduleName.text = "수사모(수영을 사랑하는 사람들의 모임) 정모일정안내"
        tvScheduleIntro.text = """수영 정모에 처음 참석하시는 분들께.. 
            |수사모는 수영을 좋아하고 정보를 교류하는 모임이에요!! 
            |
            |참여자 누구나 환영하고 모임 20분 전까지 오셔서 회원들과 인사 나누시고 수영장으로 들어갈게요~ :) 
            |
            |일정에 관한 문의는 채팅방을 이용해주시기 바래요!""".trimMargin()



        // 회원 목록 가데이터
        memberList.add(ScheduleMemberVO("양희준", "모임장", R.drawable.img_sample))
        memberList.add(ScheduleMemberVO("최효정", "운영진", R.drawable.img_sample))
        memberList.add(ScheduleMemberVO("국지호", "", R.drawable.img_sample))
        memberList.add(ScheduleMemberVO("김도운", "", R.drawable.img_sample))


        // 뒤로가기 버튼
        icBack.setOnClickListener {
            onBackPressed()
        }

        // 앱바 - 게시물 관리 기능 추가
        icMore.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.option_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_option1 -> {
                        // Option 1을 선택한 경우의 동작
                        // 여기에 원하는 동작을 추가하세요.
                        true
                    }

                    R.id.menu_option2 -> {
                        // Option 2을 선택한 경우의 동작
                        // 여기에 원하는 동작을 추가하세요.
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }



    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}