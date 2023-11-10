package com.example.senimoapplication.MainPage.fragment_main

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
import com.example.senimoapplication.MainPage.Activity_main.SearchActivity
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.VO_main.MyScheduleVO
import com.example.senimoapplication.MainPage.adapter_main.MeetingAdapter
import com.example.senimoapplication.MainPage.adapter_main.MyScheduleAdapter


// HomeMainFragment
class HomeMainFragment : Fragment() {

    val MeetingList : ArrayList<MeetingVO> = ArrayList()

    private var isScrolling = false
     private var isAtTop = true


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
        val rv_M_MySchedule = view.findViewById<RecyclerView>(R.id.rv_M_MySchedule)

        // 카테고리
        val img_M_Excercise = view.findViewById<ImageView>(R.id.img_M_Excercise)
        val img_M_Hobby = view.findViewById<ImageView>(R.id.img_M_Hobby)
        val img_M_Concert = view.findViewById<ImageView>(R.id.img_M_Concert)
        val img_M_Trip = view.findViewById<ImageView>(R.id.img_M_Trip)
        val img_M_Selfimprovement = view.findViewById<ImageView>(R.id.img_M_Selfimprovement)
        val img_M_Financial = view.findViewById<ImageView>(R.id.img_M_Financial)

        // MyScheduleVO 객체 생성
        val mySchedule = MyScheduleVO("충장로 먹부림 모임","일이삼사오육칠팔구십십일십이십삼십사","2023-11-15 09:00")

        // MyScheduleVO 객체를 리스트에 추가
        val myScheduleList = ArrayList<MyScheduleVO>()
        myScheduleList.add(mySchedule)

        // 내 일정 RecyclerView 어댑터 생성 및 설정
        val myScheduleAdapter = MyScheduleAdapter(requireContext(), R.layout.myschedule_list,myScheduleList)
        rv_M_MySchedule.adapter = myScheduleAdapter
        rv_M_MySchedule.layoutManager = LinearLayoutManager(requireContext())


        // 모임 RecyclerView 어댑터 생성 및 설정
        val adapter = MeetingAdapter(requireContext(), R.layout.meeting_list,MeetingList)
        rv_M_PopularMeeting.adapter = adapter
        rv_M_PopularMeeting.layoutManager = LinearLayoutManager(requireContext())


        // 모임 가데이터
        MeetingList.add(MeetingVO("동구","동명동 골프 모임", "같이 골프 합시다~", "운동", 7,20,R.drawable.golf_img.toString()))
        MeetingList.add(MeetingVO("북구","동명동 티 타임", "우리 같이 차 마셔요~", "취미", 5,10,R.drawable.tea_img.toString()))
        MeetingList.add(MeetingVO("남구","운암동 수영 모임", "헤엄 헤엄~", "운동", 8,30,R.drawable.tea_img.toString()))
        MeetingList.add(MeetingVO("광산구","열정 모임!!", "열정만 있다면 모두 가능합니다~", "자기계발", 8,10,R.drawable.tea_img.toString()))
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

        // 카테고리 클릭 시 모임 홈 페이지로 이동
        img_M_Excercise.setOnClickListener {
            val intent = Intent(requireContext(), ClubActivity::class.java)
            startActivity(intent)
        }
        img_M_Hobby.setOnClickListener {
            val intent = Intent(requireContext(), ClubActivity::class.java)
            startActivity(intent)
        }
        img_M_Concert.setOnClickListener {
            val intent = Intent(requireContext(), ClubActivity::class.java)
            startActivity(intent)
        }
        img_M_Trip.setOnClickListener {
            val intent = Intent(requireContext(), ClubActivity::class.java)
            startActivity(intent)
        }
        img_M_Selfimprovement.setOnClickListener {
            val intent = Intent(requireContext(), ClubActivity::class.java)
            startActivity(intent)
        }
        img_M_Financial.setOnClickListener {
            val intent = Intent(requireContext(), ClubActivity::class.java)
            startActivity(intent)
        }




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
                if (sv_M_frag1.scrollY <= 5) {
                    // 스크롤이 최상단에 있는 경우
                    if (!isAtTop) {
                        // 최상단에 오면서 cl_M_ScheduleBar가 숨겨진 경우에만 보이도록 변경
                        cl_M_ScheduleBar.visibility = View.VISIBLE
                        isAtTop = true
                    }
                } else {
                    // 스크롤 중인 경우
                    cl_M_ScheduleBar.visibility = View.GONE
                    isAtTop = false
                }
            } else {
                isScrolling = false
            }
        }


        return view
    }


}