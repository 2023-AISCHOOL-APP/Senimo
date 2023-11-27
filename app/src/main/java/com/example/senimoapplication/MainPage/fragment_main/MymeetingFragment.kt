package com.example.senimoapplication.MainPage.fragment_main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.Club.Activity_club.ScheduleActivity
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.Club.adapter.ScheduleAdapter
import com.example.senimoapplication.MainPage.Activity_main.CreateMeetingActivity
import com.example.senimoapplication.MainPage.VO_main.CombinedDataResVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.adapter_main.MeetingAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.MeetingListBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MymeetingFragment() : Fragment() {

    private var showAllItems = false // 플래그 추가
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 로그인한 사용자 정보 불러오기
        val userData = PreferenceManager.getUser(requireContext())
        val userId = userData?.user_id


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

        if(userId != null){
            val server = Server(requireContext())
            MyMeetingsManager(server).getCombinedData(userId, object : Callback<CombinedDataResVO> {
                override fun onResponse(
                    call: Call<CombinedDataResVO>, response: Response<CombinedDataResVO>) {
                    Log.d("getCombinedData", "통신 시작: ")
                    if(response.isSuccessful){
                        val fetchdata = response.body()
                        Log.d("getCombinedData", "${fetchdata}")
                        val myscheduleList: List<ScheduleVO>? = fetchdata?.mySchedule
                        val joinList: List<MeetingVO>? = fetchdata?.myClub
                        val interestList: List<MeetingVO>? = fetchdata?.myInterestedClub

                        // 모임 일정, 가입한 모임, 관심 모임 리사이클러뷰 연결하기
                        if (myscheduleList != null) {
                            val myschedule_adapter = ScheduleAdapter(requireContext(), R.layout.schedule_list, myscheduleList)
                            rv_M_Meeting_Schedule.adapter = myschedule_adapter
                            myschedule_adapter.setShowAllItems(false)
                            rv_M_Meeting_Schedule.layoutManager = LinearLayoutManager(view.context)
                            myschedule_adapter.setOnItemClickListener(object : ScheduleAdapter.OnItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val clickedSchedule = myscheduleList[position]
                                    val intent = Intent(requireContext(), ScheduleActivity::class.java)
                                    intent.putExtra("ScheduleInfo", clickedSchedule)
                                    startActivity(intent)
                                }
                            })

                            // 더보기 버튼
                            img_M_Meeting_Schedule_close.setOnClickListener {
                                showAllItems = false // 두 개의 항목만 표시
                                myschedule_adapter.setShowAllItems(showAllItems) // 어댑터 업데이트
                                img_M_Meeting_Schedule_close.visibility = View.INVISIBLE
                                Img_M_Meeting_Shcedule_more.visibility = View.VISIBLE
                            }
                            Img_M_Meeting_Shcedule_more.setOnClickListener {
                                showAllItems = true // 모든 항목 표시
                                myschedule_adapter.setShowAllItems(showAllItems) // 어댑터 업데이트
                                Img_M_Meeting_Shcedule_more.visibility = View.INVISIBLE
                                img_M_Meeting_Schedule_close.visibility = View.VISIBLE
                            }
                        }
                        if(joinList != null){
                            val join_adapter = MeetingAdapter(requireContext(),R.layout.meeting_list, joinList)
                            rv_M_Meeting_join.adapter = join_adapter
                            join_adapter.setShowAllItems(false)
                            rv_M_Meeting_join.layoutManager = LinearLayoutManager(view.context)
                            join_adapter.setOnItemClickListener(object : MeetingAdapter.OnItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val clickedMeeting = joinList[position]
                                    val intent = Intent(requireContext(), ClubActivity::class.java)
                                    intent.putExtra("clickedMeeting", clickedMeeting)
                                    startActivity(intent)
                                }
                            })

                            // 더보기 버튼
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
                        }

                        if(interestList != null){
                            val interest_adapter = MeetingAdapter(requireContext(),R.layout.meeting_list, interestList)
                            rv_M_Meeting_interest.adapter = interest_adapter
                            interest_adapter.setShowAllItems(false)
                            rv_M_Meeting_interest.layoutManager = LinearLayoutManager(view.context)
                            interest_adapter.setOnItemClickListener(object : MeetingAdapter.OnItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val clickedMeeting = interestList[position]
                                    val intent = Intent(requireContext(), ClubActivity::class.java)
                                    intent.putExtra("clickedMeeting", clickedMeeting)
                                    startActivity(intent)
                                }
                            })

                            //더보기 버튼
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
                        }
                    }
                }

                override fun onFailure(call: Call<CombinedDataResVO>, t: Throwable) {
                    Log.d("getCombinedData", "스택 트레이스: ", t)
                }
            })
        }

        Img_M_Creatmeet_Circle.setOnClickListener {
            val intent = Intent(requireContext(), CreateMeetingActivity::class.java)
            startActivity(intent)
            // activity?.finish()
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

class MyMeetingsManager(private val server: Server) {
    // 내가 참가하는 일정 데이터 가져오기
    fun getCombinedData(userId: String?, callback: Callback<CombinedDataResVO>){
        val call = server.service.getCombinedData(userId)
        call.enqueue(callback)
    }
}