package com.example.senimoapplication.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.MainActivity
import com.example.senimoapplication.MemberAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.RecyclerItemClickListener
import com.example.senimoapplication.ScheduleActivity
import com.example.senimoapplication.ScheduleAdapter
import com.example.senimoapplication.VO.MemberVO
import com.example.senimoapplication.VO.ScheduleVO

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

        // 일정 리사이클러뷰 어댑터 연결
        rvSchedule.adapter = s_adapter
        rvSchedule.layoutManager=LinearLayoutManager(view.context)

        scheduleList.add(ScheduleVO("양희준과 아이들","신나는 크리스마스 파티","2023.12.20(수) 17:00","3만원", "광주 동구 제봉로 대성학원 3층", "32",26,20,"모집중" ))
        scheduleList.add(ScheduleVO("양희준과 아이들","신나는 크리스마스 파티","2023.12.20(수) 17:00","3만원", "광주 동구 제봉로 대성학원 3층", "32",26,20,"모집중" ))

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



        // 전체 회원 리사이클러뷰 어탭터 연결
        rvMember.adapter = m_adapter
        rvMember.layoutManager=LinearLayoutManager(view.context)
        // 전체 회원 목록 가데이터
        memberList.add(MemberVO("양희준", "모임장", R.drawable.img_sample))
        memberList.add(MemberVO("최효정", "운영진", R.drawable.img_sample))
        memberList.add(MemberVO("국지호", "", R.drawable.img_sample))
        memberList.add(MemberVO("김도운", "", R.drawable.img_sample))
        memberList.add(MemberVO("이지혜", "", R.drawable.img_sample))
        memberList.add(MemberVO("나예호", "", R.drawable.img_sample))

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


        return view
    }

}