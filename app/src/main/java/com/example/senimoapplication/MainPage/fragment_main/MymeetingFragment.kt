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
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.adapter.ScheduleAdapter
import com.example.senimoapplication.MainPage.Activity_main.CreateMeetingActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.adapter_main.MeetingAdapter
import com.example.senimoapplication.R

// val moimlist : List<ScheduleVO>
class MymeetingFragment(private val myschedulList : List<ScheduleVO>, val joinList : List<MeetingVO>, val interestList : List<MeetingVO> ) : Fragment() {

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
        val Img_M_Meeting_Shcedule_more = view.findViewById<ImageView>(R.id.Img_M_Meeting_Shcedule_more)

        // 가입한 모임, 더보기
        val rv_M_Meeting_join = view.findViewById<RecyclerView>(R.id.rv_M_Meeting_join)
        val Img_M_Meeting_join_more = view.findViewById<ImageView>(R.id.Img_M_Meeting_join_more)

        // 관심 모임, 더보기
        val rv_M_Meeting_interest = view.findViewById<RecyclerView>(R.id.rv_M_Meeting_interest)
        val Img_M_Meeting_interest_more = view.findViewById<ImageView>(R.id.Img_M_Meeting_interest_more)


        // 모임 일정, 가입한 모임, 관심 모임 어댑터
        val myschedule_adapter = ScheduleAdapter(requireContext(), R.layout.schedule_list, myschedulList)
        val join_adapter = MeetingAdapter(requireContext(),R.layout.meeting_list, joinList)
        val interest_adapter = MeetingAdapter(requireContext(),R.layout.meeting_list, interestList)

        // RecyclerView에 어댑터 설정
        rv_M_Meeting_Schedule.adapter = myschedule_adapter
        rv_M_Meeting_join.adapter = join_adapter
        rv_M_Meeting_interest.adapter = interest_adapter

        // RecyclerView의 레이아웃 매니저 설정
        rv_M_Meeting_Schedule.layoutManager = LinearLayoutManager(view.context)
        rv_M_Meeting_join.layoutManager = LinearLayoutManager(view.context)
        rv_M_Meeting_interest.layoutManager = LinearLayoutManager(view.context)


        Img_M_Creatmeet_Circle.setOnClickListener {
            val intent = Intent(requireContext(), CreateMeetingActivity::class.java)
            startActivity(intent)
            // activity?.finish()
        }

        Img_M_Meeting_Shcedule_more.setOnClickListener {
            showAllItems = !showAllItems // 플래그 업데이트
            myschedule_adapter.setShowAllItems(showAllItems) // 어댑터에 플래그 전달

            // 더보기 버튼 숨김 처리
            Img_M_Meeting_Shcedule_more.visibility = if (showAllItems) View.INVISIBLE else View.VISIBLE
        }

        Img_M_Meeting_join_more.setOnClickListener {
            showAllItems = !showAllItems // 플래그 업데이트
            join_adapter.setShowAllItems(showAllItems) // 어댑터에 플래그 전달

            // 더보기 버튼 숨김 처리
            Img_M_Meeting_join_more.visibility = if (showAllItems) View.INVISIBLE else View.VISIBLE
        }

        Img_M_Meeting_interest_more.setOnClickListener {
            showAllItems = !showAllItems // 플래그 업데이트
            interest_adapter.setShowAllItems(showAllItems) // 어댑터에 플래그 전달

            // 더보기 버튼 숨김 처리
            Img_M_Meeting_interest_more.visibility = if (showAllItems) View.INVISIBLE else View.VISIBLE
        }

        return view
    }


}
