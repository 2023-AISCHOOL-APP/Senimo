package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
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
import com.example.senimoapplication.Common.showBoardDialogBox
import com.example.senimoapplication.databinding.ActivityPostBinding
import com.example.senimoapplication.databinding.ActivityScheduleBinding


class ScheduleActivity : AppCompatActivity() {

    lateinit var binding : ActivityScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val color = ContextCompat.getColor(this, R.color.white)
        binding.icMore.setImageTintList(ColorStateList.valueOf(color))

        val memberList : ArrayList<ScheduleMemberVO> = ArrayList()

        val adapter = ScheduleMemberAdapter(applicationContext, R.layout.schedule_member_list, memberList)
        binding.rvAttendance.adapter = adapter
        binding.rvAttendance.layoutManager = GridLayoutManager(this,3)


        //가데이터

        binding.tvCSTime.text = formatDate("2023-11-18 19:00")
        binding.tvCScheduleName3.text = "수사모(수영을 사랑하는 사람들의 모임) 정모일정안내"




        // 회원 목록 가데이터
        memberList.add(ScheduleMemberVO("양희준", "1", R.drawable.img_sample))
        memberList.add(ScheduleMemberVO("최효정", "2", R.drawable.img_sample))
        memberList.add(ScheduleMemberVO("국지호", "3", R.drawable.img_sample))
        memberList.add(ScheduleMemberVO("김도운", "3", R.drawable.img_sample))


        // 뒤로가기 버튼
        binding.icBack2.setOnClickListener {
            onBackPressed()
        }

        // 앱바 - 게시물 관리 기능 추가
        binding.icMore.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            val menuInflater = popupMenu.menuInflater

            menuInflater.inflate(R.menu.option_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_option1 -> {
                        // 게시물 수정
                        val intent = Intent(this, MakeScheduleActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.menu_option2 -> {
                        // 게시물 삭제
                        showBoardDialogBox(this,"게시물을 삭제하시겠어요?", "삭제하기", "게시물이 삭제되었습니다.")
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