package com.example.senimoapplication.Club.Activity_club

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.VO.AllScheduleMemberResVO
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.Club.VO.CancelJoinScheResVO
import com.example.senimoapplication.Club.VO.DeleteScheResVO
import com.example.senimoapplication.Club.VO.JoinScheResVO
import com.example.senimoapplication.Club.VO.RoleResVO
import com.example.senimoapplication.Club.VO.ScheduleMemberVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Club.adapter.ScheduleMemberAdapter
import com.example.senimoapplication.Club.fragment.MemberManager
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.Common.showActivityDialogBox
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.databinding.ActivityScheduleBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleActivity : AppCompatActivity() {
    private lateinit var scheduleMemberAdapter: ScheduleMemberAdapter
    lateinit var binding: ActivityScheduleBinding
    var clubName: String? = null
    var scheCode: String? = null
    var clickedSchedule: ScheduleVO? = null
    var joinedMemberList: List<String>? = ArrayList()
    var userId: String? = null
    var staffList: List<String>? = emptyList()
    var clickedMeeting : MeetingVO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        binding = ActivityScheduleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val UserData = PreferenceManager.getUser(this)
        userId = UserData?.user_id
        Log.d("ScheduleActivity", "유저 확인$userId")

        // Intent 데이터 관리
        clickedSchedule = intent.getParcelableExtra("ScheduleInfo")
        clickedMeeting = intent.getParcelableExtra("clubInfo")
        clubName = intent.getStringExtra("clubName")
        staffList = intent.getStringArrayListExtra("staffList")
        scheCode = clickedSchedule?.scheCode


        // MeetingVO 가져오기
        if(clickedMeeting == null){
            clickedSchedule?.scheCode?.let {
                Log.d("ScheduleActivity","scheduleVO받는값 확인${clickedSchedule?.scheCode}")
                getMeeting(it)
            }
        }

        // view 관리
        Log.d("clubName", "${clubName}, ${clubName?.length}")
        binding.tvClubName2.text = clubName?.let {
            if (it.length > 13) it.substring(0, 13) + "..." else it
        } ?: "모임"

        val color = ContextCompat.getColor(this, R.color.white)
        binding.icMore.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)


        displayScheduleInfo(clickedSchedule)
        // 일정 참여 멤버 목록 가져오기
        getUserRole(clickedSchedule?.clubCode, userId)
        getScheduleMembers()


        // 기본 버튼 동작
        binding.btnJoinSchedule.text = "일정 참가하기"
        binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape_main)
        binding.btnJoinSchedule.setTextColor(ContextCompat.getColor(this@ScheduleActivity, R.color.white))
        binding.btnJoinSchedule.setOnClickListener {
            joinSche(userId, scheCode)
            Log.d("joinSchedule", "참가하기 버튼 $userId, $scheCode")
        }

        // 뒤로가기 아이콘
        binding.icBack.setOnClickListener {
            returnToClubActivity()
        }

        // 디바이스 뒤로가기 버튼
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                returnToClubActivity()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)

    }

    private fun displayScheduleInfo(scheduleInfo : ScheduleVO?){
        binding.tvClubName2.text = clubName?.let {
            if (it.length > 13) it.substring(0, 13) + "..." else it
        } ?: "모임"
        binding.tvScheduleName.text = scheduleInfo?.scheTitle
        binding.tvScheduleIntro.text = scheduleInfo?.scheContent
        binding.tvScheduleTime.text = formatDate("${scheduleInfo?.scheDate}")
        binding.tvScheduleLoca.text = scheduleInfo?.scheLoca
        binding.tvScheduleFee.text = "${scheduleInfo?.scheFee}원"
        binding.tvScheduleMember.text = "${scheduleInfo?.joinedMembers}/${scheduleInfo?.maxNum}명"
        Log.d("cnt","${scheduleInfo?.joinedMembers}")
        Glide.with(this)
            .load(scheduleInfo?.scheImg)
            .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
            .error(R.drawable.basic_club) // 로딩 실패 시 표시될 이미지
            .into(binding.imgCSchedule)
    }

    // 일정 참석 회원 목록 가져오기 (참가 상태에 따라 회원목록, 참가자 수 변경)
    private fun getScheduleMembers() {
        val server = Server(this)
        val memberManager = MemberManager(server)
        scheCode?.let { code ->
            MemberManager(server).getScheduleMembers(
                code,
                object : Callback<AllScheduleMemberResVO> {
                    override fun onResponse(
                        call: Call<AllScheduleMemberResVO>,
                        response: Response<AllScheduleMemberResVO>
                    ) {
                        Log.d("getScheduleMember", "통신시작")
                        if (response.isSuccessful) {
                            val scheduleMemberList: List<ScheduleMemberVO>? = response.body()?.data
                            updateUIWithScheduleMembers(scheduleMemberList)
                            Log.d("getScheduleMember", "${scheduleMemberList}")
                            joinedMemberList = scheduleMemberList?.map { it.userId }
                            Log.d("joinlist", "1.일정 참여자${joinedMemberList}, 현재 유저아이디 ${userId}")
                            if (scheduleMemberList != null) {
                                val sm_adapter = ScheduleMemberAdapter(this@ScheduleActivity,
                                    R.layout.club_member_list,ArrayList(scheduleMemberList))

                                // 일정 참가하기 버튼 기능
                                Log.d("joinlist", "데이터 확인! ${joinedMemberList}")
                                userId?.let { nonNullUserId ->
                                    if (joinedMemberList?.contains(nonNullUserId) == true) {
                                        binding.btnJoinSchedule.text = "일정 참가 취소하기"
                                        binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape)
                                        binding.btnJoinSchedule.setTextColor(ContextCompat.getColor(this@ScheduleActivity, R.color.main))
                                        binding.btnJoinSchedule.setOnClickListener {
                                            cancelJoinSche(nonNullUserId, scheCode)
                                            Log.d("joinSchedule", "참가취소 버튼 $nonNullUserId, $scheCode")
                                        }
                                        sm_adapter.notifyDataSetChanged()
                                    } else {
                                        binding.btnJoinSchedule.text = "일정 참가하기"
                                        binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape_main)
                                        binding.btnJoinSchedule.setTextColor(ContextCompat.getColor(this@ScheduleActivity, R.color.white))
                                        binding.btnJoinSchedule.setOnClickListener {
                                            joinSche(nonNullUserId, scheCode)
                                            Log.d("joinSchedule", "참가하기 버튼 $nonNullUserId, $scheCode")
                                        }
                                        sm_adapter.notifyDataSetChanged()
                                    }
                                }

                                binding.rvAttendance.adapter = sm_adapter
                                binding.rvAttendance.layoutManager =
                                    LinearLayoutManager(this@ScheduleActivity)
                                binding.rvAttendance.addOnItemTouchListener(
                                    RecyclerItemClickListener(this@ScheduleActivity,
                                        binding.rvAttendance,
                                        object : RecyclerItemClickListener.OnItemClickListener {
                                            override fun onItemClick(view: View, position: Int) {
                                                val clickedSchedule = scheduleMemberList[position]
                                                // 프로필 페이지로 이동
                                                val intent = Intent(this@ScheduleActivity, userProfileActivity::class.java)
                                                intent.putExtra("selected_user", "${clickedSchedule.userId}")
                                                Log.d("userProfile","일정 멤버 리스트 보내는 값 ${clickedSchedule.userId}")
                                                startActivity(intent)
                                                finish()
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
    }
    private fun updateUIWithScheduleMembers(scheduleMemberList: List<ScheduleMemberVO>?) {
        Log.d("getScheduleMember", "${scheduleMemberList}")
        joinedMemberList = scheduleMemberList?.map { it.userId }
        Log.d("joinlist", "1.일정 참여자${joinedMemberList}, 현재 유저아이디 ${userId}")
        binding.tvScheduleMember.text = "${joinedMemberList?.size}/${clickedSchedule?.maxNum}명"
    }

    // 일정 참가 하기
    fun joinSche(userId: String?, scheCode: String?) {
        val service = Server(this).service
        val call = service.joinSche(userId, scheCode)

        call.enqueue(object : Callback<JoinScheResVO> {
            override fun onResponse(call: Call<JoinScheResVO>, response: Response<JoinScheResVO>) {
                if (response.isSuccessful) {
                    val joinScheRes = response.body()
                    if (joinScheRes != null && joinScheRes.rows == "success") {
                        // 버튼이 "일정 참가하기" 상태일 때
                        getScheduleMembers()
                        binding.tvScheduleMember.text = "${joinedMemberList?.size}/${clickedSchedule?.maxNum}명"
                        Toast.makeText(this@ScheduleActivity, "참가 신청 완료", Toast.LENGTH_SHORT).show()
                        binding.btnJoinSchedule.text = "일정 참가 취소하기"
                        binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape)
                        binding.btnJoinSchedule.setTextColor(ContextCompat.getColor(this@ScheduleActivity, R.color.main))
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

    // 일정 참가 취소하기
    fun cancelJoinSche(userId: String?, scheCode: String?) {
        val service = Server(this).service
        val call = service.cancelJoinSche(userId, scheCode)

        call.enqueue(object : Callback<CancelJoinScheResVO> {
            override fun onResponse(call: Call<CancelJoinScheResVO>, response: Response<CancelJoinScheResVO>) {
                if (response.isSuccessful) {
                    val cancelJoinScheRes = response.body()
                    if (cancelJoinScheRes != null && cancelJoinScheRes.rows == "success") {
                        getScheduleMembers()
                        binding.tvScheduleMember.text = "${joinedMemberList?.size}/${clickedSchedule?.maxNum}명"
                        Toast.makeText(this@ScheduleActivity, "일정 참가 취소가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        binding.btnJoinSchedule.text = "일정 참가하기"
                        binding.btnJoinSchedule.setBackgroundResource(R.drawable.button_shape_main)
                        binding.btnJoinSchedule.setTextColor(ContextCompat.getColor(this@ScheduleActivity, R.color.white))
                    }
                }
            }

            override fun onFailure(call: Call<CancelJoinScheResVO>, t: Throwable) {
                Log.e("ScheduleActivity", "cancelJoinSche 네트워크 요청 실패", t)
            }
        })
    }

    // 일정 삭제하기
    fun deleteSche(scheCode: String?) {
        val service = Server(this).service
        val call = service.deleteSche(scheCode)

        call.enqueue(object : Callback<DeleteScheResVO> {
            override fun onResponse(
                call: Call<DeleteScheResVO>,
                response: Response<DeleteScheResVO>
            ) {
                if (response.isSuccessful) {
                    val deleteScheRes = response.body()
                    if (deleteScheRes != null && deleteScheRes.rows == "success") {
                        Log.d("일정 삭제", "${scheCode} 삭제 성공")
                        val intent = Intent(this@ScheduleActivity, ClubActivity::class.java)
                        intent.putExtra("clickedMeeting", clickedMeeting)
                        startActivity(intent)
                    } else {
                        Log.d("일정 삭제", "${scheCode} 삭제 실패")
                    }
                }
            }

            override fun onFailure(call: Call<DeleteScheResVO>, t: Throwable) {
                Log.e("일정 삭제", "일정 삭제 네트워크 요청 실패", t)
            }

        })
    }

    // 모임 정보 가져오기
    fun getMeeting(scheCode: String) {
        val service = Server(this).service
        val call = service.getMeeting(scheCode)

        call.enqueue(object : Callback<MeetingVO> {
            override fun onResponse(call: Call<MeetingVO>, response: Response<MeetingVO>) {
                if (response.isSuccessful) {
                    val meetingVO = response.body()
                    Log.d("ScheduleActivity", "통신성공: ${meetingVO}")
                    clickedMeeting = meetingVO

                    binding.icBack.setOnClickListener {
                        val intent = Intent(this@ScheduleActivity, ClubActivity::class.java)
                        intent.putExtra("clickedMeeting", clickedMeeting)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    // 요청은 성공했지만 서버에서 오류 응답을 받았을 때의 처리
                    Log.d("ScheduleActivity", "Response not successful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MeetingVO>, t: Throwable) {
                // 네트워크 요청 실패 시의 처리
                Log.e("ScheduleActivity", "네트워크 요청 실패", t)
            }
        })
    }

    // 뒤로가기 버튼 함수
    private fun returnToClubActivity() {
        val returnIntent = Intent(this@ScheduleActivity, ClubActivity::class.java)
        clickedSchedule?.joinedMembers = joinedMemberList?.size ?: 0
        returnIntent.putExtra("ScheduleInfo", clickedSchedule)
        returnIntent.putExtra("clickedMeeting", clickedMeeting)
        setResult(Activity.RESULT_OK, returnIntent)
        Log.d("ScheduleInfo","보내기:${clickedSchedule}")
        Log.d("ScheduleActivity", "Finishing ScheduleActivity")
        startActivity(returnIntent)
        finish()
    }

    private fun getUserRole(clubCode: String?, userId: String?) {
        val service = Server(this).service
        val call = service.getUserRole(clubCode, userId)

        call.enqueue(object : Callback<RoleResVO> {
            override fun onResponse(call: Call<RoleResVO>, response: Response<RoleResVO>) {
                if (response.isSuccessful) {
                    val roleResponse = response.body()
                    if (roleResponse != null && roleResponse.userRole != 3) {
                        // 현재 로그인한 회원이 운영진인 경우
                        Log.d("getUserRole", "사용자 역할: ${roleResponse.userRole}")
                        binding.icMore.setOnClickListener { view ->
                            val popupMenu = PopupMenu(this@ScheduleActivity, view)
                            val menuInflater = popupMenu.menuInflater
                            menuInflater.inflate(R.menu.schedule_option_menu, popupMenu.menu)
                            popupMenu.setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.menu_option1 -> {
                                        // 일정 수정
                                        val intent = Intent(
                                            this@ScheduleActivity,
                                            MakeScheduleActivity::class.java
                                        )
                                        intent.putExtra("clickedSchedule", clickedSchedule)
                                        intent.putExtra("title", "일정 수정")
                                        startActivity(intent)
                                        finish()
                                        true
                                    }
                                    R.id.menu_option2 -> {
                                        // 일정 삭제
                                        showActivityDialogBox(
                                            this@ScheduleActivity,
                                            "일정을 삭제하시겠어요?",
                                            "삭제하기",
                                            "일정이 삭제되었습니다.",
                                            scheCode
                                        ) {
                                            // '일정 삭제하기' 버튼 클릭 시 실행할 내용
                                            deleteSche(scheCode) // deleteSche 함수 호출
                                        }
                                        true
                                    }
                                    else -> false
                                }
                            }
                            popupMenu.show()
                        }
                    } else {
                        binding.icMore.visibility = INVISIBLE
                    }
                } else {
                    Log.d("getUserRole", "사용자 역할 조회 실패")
                    Log.d("getUserRole", "응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RoleResVO>, t: Throwable) {
                Log.e("사용자 역할 조회", "사용자 역할 조회 네트워크 요청 실패", t)
            }
        })
    }
}


