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
    val ClubInfoList: ArrayList<ClubInfoVO> = ArrayList()
    var clickedMeeting: MeetingVO? = null // MeetingVO? 타입으로 선언
    var createMeeting: MeetingVO? = null
    val memberList: ArrayList<MemberVO> = ArrayList()
    var userId: String? =null
    var clubCode: String? = null
    var staffList : List<String> = emptyList()
    var joinedList : List<String> = emptyList()
    var clubLeader : String? = null
    var clubName : String? = null

    // 모임정보 수정 내용 바로 반영하기 (모임 정보 : displayMeetingInfo())
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val updatedMeeting: MeetingVO? = result.data?.getParcelableExtra("CreateMeeting")
                updatedMeeting?.let {displayMeetingInfo(it)}
            }
        }

    // 클럽 전체 회원 목록 새로고침
    fun refreshMemberList(view: View?) {
        if (view != null && userId != null) {
            clickedMeeting =
                activity?.intent?.getParcelableExtra<MeetingVO>("clickedMeeting")
            val server = Server(requireContext())
            val memberManager = MemberManager(server)
            clubCode = clickedMeeting?.club_code

            Log.d("getclickedMeetinghome", "${clubCode}")
            clubCode?.let { code ->
                memberManager.getAllMembers(code, object : Callback<AllMemberResVO> {
                    override fun onResponse(call: Call<AllMemberResVO>, response: Response<AllMemberResVO>) {
                        Log.d("getclickedMeetinghome", "통신재시작")
                        if (response.isSuccessful) {
                            val allMemberResVO: AllMemberResVO? = response.body()

                            if (allMemberResVO != null) {
                                val memberList: List<MemberVO> = allMemberResVO.data
                                clubLeader = memberList.find { it.clubRole == 1 }?.userId
                                Log.d("getclickedMeetinghome", "리프레시모임장${clubLeader}")
                                staffList = memberList.filter { it.clubRole == 1 || it.clubRole == 2 }.map { it.userId }.toList()
                                joinedList = memberList.map { it.userId }

                                val m_adapter = MemberAdapter(requireContext(), R.layout.club_member_list, ArrayList(memberList ?: emptyList()), clubLeader, this@HomeFragment)
                                binding.rvMember.adapter = m_adapter
                                binding.rvMember.layoutManager = LinearLayoutManager(view?.context)

                                // 운영진만 보이는 버튼
                                if (userId in staffList){
                                    binding.tvMoveEdit.visibility = VISIBLE
                                    binding.btnNewSchedule.visibility = VISIBLE

                                    binding.tvMoveEdit.setOnClickListener {
                                        val intent = Intent(view?.context, CreateMeetingActivity::class.java)
                                        intent.putExtra("MeetingVO", clickedMeeting)
                                        intent.putExtra("title", "모임 정보 수정")
                                        intent.putExtra("btnTitle", "모임 정보 수정하기")
                                        startForResult.launch(intent)
                                        Log.d("click", "모임 수정페이지로 값을 전송합니다. ${clickedMeeting}")
                                    }

                                    binding.btnNewSchedule.setOnClickListener {
                                        val intent = Intent(view?.context, MakeScheduleActivity::class.java)
                                        view?.context?.startActivity(intent)
                                    }
                                } else {
                                    binding.tvMoveEdit.visibility = INVISIBLE
                                    binding.btnNewSchedule.visibility = GONE
                                }

                                // 가입하기 버튼
                                if(userId in joinedList){
                                    // 이미 모임에 가입된 회원인 경우
                                    Log.d("joinedList","유저${userId}, 가입자목록 ${joinedList}")
                                    binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.main))
                                    binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                                    binding.btnJoinClub.text = "모임 탈퇴하기"

                                    binding.btnJoinClub.setOnClickListener {
                                        quitClub(clubCode,userId)
                                        binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                                        binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.white))
                                        binding.btnJoinClub.text = "모임 가입하기"
                                    }
                                } else {
                                    // 모임에 가입하지 않은 회원인 경우
                                    binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                                    binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.white))
                                    binding.btnJoinClub.text = "모임 가입하기"

                                    binding.btnJoinClub.setOnClickListener {
                                        joinClub(clubCode,userId)
                                        binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.main))
                                        binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                                        binding.btnJoinClub.text = "모임 탈퇴하기"
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
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val UserData = PreferenceManager.getUser(requireContext())
        userId = UserData?.user_id

        // intent 데이터 관리
        clickedMeeting =
            activity?.intent?.getParcelableExtra<MeetingVO>("clickedMeeting")
        createMeeting =
            activity?.intent?.getParcelableExtra<MeetingVO>("CreateMeeting")

        // 모임상세 -> 클럽 액티비티에서 넘어온 값
        val joinedMemberCount = arguments?.getString("JOINED_MEMBER_COUNT")
        val selectedScheCode = arguments?.getString("SELECTED_SCHE_CODE")


        Log.d("getclickedMeetinghome", clickedMeeting.toString())


        val server = Server(requireContext())
        val memberManager = MemberManager(server)
        fetchClubInfo()

        // 클럽 회원 목록 가져오기 (초기 세팅)
        clubCode = clickedMeeting?.club_code
        clubName = clickedMeeting?.title
        clubLeader = clickedMeeting?.userId
        clubCode?.let { code ->
            Log.d("getclickedMeetinghome", "${code}")
            memberManager.getAllMembers(code, object : Callback<AllMemberResVO> {
                override fun onResponse(call: Call<AllMemberResVO>, response: Response<AllMemberResVO>) {
                    Log.d("getclickedMeetinghome", "통신시작")
                    if (response.isSuccessful) {
                        val allMemberResVO: AllMemberResVO? = response.body()
                        Log.d("getclickedMeetinghome", "${allMemberResVO}")
                        if (allMemberResVO != null) {
                            val memberList: List<MemberVO> = allMemberResVO.data
                            staffList = memberList.filter { it.clubRole == 1 || it.clubRole == 2 }.map { it.userId }.toList()
                            joinedList = memberList.map { it.userId }
                            Log.d("getclickedMeetinghome", "기존 모임장 ${clubLeader}")
                            Log.d("joinedList", "가입회원목록 ${joinedList}")
                            val m_adapter = MemberAdapter(requireContext(), R.layout.club_member_list, ArrayList(memberList ?: emptyList()), clubLeader, this@HomeFragment)
                            binding.rvMember.adapter = m_adapter
                            binding.rvMember.layoutManager = LinearLayoutManager(view?.context)

                            // 운영진만 보이는 버튼
                            if (userId in staffList){
                                binding.tvMoveEdit.visibility = VISIBLE
                                binding.btnNewSchedule.visibility = VISIBLE

                                binding.tvMoveEdit.setOnClickListener {
                                    val intent = Intent(view?.context, CreateMeetingActivity::class.java)
                                    intent.putExtra("MeetingVO", clickedMeeting)
                                    intent.putExtra("title", "모임 정보 수정")
                                    intent.putExtra("btnTitle", "모임 정보 수정하기")
                                    startForResult.launch(intent)
                                    Log.d("click", "모임 수정페이지로 값을 전송합니다. ${clickedMeeting}")
                                }

                                binding.btnNewSchedule.setOnClickListener {
                                    val intent = Intent(view?.context, MakeScheduleActivity::class.java)
                                    view?.context?.startActivity(intent)
                                }
                            } else {
                                binding.tvMoveEdit.visibility = INVISIBLE
                                binding.btnNewSchedule.visibility = GONE
                            }

                            // 가입하기 버튼
                            if(userId in joinedList){
                                // 이미 모임에 가입된 회원인 경우
                                Log.d("joinedList","유저${userId}, 가입자목록 ${joinedList}")
                                binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.main))
                                binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                                binding.btnJoinClub.text = "모임 탈퇴하기"

                                binding.btnJoinClub.setOnClickListener {
                                    showQuitDialogBox(requireContext(),"정말 모임에서 탈퇴하시겠어요?","탈퇴하기","모임에서 탈퇴되었습니다.", this@HomeFragment, code)
                                    binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                                    binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.white))
                                    binding.btnJoinClub.text = "모임 가입하기"
                                }
                            } else {
                                // 모임에 가입하지 않은 회원인 경우
                                binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                                binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.white))
                                binding.btnJoinClub.text = "모임 가입하기"

                                binding.btnJoinClub.setOnClickListener {
                                    joinClub(clubCode,userId)
                                    binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.main))
                                    binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                                    binding.btnJoinClub.text = "모임 탈퇴하기"
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

        // 모임 일정 데이터 가져오기
        clubCode?.let { code ->
            ScheduleManager(server).getSchedules(code, object : Callback<AllSchedulesResVO> {
                override fun onResponse(call: Call<AllSchedulesResVO>, response: Response<AllSchedulesResVO>) {
                    Log.d("getclickedSchedule", "통신시작")
                    if (response.isSuccessful) {
                        val scheduleList: List<ScheduleVO>? = response.body()?.data
                        Log.d("getclickedSchedule", "${scheduleList}")
                        if (scheduleList != null) {
                            val s_adapter = ScheduleAdapter(requireContext(), R.layout.schedule_list, scheduleList,joinedMemberCount,selectedScheCode )
                            binding.rvSchedule.adapter = s_adapter
                            binding.rvSchedule.layoutManager = LinearLayoutManager(view.context)
                            binding.rvSchedule.addOnItemTouchListener(
                                RecyclerItemClickListener(requireContext(), binding.rvSchedule,
                                    object : RecyclerItemClickListener.OnItemClickListener {
                                        override fun onItemClick(view: View, position: Int) {
                                            val clickedSchedule = scheduleList[position] // 클릭된 아이템의 ScheduleVO 가져오기
                                            Log.d("joinedList","일정탭_가입회원 목록: ${joinedList}, 현재 유저 ${userId}")
                                            if (joinedList.contains(userId)) {
                                                // 새로운 액티비티로 이동
                                                val intent = Intent(requireContext(), ScheduleActivity::class.java)
                                                intent.putExtra("ScheduleInfo", clickedSchedule)
                                                intent.putExtra("clubName", clubName)
                                                intent.putExtra("scheCode", clickedSchedule.scheCode)
                                                intent.putStringArrayListExtra("staffList", ArrayList(staffList))
                                                startActivity(intent)
                                            } else {
                                                // 여기서 showAlertDialogBox 함수를 호출할 때 Context를 전달합니다.
                                                showAlertDialogBox(requireContext(), "모임 회원만 확인할 수 있습니다.", "확인")
                                            }
                                        }
                                    })
                            )
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

        // 모임 일정 등록 액티비티로 이동
        binding.btnNewSchedule.setOnClickListener {
            val intent = Intent(view.context, MakeScheduleActivity::class.java)
            val clubCode = clickedMeeting?.club_code.toString()
            intent.putExtra("club_code", clubCode)
            Log.d("club_code", clubCode)
            view.context.startActivity(intent)
        }

        return view
    }

    private fun fetchClubInfo() {
        val service = Server(requireContext()).service
        if (createMeeting != null) {
            Log.d("fetchClubInfo_createMeeting", createMeeting.toString())
            //MeetingVO(gu=광산구, title=헬스클럽, content=인생은 100세 시대 운동을 하자!!, keyword=자기계발, attendance=0, allMember=20, imageUri=https://improved-sadly-snake.ngrok-free.app/uploads/IMG_20231119_094239.jpg, club_code=, userId=)
            displayMeetingInfo(createMeeting!!)
            //createMeeting = null // createMeeting 초기화해서 모임정보 눌렀을떄 안겹치게
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
                                    //binding.clubImage.setImageURI(clubInfo.clubImg)
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
            .into(binding.clubImage) // 이미지를 표시할 ImageView
    }

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
                        refreshMemberList(view)
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
                        refreshMemberList(view)
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


// 모임 회원 정보 가져오기
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

// 모임 스케줄 가져오기
class ScheduleManager(private val server: Server) {
    fun getSchedules(clubCode: String, callback: Callback<AllSchedulesResVO>) {
        val call = server.service.getSchedules(clubCode)
        call.enqueue(callback)
    }

    fun scheduleInfo(scheCode: String, callback: Callback<ScheduleVO>) {
        val call = server.service.scheduleInfo(scheCode) // 올바른 메서드 호출
        call.enqueue(callback)
    }
}