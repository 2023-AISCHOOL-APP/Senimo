package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.VO.CancelJoinScheResVO
import com.example.senimoapplication.Club.VO.JoinScheResVO
import com.example.senimoapplication.Club.VO.ScheduleMemberVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Club.adapter.ScheduleMemberAdapter
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.Common.showActivityDialogBox
import com.example.senimoapplication.MainPage.VO_main.MyScheduleVO
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.senimoapplication.databinding.ActivityScheduleBinding
import com.example.senimoapplication.server.Token.UserData

class ScheduleActivity : AppCompatActivity() {
  private lateinit var scheduleMemberAdapter: ScheduleMemberAdapter
  val ScheduleGroupList: ArrayList<ScheduleVO> = ArrayList()
  lateinit var binding: ActivityScheduleBinding
  var isJoined = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val scheduleData = intent.getParcelableExtra<MyScheduleVO>("scheduleData")
        if (scheduleData != null) {
            Log.d("ScheduleActivity", "받아온 일정 데이터 : $scheduleData")
        } else {
            Log.d("ScheduleActivity", "일정 데이터 못 받음")
        }

    binding = ActivityScheduleBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val color = ContextCompat.getColor(this, R.color.white)
    binding.icMore.setImageTintList(ColorStateList.valueOf(color))

    val memberList: ArrayList<ScheduleMemberVO> = ArrayList()

    val adapter = ScheduleMemberAdapter(applicationContext, R.layout.club_member_list, memberList)
    binding.rvAttendance.adapter = adapter
    binding.rvAttendance.layoutManager = LinearLayoutManager(this)


    //가데이터
    val schedule = ScheduleVO(
      "축구보자축구",
      "대한민국싱가포르축구볼사람구함",
      "3층에 모여서 축구봐요. 생각보다 잘할거에여.",
      "2023-11-22T12:00:08.123Z",
      3000,
      "광주 동구 대성학원 3층 6강의실",
      10,
      4,
      "모집중",
      "R.drawable.img_sample2"
    )


    //binding.tvCScheduleName3.text = schedule.scheduleName
    //binding.imgCSchedule.setImageResource(schedule.scheImg)
    //binding.tvCScheduleIntro.text = schedule.scheduleIntro

    //binding.tvCSTime.text = formatDate(schedule.scheduleDate)
    //binding.tvCSLoca.text = schedule.scheduleLoca
    //binding.tvCSFee.text = "${schedule.scheduleFee}원"
    //binding.tvCSAttendance.text = "${schedule.attendance}/${schedule.allMembers}명"


    // 회원 목록 가데이터
    memberList.add(ScheduleMemberVO("양희준", 1, R.drawable.img_sample))
    memberList.add(ScheduleMemberVO("최효정", 2, R.drawable.img_sample))
    memberList.add(ScheduleMemberVO("국지호", 3, R.drawable.img_sample))
    memberList.add(ScheduleMemberVO("김도운", 3, R.drawable.img_sample))

    // 일정 참가하기 버튼
    binding.btnJoinSchedule.setOnClickListener {
      val userId = UserData.userId.toString()
      val scheCode = "test1"

      if (isJoined) {
        joinSche(userId, scheCode)
        isJoined = false
      } else {
        cancelJoinSche(userId, scheCode)
        isJoined = true
      }

      Log.d("isJoinedState", isJoined.toString())
    }

    // 뒤로가기 버튼
    binding.icBack.setOnClickListener {
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
            showActivityDialogBox(this, "게시물을 삭제하시겠어요?", "삭제하기", "게시물이 삭제되었습니다.")
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

    // 서버에 요청을 보낼 '전화기'를 만들어요.
    val service = Server(this).service
    val sche_code = "test1" // 예시 ID
    // '전화'를 걸어요. 서버에 데이터를 달라고 요청해요.
    service.getScheIntro(sche_code).enqueue(object : Callback<ScheduleVO> {
      // 서버에서 답이 오면 이 부분이 실행돼요.
      override fun onResponse(call: Call<ScheduleVO>, response: Response<ScheduleVO>) {
        Log.d("ScheduleActivity", response.toString())
        // 서버 응답이 null인지 확인합니다.
        if (response.isSuccessful) {
          if (response.body() != null) {
            Log.d("ScheduleActivity", response.body().toString())
            response.body()?.let { schedules ->
              // null이 아니면 기존 목록을 지우고 새 데이터로 채웁니다.
              findViewById<TextView>(R.id.tv_C_S_Time).text = formatDate(schedules.scheDate)
              findViewById<TextView>(R.id.tvClubName2).text = schedules.clubName
              findViewById<TextView>(R.id.tv_C_ScheduleName3).text = schedules.scheTitle
              findViewById<TextView>(R.id.tv_C_Schedule_Intro).text = schedules.scheContent
              findViewById<TextView>(R.id.tv_C_S_Loca).text = schedules.scheLoca
              findViewById<TextView>(R.id.tv_C_S_Fee).text = "${schedules.scheFee} 원"
              findViewById<TextView>(R.id.tv_C_S_attendance).text =
                "${schedules.attendUserCnt}/${schedules.maxNum}명"
              // binding.tvCScheduleIntro.text = schedules.scheduleName
              //binding.imgCSchedule.setImageResource(schedules.imageUri)
              Glide.with(this@ScheduleActivity)
                .load(schedules.scheImg) // MeetingVO 객체의 imageUri
                .placeholder(R.drawable.loading) // 로딩 중 표시될 이미지
                .error(R.drawable.golf_img) // 로딩 실패 시 표시될 이미지
                .into(binding.imgCSchedule) // 이미지를 표시할 ImageView
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

  fun joinSche(userId: String, scheCode: String) {
    val service = Server(this).service
    val call = service.joinSche(userId, scheCode)

    call.enqueue(object : Callback<JoinScheResVO> {
      override fun onResponse(
        call: Call<JoinScheResVO>,
        response: Response<JoinScheResVO>
      ) {
        if (response.isSuccessful) {
          val joinScheRes = response.body()
          if (joinScheRes != null && joinScheRes.rows == "success") {
              // 버튼이 "일정 참가하기" 상태일 때
              binding.btnJoinSchedule.text = "일정 참가 취소하기"
              binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape) // 선택된 배경으로 변경
              binding.btnJoinSchedule.setTextColor(ContextCompat.getColor(this@ScheduleActivity, R.color.main)) // 메인 텍스트 색상으로 변경
              Toast.makeText(this@ScheduleActivity, "참가 신청 완료", Toast.LENGTH_SHORT).show()
          } else {
            Log.d("joinSche", "not success")
          }
        }
      }

      override fun onFailure(call: Call<JoinScheResVO>, t: Throwable) {
        Log.e("ScheduleActivity", "joinSche 네트워크 요청 실패", t)
      }
    })
  }

  fun cancelJoinSche(userId: String, scheCode: String) {
    val service = Server(this).service
    val call = service.cancelJoinSche(userId, scheCode)

    call.enqueue(object : Callback<CancelJoinScheResVO> {
      override fun onResponse(
        call: Call<CancelJoinScheResVO>,
        response: Response<CancelJoinScheResVO>
      ) {
        if (response.isSuccessful) {
          val cancelJoinScheRes = response.body()
          if (cancelJoinScheRes != null && cancelJoinScheRes.rows == "success") {
            // 데이터 삭제 요청이 성공한 경우 수행할 작업
            binding.btnJoinSchedule.text = "일정 참가하기"
            binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape_main) // 디폴트 배경으로 변경
            binding.btnJoinSchedule.setTextColor(ContextCompat.getColor(this@ScheduleActivity, R.color.white)) // 디폴트 텍스트 색상으로 변경
            Toast.makeText(this@ScheduleActivity, "일정 참가 취소가 완료되었습니다.", Toast.LENGTH_SHORT).show()
          } else {
            Log.d("cancelJoinSche", "not success")
          }
        }
      }
      override fun onFailure(call: Call<CancelJoinScheResVO>, t: Throwable) {
        Log.e("ScheduleActivity", "cancelJoinSche 네트워크 요청 실패", t)
      }
    })
  }
}
