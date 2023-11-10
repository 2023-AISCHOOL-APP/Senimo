package com.example.senimoapplication.Club.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.Activity_club.MakeScheduleActivity
import com.example.senimoapplication.Common.KeywordAdapter
import com.example.senimoapplication.Club.adapter.MemberAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.Club.Activity_club.ScheduleActivity
import com.example.senimoapplication.Club.adapter.ScheduleAdapter
import com.example.senimoapplication.Club.VO.MemberVO
import com.example.senimoapplication.Club.VO.ScheduleVO

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val scheduleList : ArrayList<ScheduleVO> = ArrayList()
        val s_adapter = ScheduleAdapter(requireContext(),R.layout.schedule_list, scheduleList)
        val rvSchedule = view.findViewById<RecyclerView>(R.id.rvSchedule)
        val rvMember = view.findViewById<RecyclerView>(R.id.rvMember)
        val memberList : ArrayList<MemberVO> = ArrayList()
        val m_adapter = MemberAdapter(requireContext(),R.layout.club_member_list, memberList)
        val keywordList : ArrayList<String> = ArrayList()
        val k_adapter = KeywordAdapter(requireContext(),R.layout.keyword_list, keywordList)
        val rvKeyword = view.findViewById<RecyclerView>(R.id.rvKeyword)
        val btnJoinClub = view.findViewById<Button>(R.id.btnJoinClub)
        val btnNewSchedule = view.findViewById<Button>(R.id.btnNewSchedule)


        // 모임 키워드 리사이클러뷰 어댑터 연결
        rvKeyword.adapter = k_adapter
        rvKeyword.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        // 모임 키워드 가데이터
        keywordList.add("운동")
        keywordList.add("취미")
        keywordList.add("전시/공연")
        keywordList.add("여행")


        // 일정 리사이클러뷰 어댑터 연결
        rvSchedule.adapter = s_adapter
        rvSchedule.layoutManager=LinearLayoutManager(view.context)

        // 모임 일정 가데이터
        scheduleList.add(ScheduleVO("신나는 크리스마스 파티","노는게 제일 좋은 친구들 모여서 함꼐 놀아요","2023-12-20 17:00","30000", "광주 동구 제봉로 대성학원 3층", 26,20,"모집중" ))
        scheduleList.add(ScheduleVO("짬뽕이 제일 좋아","짜장보다는 짬뽕드실 분 모집","2023-11-8 17:00","10000", "광주 동구 도야짬뽕", 10,10,"모집마감" ))
        //scheduleList.add(ScheduleVO("점심을 공유합시다","2023-11-6 17:00","30000", "광주 동구 동부식당", 26,20,"모집마감" ))

        // 모임 상세 페이지로 이동
        rvSchedule.addOnItemTouchListener(
            RecyclerItemClickListener(requireContext(), rvSchedule, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val clickedSchedule = scheduleList[position] // 클릭된 아이템의 ScheduleVO 가져오기
                    // 새로운 액티비티로 이동
                    val intent = Intent(requireContext(), ScheduleActivity::class.java)
                    startActivity(intent)
                }
            })
        )

        // 모임 일정 등록 액티비티로 이동
        btnNewSchedule.setOnClickListener {
            val intent = Intent(view.context, MakeScheduleActivity::class.java)
            view.context.startActivity(intent)
        }

        // 전체 회원 리사이클러뷰 어탭터 연결
        rvMember.adapter = m_adapter
        rvMember.layoutManager=LinearLayoutManager(view.context)

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
        btnJoinClub.setOnClickListener {
            if(joinstate == 0){
                btnJoinClub.setTextColor(ContextCompat.getColor(view.context, R.color.main))
                btnJoinClub.setBackgroundResource(R.drawable.button_shape)
                btnJoinClub.text = "모임 탈퇴하기"
                joinstate = 1
            }else{
                btnJoinClub.setBackgroundResource(R.drawable.button_shape_main)
                btnJoinClub.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                btnJoinClub.text = "모임 가입하기"
                joinstate = 0
            }
        }

        return view
    }


}