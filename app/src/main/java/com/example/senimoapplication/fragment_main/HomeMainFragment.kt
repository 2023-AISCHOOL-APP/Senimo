package com.example.senimoapplication.fragment_main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Activity_main.SearchActivity
import com.example.senimoapplication.ClubActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.RecyclerItemClickListener
import com.example.senimoapplication.VO_main.MeetingVO
import com.example.senimoapplication.adapter_main.MeetingAdapter
import com.example.senimoapplication.fragment.HomeFragment


// HomeMainFragment
class HomeMainFragment : Fragment() {

    val MeetingList : ArrayList<MeetingVO> = ArrayList()
    private  var isScrolling = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // RecyclerView 구현하기
        val view = inflater.inflate(R.layout.fragment_home_main, container, false)

        val rv_M_PopularMeeting = view.findViewById<RecyclerView>(R.id.rv_M_PopularMeeting)
        val Img_M_SearchBar = view.findViewById<ImageView>(R.id.Img_M_SearchBar)
        val Img_M_Schecule_Circle = view.findViewById<ImageView>(R.id.Img_M_Schecule_Circle)
        val cl_M_ScheduleBar = view.findViewById<ConstraintLayout>(R.id.cl_M_ScheduleBar)
        val sv_M_frag1 = view.findViewById<ScrollView>(R.id.sv_M_frag1)


        val adapter = MeetingAdapter(requireContext(), R.layout.meeting_list,MeetingList)
        rv_M_PopularMeeting.adapter = adapter
        rv_M_PopularMeeting.layoutManager = LinearLayoutManager(requireContext())

        // 모임 가데이터
        MeetingList.add(MeetingVO("동명동 골프 모임", "같이 골프 합시다~", "골프", 7,20,R.drawable.golf_img.toString()))
        MeetingList.add(MeetingVO("동명동 티 타임", "우리 같이 차 마셔요~", "카페", 5,10,R.drawable.tea_img.toString()))
        MeetingList.add(MeetingVO("운암동 수영 모임", "헤엄 헤엄~", "수영", 8,30,R.drawable.tea_img.toString()))
        MeetingList.add(MeetingVO("열정 모임!!", "열정만 있다면 모두 가능합니다~", "도전", 8,10,R.drawable.tea_img.toString()))
        adapter.notifyDataSetChanged() // 어댑터 새로고침

        // 모임 홈 페이지로 이동
        rv_M_PopularMeeting.addOnItemTouchListener(
            RecyclerItemClickListener(requireContext(), rv_M_PopularMeeting, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val clickedMeetinghome = MeetingList[position] // 클릭된 아이템의 MeetingVO 가져오기
                    // 새로운 액티비티로 이동
                    val intent = Intent(requireContext(), ClubActivity::class.java)
                    intent.putExtra("clickedMeetinghome",clickedMeetinghome)
                    startActivity(intent)
                }
            })
        )




        Img_M_SearchBar.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)

            intent.putExtra("MeetingList", MeetingList)

            startActivity(intent)
            activity?.finish()

        }

        Img_M_Schecule_Circle.setOnClickListener {
            cl_M_ScheduleBar.visibility = View.VISIBLE
        }




        // 스크롤 중에 터치 이벤트가 감지될 때
        sv_M_frag1.setOnTouchListener { _, _ ->
            isScrolling = true
            cl_M_ScheduleBar.visibility = View.GONE
            false
        }

        // 스크롤 중일 때
        sv_M_frag1.viewTreeObserver.addOnScrollChangedListener {
            if (!isScrolling) {
                cl_M_ScheduleBar.visibility = View.GONE
            } else {
                isScrolling = false
            }
        }


        return view
    }


}