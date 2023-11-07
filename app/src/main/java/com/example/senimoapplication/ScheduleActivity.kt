package com.example.senimoapplication

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.VO.MemberVO
import com.example.senimoapplication.VO.ScheduleMemberVO

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