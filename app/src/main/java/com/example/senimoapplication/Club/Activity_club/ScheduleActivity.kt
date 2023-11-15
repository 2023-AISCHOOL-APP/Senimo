package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.senimoapplication.Club.VO.ScheduleMemberVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Club.adapter.ScheduleMemberAdapter
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.Common.showBoardDialogBox
import com.example.senimoapplication.server.Retrofit.ApiService
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.senimoapplication.databinding.ActivityScheduleBinding

class ScheduleActivity : AppCompatActivity() {
    private lateinit var scheduleMemberAdapter: ScheduleMemberAdapter
    val ScheduleGroupList : ArrayList<ScheduleVO> = ArrayList()
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


        fetchSchedule()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }



    private fun fetchSchedule() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.70.234:3000") // 실제 서버 주소
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
        val retrofit = Server().retrofit

        // 서버에 요청을 보낼 '전화기'를 만들어요.
        val service = retrofit.create(ApiService::class.java)
        val sche_code = "sche_code 1" // 예시 ID
        // '전화'를 걸어요. 서버에 데이터를 달라고 요청해요.
        service.getScheIntro(sche_code).enqueue(object : Callback<ScheduleVO> {
            // 서버에서 답이 오면 이 부분이 실행돼요.
            override fun onResponse(call: Call<ScheduleVO>, response: Response<ScheduleVO>) {
                Log.d("Schedule", response.toString())
                // 서버 응답이 null인지 확인합니다.
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("Schedule", response.body().toString())
                        response.body()?.let { schedules ->
                            // null이 아니면 기존 목록을 지우고 새 데이터로 채웁니다.
                            findViewById<TextView>(R.id.tv_C_S_Time).text = formatDate(schedules.scheduleDate)
                            findViewById<TextView>(R.id.tvClubName2).text = schedules.club_code
                            findViewById<TextView>(R.id.tv_C_ScheduleName3).text = schedules.scheduleName
                            findViewById<TextView>(R.id.tv_C_Schedule_Intro).text = schedules.scheduleIntro
                            findViewById<TextView>(R.id.tv_C_S_Loca).text = schedules.scheduleLoca
                            findViewById<TextView>(R.id.tv_C_S_Fee).text = "${schedules.scheduleFee} 원"
                            findViewById<TextView>(R.id.tv_C_S_attendance).text = "${schedules.attendance} /"
                            findViewById<TextView>(R.id.tv_C_S_all).text = "${schedules.allMembers}명"

                        }
                    } else {
                        Log.e("ScheduleActivity", "서버 에러: ${response.code()}")
                    }

                }

            }
            override fun onFailure(call: Call<ScheduleVO>, t: Throwable) {
                Log.e("ScheduleActivity", "네트워크 요청 실패: ${t.localizedMessage}")
            }
            })
        }

    }
