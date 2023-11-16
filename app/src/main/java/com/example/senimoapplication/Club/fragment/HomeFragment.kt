package com.example.senimoapplication.Club.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.Activity_club.MakeScheduleActivity
import com.example.senimoapplication.Club.Activity_club.ScheduleActivity
import com.example.senimoapplication.Club.VO.ClubInfoVO
import com.example.senimoapplication.Club.VO.MemberVO
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.adapter.MemberAdapter
import com.example.senimoapplication.Club.adapter.ScheduleAdapter
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.MainPage.Retrofit.ApiService
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentHomeBinding
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    val ClubInfoList : ArrayList<ClubInfoVO> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val view = binding.root

        val scheduleList : ArrayList<ScheduleVO> = ArrayList()
        val memberList : ArrayList<MemberVO> = ArrayList()

        val s_adapter = ScheduleAdapter(requireContext(),R.layout.schedule_list, scheduleList)
        val m_adapter = MemberAdapter(requireContext(),R.layout.club_member_list, memberList)


        // 관리자 여부 확인해서 버튼 보이기
//        if(회원레벨이 3인 경우){
//            binding.tvMoveEdit.visibility = INVISIBLE
//            binding.btnNewSchedule.visibility = GONE
//        }else{
//            // 관리자, 운영자인 경우
//            binding.tvMoveEdit.visibility = VISIBLE
//            binding.btnNewSchedule.visibility = VISIBLE
//        }
//
        // view요소들 데이터 변경


//        val clubInfo = ClubInfoVO(
//            "img_sample",
//            5,
//            10,
//            "양희준과 아이들 T1F4",
//            "동구",
//            "자기계발",
//            "시니어의 활동적인 삶을 위해서 모임 플랫폼을 만들기로 했습니다. 우리의 첫 번째 모임입니다.",
//        )

        binding.tvMoveEdit.setOnClickListener {
            val intent = Intent(view.context, MakeScheduleActivity::class.java)
            startActivity(intent)
        }


        // 일정 리사이클러뷰 어댑터 연결
        binding.rvSchedule.adapter = s_adapter
        binding.rvSchedule.layoutManager=LinearLayoutManager(view.context)

        // 모임 일정 가데이터
        scheduleList.add(ScheduleVO("모임명","신나는 크리스마스 파티","노는게 제일 좋은 친구들 모여서 함꼐 놀아요","2023-12-20 17:00",30000, "광주 동구 제봉로 대성학원 3층", 26,20,"모집중" ))
        scheduleList.add(ScheduleVO("모임명","짬뽕이 제일 좋아","짜장보다는 짬뽕드실 분 모집","2023-11-8 17:00",10000, "광주 동구 도야짬뽕", 10,10,"모집마감" )) // 도운이 수정함 fee int로 바꿔서"삭제
        //scheduleList.add(ScheduleVO("점심을 공유합시다","2023-11-6 17:00","30000", "광주 동구 동부식당", 26,20,"모집마감" ))

        // 모임 상세 페이지로 이동
        binding.rvSchedule.addOnItemTouchListener(
            RecyclerItemClickListener(requireContext(), binding.rvSchedule, object : RecyclerItemClickListener.OnItemClickListener {
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
            view.context.startActivity(intent)
        }

        // 전체 회원 리사이클러뷰 어탭터 연결
        binding.rvMember.adapter = m_adapter
        binding.rvMember.layoutManager=LinearLayoutManager(view.context)

        // 전체 회원 목록 가데이터
        memberList.add(MemberVO("양희준", 1, R.drawable.img_sample))
        memberList.add(MemberVO("최효정", 2, R.drawable.img_sample))
        memberList.add(MemberVO("국지호", 2, R.drawable.img_sample))
        memberList.add(MemberVO("김도운", 3, R.drawable.img_sample))
        memberList.add(MemberVO("이지혜", 3, R.drawable.img_sample))
        memberList.add(MemberVO("나예호", 3, R.drawable.img_sample))

        // 회원 정보 페이지로 이동
        // 클릭 이벤트 처리
//        rvMember.addOnItemTouchListener(
//            RecyclerItemClickListener(this, rvMember, object : RecyclerItemClickListener.OnItemClickListener {
//                override fun onItemClick(view: View, position: Int) {
//                    // 클릭한 아이템의 정보를 가져오거나 원하는 프래그먼트를 결정할 수 있음
//                    val selectedFragment = YourFragment() // 여기에는 원하는 프래그먼트 클래스를 넣어주세요
//
//                    // 프래그먼트 전환
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, selectedFragment) // R.id.fragment_container는 프래그먼트를 호스팅하는 레이아웃의 ID입니다.
//                        .commit()
//                }
//            })
//        )

        // 모임 가입 상태 체크 및 버튼 전환 (join
        var joinstate : Int = 0
        binding.btnJoinClub.setOnClickListener {
            if(joinstate == 0){
                binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context, R.color.main))
                binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                binding.btnJoinClub.text = "모임 탈퇴하기"
                joinstate = 1
            }else{
                binding.btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                binding.btnJoinClub.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                binding.btnJoinClub.text = "모임 가입하기"
                joinstate = 0
            }
        }

        fetchClubInfo()

        return view
    }

    private fun fetchClubInfo() {
        val retrofit = Server().retrofit
        val service = retrofit.create(ApiService::class.java)
        val clubCode = "club_code1"
        val call = service.getClubInfo(clubCode)

        call.enqueue(object : Callback<ClubInfoVO>{
            override fun onResponse(call: Call<ClubInfoVO>, response: Response<ClubInfoVO>) {
                Log.d("ClubInfo", response.toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("ClubInfo 응답 성공",response.body().toString())
                        response.body()?.let { clubInfos ->
                            //binding.clubImage.setImageURI(clubInfo.clubImg)
                            binding.tvMemberAllNum.text="/${clubInfos.maxCnt}명"
                            binding.tvMemberNum.text="${clubInfos.joinedUserCnt}"
                            binding.tvClubNameTitle.text = clubInfos.clubName
                            binding.tvClubLoca.text = clubInfos.clubLocation
                            binding.tvClubIntro.text = clubInfos.clubIntroduce
                        }
                    }else {
                        Log.e("HomFragment", "서버 에러: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<ClubInfoVO>, t: Throwable) {
                Log.e("HomeFragment", "네트워크 요청 실패", t)
            }
        })
    }

}