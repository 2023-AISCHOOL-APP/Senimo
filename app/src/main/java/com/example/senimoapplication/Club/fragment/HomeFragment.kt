package com.example.senimoapplication.Club.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.senimoapplication.Club.Activity_club.MakeScheduleActivity
import com.example.senimoapplication.Club.Activity_club.ScheduleActivity
import com.example.senimoapplication.Club.VO.AllMemberResVO
import com.example.senimoapplication.Club.VO.AllScheduleMemberResVO
import com.example.senimoapplication.Club.VO.AllSchedulesResVO
import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.Club.VO.DeleteMemberVO
import com.example.senimoapplication.Club.VO.JoinClubResVO
import com.example.senimoapplication.Club.VO.MemberVO
import com.example.senimoapplication.Club.VO.QuitClubResVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.VO.UpdateMemberVO
import com.example.senimoapplication.Club.adapter.MemberAdapter
import com.example.senimoapplication.Club.adapter.ScheduleAdapter
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.Common.showAlertDialogBox
import com.example.senimoapplication.Common.showQuitDialogBox
import com.example.senimoapplication.MainPage.Activity_main.CreateMeetingActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentHomeBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import com.example.senimoapplication.server.Token.UserData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    var clickedMeeting: MeetingVO? = null // MeetingVO? 타입으로 선언
    var clickedSchedule : ScheduleVO? =null
    var createMeeting: MeetingVO? = null
    var userId: String? = null
    var clubCode: String? = null
    var staffList : List<String> = emptyList()
    var joinedList : List<String> = emptyList()
    var clubLeader : String? = null
    var clubName : String? = null

    // 모임 정보 수정 내용 바로 반영하기 (모임 정보 : displayMeetingInfo())
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val updatedMeeting: MeetingVO? = result.data?.getParcelableExtra("CreateMeeting")
                updatedMeeting?.let {displayMeetingInfo(it)}
            }
        }

    // 스케줄 액티비티에서 뒤로가기 버튼을 눌러서 이동했을 때 새로고침 하기
    private val startForResultSchedule =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val updatedSchedule = data?.getParcelableExtra<ScheduleVO?>("ScheduleInfo")
                Log.d("ScheduleInfo", "data값확인${updatedSchedule}")
                if (updatedSchedule != null) {
                    clickedSchedule = updatedSchedule
                    Log.d("ScheduleInfo", "startForResultSchedule: updated clickedSchedule = ${clickedSchedule?.joinedMembers}")
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // 현재 로그인한 회원 정보
        val UserData = PreferenceManager.getUser(requireContext())
        userId = UserData?.user_id

        // intent 데이터 관리 (메인 -> 모임홈)
        clickedMeeting = activity?.intent?.getParcelableExtra("clickedMeeting")
        createMeeting = activity?.intent?.getParcelableExtra("CreateMeeting")
        Log.d("getclickedMeetinghome", clickedMeeting.toString())

        clubCode = activity?.intent?.getStringExtra("clubCodeFromMakeSchedule")

        fetchClubInfo()

        // 클럽 회원 목록 가져오기 (초기 세팅)
        clickedMeeting = activity?.intent?.getParcelableExtra("clickedMeeting")
        clubCode = clickedMeeting?.club_code
        clubName = clickedMeeting?.title
        clubLeader = clickedMeeting?.userId

        // 모임 데이터 가져오기
        fetchMemberList(view)
        fetchScheduleData()

        return view

    }
    override fun onResume() {
        super.onResume()
        fetchScheduleData()
    }

    // 클럽 전체 회원 목록 새로고침
    fun fetchMemberList(view: View?) {
        if (view != null && userId != null) {
            val server = Server(requireContext())
            val memberManager = MemberManager(server)
            Log.d("getclickedMeetinghome", "${clubCode}")
            clubCode?.let { code ->
                memberManager.getAllMembers(code, object : Callback<AllMemberResVO> {
                    override fun onResponse(call: Call<AllMemberResVO>, response: Response<AllMemberResVO>) {
                        Log.d("getclickedMeetinghome", "통신재시작")
                        if (response.isSuccessful) {
                            val allMemberResVO: AllMemberResVO? = response.body()
                            Log.d("getclickedMeetinghome", "리프레시모임장VO${allMemberResVO}")
                            if (allMemberResVO != null) {
                                val memberList: List<MemberVO> = allMemberResVO.data
                                Log.d("getclickedMeetinghome", "리프레시모임장VO${memberList}")
                                clubLeader = memberList.find { it.clubRole == 1 }?.userId
                                Log.d("getclickedMeetinghome", "리프레시모임장${staffList}")
                                staffList = memberList.filter { it.clubRole == 1 || it.clubRole == 2 }.map { it.userId }.toList()
                                joinedList = memberList.map { it.userId }

                                val m_adapter = MemberAdapter(requireContext(), R.layout.club_member_list, ArrayList(memberList), clubLeader, this@HomeFragment)
                                binding.rvMember.adapter = m_adapter
                                binding.rvMember.layoutManager = LinearLayoutManager(view.context)

                                // 운영진만 보이는 버튼
                                if (userId in staffList){
                                    binding.tvMoveEdit.visibility = VISIBLE
                                    binding.btnNewSchedule.visibility = VISIBLE

                                    binding.tvMoveEdit.setOnClickListener {
                                        val intent = Intent(view.context, CreateMeetingActivity::class.java)
                                        intent.putExtra("MeetingVO", clickedMeeting)
                                        intent.putExtra("title", "모임 정보 수정")
                                        intent.putExtra("btnTitle", "모임 정보 수정하기")
                                        startForResult.launch(intent)
                                        //activity?.supportFragmentManager?.beginTransaction()?.remove(this@HomeFragment)?.commit()
                                        Log.d("click", "모임 수정페이지로 값을 전송합니다. ${clickedMeeting}")
                                    }

                                    binding.btnNewSchedule.setOnClickListener {
                                        val intent = Intent(view.context, MakeScheduleActivity::class.java)
                                        val clubCode = code
                                        intent.putExtra("club_code", code)
                                        Log.d("club_code", code)
                                        view.context.startActivity(intent)
                                    }
                                } else {
                                    binding.tvMoveEdit.visibility = INVISIBLE
                                    binding.btnNewSchedule.visibility = GONE
                                }

                                // 모임 가입/탈퇴 버튼 기능
                                if(userId in joinedList){
                                    binding.btnJoinClub.text ="모임 탈퇴하기"
                                    binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                                    binding.btnJoinClub.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
                                    binding.btnJoinClub.setOnClickListener {
                                        if (clubLeader == userId) {
                                            showAlertDialogBox(requireContext(), "모임장을 위임해야 탈퇴할 수 있습니다.","확인")
                                        } else {
                                            quitClub(clubCode,userId)
                                        }
                                    }
                                }else{
                                    binding.btnJoinClub.text ="모임 가입하기"
                                    binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                                    binding.btnJoinClub.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                                    binding.btnJoinClub.setOnClickListener {
                                        joinClub(clubCode,userId)
                                    }
                                }
                            }
                        } else {
                            Log.d("getclickedMeetinghome", "멤버 리스트 만들기 실패")
                        }
                    }

                    override fun onFailure(call: Call<AllMemberResVO>, t: Throwable) {
                        Log.d("getclickedMeetinghome", "스택 트레이스: ", t)
                    }
                })
            }
        }
    }

    // 스케줄 데이터 새로고침 (통신)
    private fun fetchScheduleData() {
        val server = Server(requireContext())
        val scheduleManager = ScheduleManager(server)
        clubCode?.let { code ->
            scheduleManager.getSchedules(code, object : Callback<AllSchedulesResVO> {
                override fun onResponse(call: Call<AllSchedulesResVO>, response: Response<AllSchedulesResVO>) {
                    Log.d("getclickedSchedule", "통신시작")
                    if (response.isSuccessful) {
                        val scheduleList = response.body()?.data
                        Log.d("getclickedSchedule", "$scheduleList")
                        scheduleList?.let {
                            updateScheduleUI(it)
                        }
                    } else {
                        Log.d("getclickedSchedule", "일정 리스트 가져오기 실패")
                    }
                }
                override fun onFailure(call: Call<AllSchedulesResVO>, t: Throwable) {
                    Log.d("getclickedSchedule", "스택 트레이스: ", t)
                }
            })
        }
    }

    // 스케줄 UI 업데이트
    private fun updateScheduleUI(scheduleList: List<ScheduleVO>) {
        val s_adapter = ScheduleAdapter(requireContext(), R.layout.schedule_list, scheduleList, clickedSchedule?.joinedMembers, clickedSchedule?.scheCode)
        binding.rvSchedule.adapter = s_adapter
        binding.rvSchedule.layoutManager = LinearLayoutManager(context)
        binding.rvSchedule.addOnItemTouchListener(
            RecyclerItemClickListener(requireContext(), binding.rvSchedule,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val clickedSchedule = scheduleList[position]
                        // 클릭된 아이템의 ScheduleVO 가져오기
                        Log.d("joinedList", "일정탭_가입회원 목록: $joinedList, 현재 유저 $userId")
                        if (joinedList.contains(userId)) {
                            // 새로운 액티비티로 이동
                            val intent = Intent(requireContext(), ScheduleActivity::class.java)
                            intent.putExtra("ScheduleInfo", clickedSchedule)
                            intent.putExtra("clubInfo", clickedMeeting)
                            intent.putExtra("clubName", clubName)
                            intent.putStringArrayListExtra("staffList", ArrayList(staffList))
                            Log.d("HomeFragment", "Launching ScheduleActivity")
                            startForResultSchedule.launch(intent)
                        } else {
                            // 여기서 showAlertDialogBox 함수를 호출할 때 Context를 전달합니다.
                            showAlertDialogBox(requireContext(), "모임 회원만 확인할 수 있습니다.", "확인")
                        }
                    }
                })
        )
    }

    // 메인 -> 모임홈 : 처음 데이터 가져오기
    private fun fetchClubInfo() {
        val service = Server(requireContext()).service
        if (createMeeting != null) {
            Log.d("fetchClubInfo_createMeeting", createMeeting.toString())
            //MeetingVO(gu=광산구, title=헬스클럽, content=인생은 100세 시대 운동을 하자!!, keyword=자기계발, attendance=0, allMember=20, imageUri=https://improved-sadly-snake.ngrok-free.app/uploads/IMG_20231119_094239.jpg, club_code=, userId=)
            displayMeetingInfo(createMeeting!!)
            createMeeting = null // createMeeting 초기화해서 모임정보 눌렀을떄 안겹치게
        } else {
            clickedMeeting?.let { meeting ->
                val clubCode = meeting.club_code
                val call = service.getClubInfo(clubCode)

                call.enqueue(object : Callback<ClubInfoVO> {
                    override fun onResponse(
                        call: Call<ClubInfoVO>,
                        response: Response<ClubInfoVO>
                    ) {
                        Log.d("ClubInfo", response.toString())
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                Log.d("ClubInfo 응답 성공", response.body().toString())
                                response.body()?.let { clubInfos ->
                                    binding.tvMemberAllNum.text = "/${clubInfos.maxCnt}명"
                                    binding.tvMemberNum.text = "${clubInfos.joinedUserCnt}"
                                    binding.tvClubNameTitle.text = clubInfos.clubName
                                    binding.tvClubLoca.text = clubInfos.clubLocation
                                    binding.tvClubIntro.text = clubInfos.clubIntroduce
                                    binding.tvKeyword.text = clubInfos.keywordName

                                    // 키워드 색상 변경
                                    val keywordBackground = getKeywordBackground(clubInfos.keywordName)
                                    binding.tvKeyword.setBackgroundResource(keywordBackground)

                                    Glide.with(this@HomeFragment) // 현재 컨텍스트를 파라미터로 받습니다
                                        .load(clubInfos.clubImageUri) // MeetingVO 객체의 imageUri
                                        .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
                                        .error(R.drawable.golf_img) // 로딩 실패 시 표시될 이미지
                                        .into(binding.clubImage) // 이미지를 표시할 ImageView
                                }
                            } else {
                                Log.e("HomFragment", "서버 에러: ${response.code()}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ClubInfoVO>, t: Throwable) {
                        Log.e("HomeFragment", "네트워크 요청 실패", t)
                    }
                })
            } ?: run {
                // clickedMeeting이 null인 경우의 처리
                Log.e("HomeFragment", "clickedMeeting이 null입니다.")
            }

        }


    }

    private fun getKeywordBackground(keyword: String): Int {
        return when (keyword) {
            "운동" -> R.drawable.keyword
            "취미" -> R.drawable.keyword_color2
            "전시/공연" -> R.drawable.keyword_color3
            "여행" -> R.drawable.keyword_color4
            "자기계발" -> R.drawable.keyword_color5
            "재테크" -> R.drawable.keyword_color6
            else -> R.drawable.keyword_color6 // 기본 배경색
        }
    }

    // 메인 -> 모임홈 : 데이터 view에 적용하기
    private fun displayMeetingInfo(meeting: MeetingVO) {
        binding.tvMemberAllNum.text = "/${meeting.allMember}명"
        binding.tvMemberNum.text = "${meeting.attendance}"
        binding.tvClubNameTitle.text = meeting.title
        binding.tvClubLoca.text = meeting.gu
        binding.tvClubIntro.text = meeting.content
        binding.tvKeyword.text = meeting.keyword

        val keywordBackground = getKeywordBackground(meeting.keyword)
        binding.tvKeyword.setBackgroundResource(keywordBackground)

        Glide.with(this@HomeFragment) // 현재 컨텍스트를 파라미터로 받습니다
            .load(meeting.imageUri) // MeetingVO 객체의 imageUri
            .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
            .error(R.drawable.golf_img) // 로딩 실패 시 표시될 이미지
//            .diskCacheStrategy(DiskCacheStrategy.NONE) // 캐시 무시
//            .skipMemoryCache(true) // 메모리 캐시 무시
            .into(binding.clubImage) // 이미지를 표시할 ImageView
    }

    // 모임 가입하기 기능
    fun joinClub(clubCode: String?, userId: String?) {
        val service = Server(requireContext()).service
        val call = service.joinClub(clubCode, userId)

        call.enqueue(object : Callback<JoinClubResVO> {
            override fun onResponse(call: Call<JoinClubResVO>, response: Response<JoinClubResVO>) {
                if (response.isSuccessful) {
                    val joinClubRes = response.body()
                    if (joinClubRes != null && joinClubRes.rows == "success") {
                        binding.btnJoinClub.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
                        binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                        binding.btnJoinClub.text = "모임 탈퇴하기"
                        Toast.makeText(activity, "모임에 가입되었습니다.", Toast.LENGTH_SHORT).show()
                        fetchClubInfo()
                        fetchMemberList(view)
                    } else {
                        Log.d("joinClud", "not success")
                    }
                }
            }

            override fun onFailure(call: Call<JoinClubResVO>, t: Throwable) {
                Log.e("HomeFragment joinClub", "joinClub 네트워크 요청 실패", t)
            }

        })
    }

    // 모임 탈퇴하기 기능
    fun quitClub(clubCode: String?, userId: String?) {
        val service = Server(requireContext()).service
        val call = service.quitClub(clubCode, userId)

        call.enqueue(object : Callback<QuitClubResVO> {
            override fun onResponse(call: Call<QuitClubResVO>, response: Response<QuitClubResVO>) {
                if (response.isSuccessful) {
                    val quitClubRes = response.body()
                    if (quitClubRes != null && quitClubRes.rows == "success") {
                        binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                        binding.btnJoinClub.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        binding.btnJoinClub.text = "모임 가입하기"
                        Toast.makeText(activity, "모임에서 탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
                        fetchClubInfo()
                        fetchMemberList(view)
                    } else {
                        Log.d("quitClud", "not success")
                    }
                }
            }

            override fun onFailure(call: Call<QuitClubResVO>, t: Throwable) {
                Log.e("HomeFragment quitClub", "quitClub 네트워크 요청 실패", t)
            }

        })
    }
}


// 모임 회원 정보 통신을 위한 매니저 설정
class MemberManager(private val server: Server) {
    // 클럽 코드를 기반으로 멤버 목록을 가져오는 함수
    fun getAllMembers(clubCode: String, callback: Callback<AllMemberResVO>) {
        val call = server.service.getAllMembers(clubCode)
        call.enqueue(callback)
    }

    // 스케줄 코드를 기반으로 참여 회원 목록 가져오는 함수
    fun getScheduleMembers(scheCode:String, callback: Callback<AllScheduleMemberResVO>){
        val call = server.service.getScheduleMembers(scheCode)
        call.enqueue(callback)
    }

    // 멤버 역할 업데이트 함수
    fun updateMember(updateMemberVO: UpdateMemberVO, callback: Callback<JsonObject>) {
        val call = server.service.updateMember(updateMemberVO)
        call.enqueue(callback)
    }

    // 모임장 변경 업데이트 함수
    fun updateLeader(updateMemberVO: UpdateMemberVO, callback: Callback<JsonObject>) {
        val call = server.service.updateMember(updateMemberVO)
        call.enqueue(callback)
    }
    // 멤버 내보내기 함수
    fun deleteMember(deleteMemberVO: DeleteMemberVO, callback: Callback<JsonObject>) {
        val call = server.service.deleteMember(deleteMemberVO)
        call.enqueue(callback)
    }
}

// 모임 일정 통신을 위한 매니저 설정
class ScheduleManager(private val server: Server) {
    fun getSchedules(clubCode: String, callback: Callback<AllSchedulesResVO>) {
        val call = server.service.getSchedules(clubCode)
        call.enqueue(callback)
    }
}