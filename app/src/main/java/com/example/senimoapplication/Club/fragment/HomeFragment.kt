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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.Activity_club.MakeScheduleActivity
import com.example.senimoapplication.Club.Activity_club.ScheduleActivity
import com.example.senimoapplication.Club.VO.AllMemberResVO
import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.Club.VO.DeleteMemberVO
import com.example.senimoapplication.Club.VO.MemberVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.VO.UpdateMemberVO
import com.example.senimoapplication.Club.adapter.MemberAdapter
import com.example.senimoapplication.Club.adapter.ScheduleAdapter
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.MainPage.Activity_main.CreateMeetingActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.server.Retrofit.ApiService
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentHomeBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.UserData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val ClubInfoList: ArrayList<ClubInfoVO> = ArrayList()
    var clickedMeeting: MeetingVO? = null // MeetingVO? 타입으로 선언
    var createMeeting: MeetingVO? = null
    val memberList: ArrayList<MemberVO> = ArrayList()
    val userId = UserData.userId
    var clubCode: String? =null
    var staffList : List<String> = emptyList()
    var joinedList : List<String> = emptyList()
    var clubLeader : String? = null

    // 모임정보 수정 내용 바로 반영하기 (모임 정보 : displayMeetingInfo())
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val updatedMeeting: MeetingVO? = result.data?.getParcelableExtra("CreateMeeting")
                updatedMeeting?.let {displayMeetingInfo(it)}
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // intent 데이터 관리
        clickedMeeting =
            activity?.intent?.getParcelableExtra<MeetingVO>("clickedMeeting")
        createMeeting =
            activity?.intent?.getParcelableExtra<MeetingVO>("CreateMeeting")
        //clickedMeetinghome = activity?.intent?.getParcelableArrayListExtra("clickedMeetinghome") ?: ArrayList()

        Log.d("getclickedMeetinghome", clickedMeeting.toString())
        Log.d("Home_getData_memberList", memberList.toString())


        val scheduleList: ArrayList<ScheduleVO> = ArrayList()
        val s_adapter = ScheduleAdapter(requireContext(), R.layout.schedule_list, scheduleList)
        val memberManager = MemberManager()
        fetchClubInfo()

        // 전체 회원 목록 통신으로 가져오기 => 회원 목록 리사이클러뷰 어댑터 연결
        clubCode = clickedMeeting?.club_code
        clubLeader = clickedMeeting?.userId

        clubCode?.let { code ->
            memberManager.getAllMembers(code, object : Callback<AllMemberResVO> {
                override fun onResponse(call: Call<AllMemberResVO>, response: Response<AllMemberResVO>) {
                    if (response.isSuccessful) {
                        val allMemberResVO: AllMemberResVO? = response.body()
                        if (allMemberResVO != null) {
                            val memberList: List<MemberVO> = allMemberResVO.data
                            staffList = memberList.filter { it.clubRole == 1 || it.clubRole == 2 }.map { it.userId }.toList()
                            joinedList = memberList.map { it.userId }

                            val m_adapter = MemberAdapter(requireContext(), R.layout.club_member_list, ArrayList(memberList ?: emptyList()), clubLeader)
                            binding.rvMember.adapter = m_adapter
                            binding.rvMember.layoutManager = LinearLayoutManager(view.context)

                            // 운영진만 보이는 버튼
                            if(userId in staffList){
                                binding.tvMoveEdit.visibility = VISIBLE
                                binding.btnNewSchedule.visibility = VISIBLE

                                binding.tvMoveEdit.setOnClickListener {
                                    val intent =
                                        Intent(view.context, CreateMeetingActivity::class.java)
                                    intent.putExtra("MeetingVO", clickedMeeting)
                                    intent.putExtra("title", "모임 정보 수정")
                                    intent.putExtra("btnTitle", "모임 정보 수정하기")
                                    startForResult.launch(intent)
                                    Log.d("click", "모임 수정페이지로 값을 전송합니다. ${clickedMeeting}")
                                }

                                binding.btnNewSchedule.setOnClickListener {
                                    val intent = Intent(view.context, MakeScheduleActivity::class.java)
                                    view.context.startActivity(intent)
                                }
                            } else {
                                binding.tvMoveEdit.visibility = INVISIBLE
                                binding.btnNewSchedule.visibility = GONE
                            }

                            if(userId in joinedList){
                                binding.btnJoinClub.setOnClickListener {
                                    binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.main))
                                    binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                                    binding.btnJoinClub.text = "모임 탈퇴하기"
                                }
                            } else {
                                binding.btnJoinClub.setOnClickListener {
                                    binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                                        binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context,R.color.white))
                                        binding.btnJoinClub.text = "모임 가입하기"
                                }
                            }
                        }
                    } else {
                        Log.d("member", "멤버 리스트 만들기 실패")
                    }
                }

                override fun onFailure(call: Call<AllMemberResVO>, t: Throwable) {
                    Log.d("member", "스택 트레이스: ", t)
                }
            })
        }

        // 일정 리사이클러뷰 어댑터 연결
        binding.rvSchedule.adapter = s_adapter
        binding.rvSchedule.layoutManager = LinearLayoutManager(view.context)

        // 모임 일정 가데이터
        scheduleList.add(
            ScheduleVO(
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
        )
        scheduleList.add(
            ScheduleVO(
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
        )

        // 모임 상세 페이지로 이동
        binding.rvSchedule.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.rvSchedule,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val clickedSchedule = scheduleList[position] // 클릭된 아이템의 ScheduleVO 가져오기
                        // 새로운 액티비티로 이동
                        val intent = Intent(requireContext(), ScheduleActivity::class.java)
                        startActivity(intent)

                    }
                })
        )


        // 모임 일정 등록 액티비티로 이동
        binding.btnNewSchedule.setOnClickListener {
            val intent = Intent(view.context, MakeScheduleActivity::class.java)
            val clubCode = clickedMeeting?.club_code.toString()
            intent.putExtra("club_code", clubCode)
            Log.d("club_code", clubCode)
            view.context.startActivity(intent)
        }

        // 모임 가입 상태 체크 및 버튼 전환 
        var joinstate: Int = 0
        binding.btnJoinClub.setOnClickListener {
            if (joinstate == 0) {
                binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context, R.color.main))
                binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                binding.btnJoinClub.text = "모임 탈퇴하기"
                joinstate = 1
            } else {
                binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                binding.btnJoinClub.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.white
                    )
                )
                binding.btnJoinClub.text = "모임 가입하기"
                joinstate = 0
            }
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

    private fun displayMeetingInfo(meeting: MeetingVO) {
        binding.tvMemberAllNum.text = "/${meeting.allMember}명"
        binding.tvMemberNum.text = "${meeting.attendance}"
        binding.tvClubNameTitle.text = meeting.title
        binding.tvClubLoca.text = meeting.gu
        binding.tvClubIntro.text = meeting.content
        binding.tvKeyword.text = meeting.keyword
        Glide.with(this@HomeFragment) // 현재 컨텍스트를 파라미터로 받습니다
            // .asGif()
            .load(meeting.imageUri) // MeetingVO 객체의 imageUri
            .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
            .error(R.drawable.golf_img) // 로딩 실패 시 표시될 이미지
            .into(binding.clubImage) // 이미지를 표시할 ImageView
    }

}
class MemberManager {
    private val apiService: ApiService

    init {
        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.70.199:3333")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    // 클럽 코드를 기반으로 멤버 목록을 가져오는 함수
    fun getAllMembers(clubCode: String, callback: Callback<AllMemberResVO>) {
        val call = apiService.getAllMembers(clubCode)
        call.enqueue(callback)
    }

    // 멤버 역할 업데이트 함수
    fun updateMember(updateMemberVO: UpdateMemberVO, callback: Callback<JsonObject>) {
        val call = apiService.updateMember(updateMemberVO)
        call.enqueue(callback)
    }

    // 멤버 내보내기 함수
    fun deleteMember(deleteMemberVO: DeleteMemberVO, callback: Callback<JsonObject>) {
        val call = apiService.deleteMember(deleteMemberVO)
        call.enqueue(callback)
    }
}
