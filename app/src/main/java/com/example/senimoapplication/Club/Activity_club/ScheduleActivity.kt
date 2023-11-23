package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.VO.AllScheduleMemberResVO
import android.widget.Toast
import com.example.senimoapplication.Club.VO.CancelJoinScheResVO
import com.example.senimoapplication.Club.VO.JoinScheResVO
import com.example.senimoapplication.Club.VO.ScheduleMemberVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Club.adapter.ScheduleMemberAdapter
import com.example.senimoapplication.Club.fragment.HomeFragment
import com.example.senimoapplication.Club.fragment.MemberManager
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.Common.showActivityDialogBox
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.databinding.ActivityScheduleBinding
import com.example.senimoapplication.MainPage.VO_main.MyScheduleVO
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.senimoapplication.server.Token.UserData

class ScheduleActivity : AppCompatActivity() {
    private lateinit var scheduleMemberAdapter: ScheduleMemberAdapter
    lateinit var binding: ActivityScheduleBinding
    var clubName: String? = null
    var scheCode : String? = null
    var clickedSchedule : ScheduleVO? = null
    var joinedMemberList : List<String>? = ArrayList()
    val userId = UserData.userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        binding = ActivityScheduleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val scheduleData = intent.getParcelableExtra<MyScheduleVO>("scheduleData")
        if (scheduleData != null) {
            Log.d("ScheduleActivity", "받아온 일정 데이터 : $scheduleData")
        } else {
            Log.d("ScheduleActivity", "일정 데이터 못 받음")
        }

        // Intent 데이터 관리
        clickedSchedule = intent.getParcelableExtra("ScheduleInfo")
        clubName = intent.getStringExtra("clubName")
        scheCode = intent.getStringExtra("scheCode")
        Log.d("ScheduleActivity", "${clubName}")
        Log.d("ScheduleActivity", "${clickedSchedule}")

        // view 관리
        binding.tvClubName2.text = clubName
        val color = ContextCompat.getColor(this, R.color.white)
        binding.icMore.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
        binding.tvScheduleName.text = clickedSchedule?.scheTitle
        binding.tvScheduleIntro.text = clickedSchedule?.scheContent
        binding.tvScheduleTime.text = formatDate("${clickedSchedule?.scheDate}")
        binding.tvScheduleLoca.text = clickedSchedule?.scheLoca
        binding.tvScheduleFee.text = "${clickedSchedule?.scheFee}원"
        binding.tvScheduleMember.text =
            "${clickedSchedule?.joinedMembers}/${clickedSchedule?.maxNum}명"
        Glide.with(this)
            .load(clickedSchedule?.scheImg)
            .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
            .error(R.drawable.basic_club) // 로딩 실패 시 표시될 이미지
            .into(binding.imgCSchedule)


        // 일정 참여 멤버 목록 가져오기
        val server = Server(this)
        val memberManager = MemberManager(server)
        scheCode?.let { code ->
            MemberManager(server).getScheduleMembers(code, object : Callback<AllScheduleMemberResVO> {
                override fun onResponse(call: Call<AllScheduleMemberResVO>, response: Response<AllScheduleMemberResVO>) {
                    Log.d("getScheduleMember", "통신시작")
                    if (response.isSuccessful) {
                        val scheduleMemberList: List<ScheduleMemberVO>? = response.body()?.data
                        Log.d("getScheduleMember", "${scheduleMemberList}")
                        joinedMemberList = scheduleMemberList?.map { it.userId }
                        Log.d("joinlist", "일정 참여자${joinedMemberList}, 현재 유저아이디 ${userId}")
                        if (scheduleMemberList != null) {
                            val sm_adapter = ScheduleMemberAdapter(this@ScheduleActivity, R.layout.club_member_list, ArrayList(scheduleMemberList))
                                binding.rvAttendance.adapter = sm_adapter
                                binding.rvAttendance.layoutManager = LinearLayoutManager(view.context)
                                binding.rvAttendance.addOnItemTouchListener(RecyclerItemClickListener(this@ScheduleActivity, binding.rvAttendance,
                                        object : RecyclerItemClickListener.OnItemClickListener {
                                            override fun onItemClick(view: View, position: Int) {
                                                val clickedSchedule = scheduleMemberList[position]
                                                // 프로필 페이지로 이동
                                                val intent = Intent(this@ScheduleActivity, MainActivity::class.java)
                                                intent.putExtra("selected_tab", "M_tab4")
                                                intent.putExtra("selected_user", "${clickedSchedule.userId}")
                                                startActivity(intent)
                                            }
                                        })
                                )
                            }
                        } else {
                            Log.d("getScheduleMember", "일정 멤버 리스트 가져오기 실패")
                        }
                    }
                override fun onFailure(call: Call<AllScheduleMemberResVO>, t: Throwable) {
                    Log.d("getclickedSchedule", "스택 트레이스: ", t)
                }
            })
        }

        // 일정 참가하기 버튼
        binding.btnJoinSchedule.setOnClickListener {
            val userId = UserData.userId.toString()

            if(joinedMemberList?.contains(userId) == true){
                Log.d("joinlist2","중복감지")
                binding.btnJoinSchedule.text = "일정 참가 취소하기"
                binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape_main) // 디폴트 배경으로 변경
                binding.btnJoinSchedule.setTextColor(ContextCompat.getColor(this@ScheduleActivity, R.color.white))
                scheCode?.let { it1 -> cancelJoinSche(userId, it1) }

            } else {
                Log.d("joinlist3","중복감지안됨")

                // 버튼 view 변경하기
                binding.btnJoinSchedule.text = "일정 참가하기"
                binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape) // 선택된 배경으로 변경
                binding.btnJoinSchedule.setTextColor(
                    ContextCompat.getColor(this@ScheduleActivity, R.color.main)) // 메인 텍스트 색상으로 변경
                scheCode?.let { it1 -> joinSche(userId, it1) }

            }
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun joinSche(userId: String, scheCode: String) {
        val service = Server(this).service
        val call = service.joinSche(userId, scheCode)

        call.enqueue(object : Callback<JoinScheResVO> {
            override fun onResponse(call: Call<JoinScheResVO>, response: Response<JoinScheResVO>) {
                if (response.isSuccessful) {
                    val joinScheRes = response.body()
                    if (joinScheRes != null && joinScheRes.rows == "success") {
                        // 버튼이 "일정 참가하기" 상태일 때
                        Toast.makeText(this@ScheduleActivity, "참가 신청 완료", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("joinSche", "일정 참가하기 실패")
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
            override fun onResponse(call: Call<CancelJoinScheResVO>, response: Response<CancelJoinScheResVO>) {
                if (response.isSuccessful) {
                    val cancelJoinScheRes = response.body()
                    if (cancelJoinScheRes != null && cancelJoinScheRes.rows == "success") {
                        Toast.makeText(this@ScheduleActivity, "일정 참가 취소가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("cancelJoinSche", "일정 참가 취소하기 실패")
                    }
                }
            }

            override fun onFailure(call: Call<CancelJoinScheResVO>, t: Throwable) {
                Log.e("ScheduleActivity", "cancelJoinSche 네트워크 요청 실패", t)
            }
        })
    }
}

