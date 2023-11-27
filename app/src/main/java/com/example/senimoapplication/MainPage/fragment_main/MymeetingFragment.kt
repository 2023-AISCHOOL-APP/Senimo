package com.example.senimoapplication.MainPage.fragment_main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.adapter.ScheduleAdapter
import com.example.senimoapplication.MainPage.Activity_main.CreateMeetingActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.adapter_main.MeetingAdapter
import com.example.senimoapplication.R

class MymeetingFragment(private val myscheduleList : List<ScheduleVO>, val joinList : List<MeetingVO>, val interestList : List<MeetingVO> ) : Fragment() {

    private var showAllItems = false // 플래그 추가
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mymeeting, container, false)

        val Img_M_Creatmeet_Circle = view.findViewById<ImageView>(R.id.Img_M_Creatmeet_Circle) // 모임 생성 버튼

        // 모임 일정 , 더보기
        val rv_M_Meeting_Schedule = view.findViewById<RecyclerView>(R.id.rv_M_Meeting_Schedule)
        val Img_M_Meeting_Shcedule_more = view.findViewById<ImageView>(R.id.Img_M_Meeting_Schedule_more)
        val img_M_Meeting_Schedule_close = view.findViewById<ImageView>(R.id.img_M_Meeting_Schedule_close)

        // 가입한 모임, 더보기
        val rv_M_Meeting_join = view.findViewById<RecyclerView>(R.id.rv_M_Meeting_join)
        val Img_M_Meeting_join_more = view.findViewById<ImageView>(R.id.Img_M_Meeting_join_more)
        val Img_M_Meeting_join_close = view.findViewById<ImageView>(R.id.Img_M_Meeting_join_close)

        // 관심 모임, 더보기
        val rv_M_Meeting_interest = view.findViewById<RecyclerView>(R.id.rv_M_Meeting_interest)
        val Img_M_Meeting_interest_more = view.findViewById<ImageView>(R.id.Img_M_Meeting_interest_more)
        val Img_M_Meeting_interest_close = view.findViewById<ImageView>(R.id.Img_M_Meeting_interest_close)


        // 모임 일정, 가입한 모임, 관심 모임 어댑터
        val myschedule_adapter = ScheduleAdapter(requireContext(), R.layout.schedule_list, myscheduleList)
        val join_adapter = MeetingAdapter(requireContext(),R.layout.meeting_list, joinList)
        val interest_adapter = MeetingAdapter(requireContext(),R.layout.meeting_list, interestList)

        // RecyclerView에 어댑터 설정
        rv_M_Meeting_Schedule.adapter = myschedule_adapter
        rv_M_Meeting_join.adapter = join_adapter
        rv_M_Meeting_interest.adapter = interest_adapter

        // 최대 2개의 모임 목록만 보이게 하기
        myschedule_adapter.setShowAllItems(false)
        join_adapter.setShowAllItems(false)
        interest_adapter.setShowAllItems(false)

        // RecyclerView의 레이아웃 매니저 설정
        rv_M_Meeting_Schedule.layoutManager = LinearLayoutManager(view.context)
        rv_M_Meeting_join.layoutManager = LinearLayoutManager(view.context)
        rv_M_Meeting_interest.layoutManager = LinearLayoutManager(view.context)

        // 각 목록 항목에 클릭 리스너 추가
        myschedule_adapter.setOnItemClickListener(object : ScheduleAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                // 해당 항목의 데이터 가져오기
                val clickedSchedule = myscheduleList[position]

                // ClubActivity로 데이터 전달을 위한 Intent 생성
                val intent = Intent(requireContext(), ClubActivity::class.java)

                // Intent에 데이터 추가
                // intent.putExtra("scheduleId", clickedSchedule.id)

                // ClubActivity 시작
                startActivity(intent)
            }
        })

        // 각 목록 항목에 클릭 리스너 추가
        join_adapter.setOnItemClickListener(object : MeetingAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                // 해당 항목의 데이터 가져오기
                val clickedJoin = joinList[position]

                // ClubActivity로 데이터 전달을 위한 Intent 생성
                val intent = Intent(requireContext(), ClubActivity::class.java)

                // Intent에 데이터 추가
                intent.putExtra("meetingId", clickedJoin)

                // ClubActivity 시작
                startActivity(intent)
            }
        })

        // 각 목록 항목에 클릭 리스너 추가
        interest_adapter.setOnItemClickListener(object : MeetingAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                // 해당 항목의 데이터 가져오기
                val clickedInterest = interestList[position]

                // ClubActivity로 데이터 전달을 위한 Intent 생성
                val intent = Intent(requireContext(), ClubActivity::class.java)

                // Intent에 데이터 추가
                intent.putExtra("meetingId", clickedInterest)

                // ClubActivity 시작
                startActivity(intent)
            }
        })


        Img_M_Creatmeet_Circle.setOnClickListener {
            val intent = Intent(requireContext(), CreateMeetingActivity::class.java)
            startActivity(intent)
            // activity?.finish()
        }

        Img_M_Meeting_Shcedule_more.setOnClickListener {
            showAllItems = true // 모든 항목 표시
            myschedule_adapter.setShowAllItems(showAllItems) // 어댑터 업데이트

            // 버튼 가시성 업데이트
            Img_M_Meeting_Shcedule_more.visibility = View.INVISIBLE
            img_M_Meeting_Schedule_close.visibility = View.VISIBLE
        }

        img_M_Meeting_Schedule_close.setOnClickListener {
            showAllItems = false // 두 개의 항목만 표시
            myschedule_adapter.setShowAllItems(showAllItems) // 어댑터 업데이트

            // 버튼 가시성 업데이트
            img_M_Meeting_Schedule_close.visibility = View.INVISIBLE
            Img_M_Meeting_Shcedule_more.visibility = View.VISIBLE
        }

        Img_M_Meeting_join_more.setOnClickListener {
            showAllItems = true // 플래그 업데이트
            join_adapter.setShowAllItems(showAllItems) // 어댑터에 플래그 전달

            // 더보기 버튼 숨김 처리
            Img_M_Meeting_join_more.visibility = View.INVISIBLE
            Img_M_Meeting_join_close.visibility = View.VISIBLE
        }

        Img_M_Meeting_join_close.setOnClickListener {
            showAllItems = false // 플래그 업데이트
            join_adapter.setShowAllItems(showAllItems) // 어댑터에 플래그 전달

            // 더보기 버튼 숨김 처리
            Img_M_Meeting_join_close.visibility = View.INVISIBLE
            Img_M_Meeting_join_more.visibility = View.VISIBLE
        }

        Img_M_Meeting_interest_more.setOnClickListener {
            showAllItems = true // 플래그 업데이트
            interest_adapter.setShowAllItems(showAllItems) // 어댑터에 플래그 전달

            // 더보기 버튼 숨김 처리
            Img_M_Meeting_interest_more.visibility = View.INVISIBLE
            Img_M_Meeting_interest_close.visibility = View.VISIBLE
        }

        Img_M_Meeting_interest_close.setOnClickListener {
            showAllItems = false // 플래그 업데이트
            interest_adapter.setShowAllItems(showAllItems) // 어댑터에 플래그 전달

            // 더보기 버튼 숨김 처리
            Img_M_Meeting_interest_close.visibility = View.INVISIBLE
            Img_M_Meeting_interest_more.visibility = View.VISIBLE
        }

        return view
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // 호기심탐험가
//        // 관심 모임 개수 확인
//        val interestedClubsCount = interestList.size
//
//        // 사용자 ID
//        val userId = "" // 현재 사용자의 ID를 얻는 방법에 따라 달라짐
//
//        // 관심 모임 개수가 5개 이상이면 BadgeManager의 함수 호출
//        if (interestedClubsCount >= 5) {
//            BadgeManagerSingleton.instance.updateForInterestedClubs(userId, interestedClubsCount)
//        }
//
//
//        // 새싹모임러, 이구역모임왕
//        val joinedMeetingCount = joinList.size
//
//        // 가입한 모임 개수에 따라 BadgeManager의 함수 호출
//        val isFirstJoin = joinedMeetingCount == 1 // 첫 번째 가입인 경우
//        if (isFirstJoin) {
//            BadgeManagerSingleton.instance.updateForNewJoin(userId, joinedMeetingCount, isFirstJoin)
//        }
//
//        if (joinedMeetingCount >= 5) {
//            BadgeManagerSingleton.instance.updateForNewJoin(userId, joinedMeetingCount, false)
//        }
//    }


}
