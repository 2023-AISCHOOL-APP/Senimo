package com.example.senimoapplication.MainPage.fragment_main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.MainPage.Activity_main.SearchActivity
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.Club.Activity_club.ScheduleActivity
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.server.Retrofit.ApiService
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.VO_main.MyScheduleVO
import com.example.senimoapplication.MainPage.adapter_main.MeetingAdapter
import com.example.senimoapplication.MainPage.adapter_main.MyScheduleAdapter
import com.example.senimoapplication.server.Token.PreferenceManager

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// HomeMainFragment
class HomeMainFragment : Fragment() {

    // 멤버변수로 선언, adapter를 HomeMainFragment 클래스의 멤버 변수로 변수로 선언해야함
    private lateinit var adapter: MeetingAdapter
    private lateinit var myScheduleAdapter: MyScheduleAdapter
    val MeetingList: ArrayList<MeetingVO> = ArrayList()
    val myScheduleList: ArrayList<ScheduleVO> = ArrayList()
    private lateinit var apiService: ApiService

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


//        if (myScheduleList.size == 0){
//            Img_M_Schecule_Circle.visibility = INVISIBLE
//        } else {
//            Img_M_Schecule_Circle.visibility = VISIBLE
//        }
        // 내 일정 RecyclerView 어댑터 생성 및 설정
        myScheduleAdapter = MyScheduleAdapter(requireContext(), R.layout.myschedule_list, myScheduleList)

        rv_M_MySchedule.adapter = myScheduleAdapter
        rv_M_MySchedule.layoutManager = LinearLayoutManager(requireContext())

        myScheduleAdapter.setOnClickListener(object : MyScheduleAdapter.OnItemClickListener {
            override fun onItemClick(schedule: ScheduleVO) {
                // 클릭된 아이템에 대한 처리
                val intent = Intent(requireContext(), ScheduleActivity::class.java)
                intent.putExtra("ScheduleInfo", schedule)
                intent.putExtra("clubName", schedule.clubName )
                startActivity(intent)
            }
        })

        // 모임 RecyclerView 어댑터 생성 및 설정
        adapter = MeetingAdapter(requireContext(), R.layout.meeting_list, MeetingList)
        rv_M_PopularMeeting.adapter = adapter
        rv_M_PopularMeeting.layoutManager = LinearLayoutManager(requireContext())

        // 모든 모임 목록을 보이게 하기
        adapter.setShowAllItems(true)

        // 메인페이지 인기 모임클릭 시 모임 홈 페이지로 이동
        rv_M_PopularMeeting.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                rv_M_PopularMeeting,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val clickedMeeting  = MeetingList[position] // 클릭된 아이템의 MeetingVO 가져오기
                        // 새로운 액티비티로 이동
                        Log.d("clickedMeetinghome", clickedMeeting.toString())
                        val intent = Intent(requireContext(), ClubActivity::class.java)
                        intent.putExtra("clickedMeeting", clickedMeeting) // Parcelable로 넘김
                        startActivity(intent)
                        activity?.finish()
                    }
                })
        )

        // 카테고리 클릭 시 모임 홈 페이지로 이동
        img_M_Excercise.setOnClickListener {
            val categoryKeyword = "운동"
            // val filteredExerciseMeetingList = MeetingList.filter { it.keyword == categoryKeyword }
            Log.d("SearchActivity", "Category Keyword: $categoryKeyword")
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putParcelableArrayListExtra("MeetingList", ArrayList(MeetingList))
            intent.putExtra("CategoryKeyword", categoryKeyword)
            startActivity(intent)
            activity?.finish()
        }
        img_M_Hobby.setOnClickListener {
            val categoryKeyword = "취미"
            // val filteredExerciseMeetingList = MeetingList.filter { it.keyword == categoryKeyword }
            Log.d("SearchActivity", "Category Keyword: $categoryKeyword")
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putParcelableArrayListExtra("MeetingList", ArrayList(MeetingList))
            intent.putExtra("CategoryKeyword", categoryKeyword)
            startActivity(intent)
            activity?.finish()
        }
        img_M_Concert.setOnClickListener {
            val categoryKeyword = "전시/공연"
            // val filteredExerciseMeetingList = MeetingList.filter { it.keyword == categoryKeyword }
            Log.d("SearchActivity", "Category Keyword: $categoryKeyword")
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putParcelableArrayListExtra("MeetingList", ArrayList(MeetingList))
            intent.putExtra("CategoryKeyword", categoryKeyword)
            startActivity(intent)
            activity?.finish()
        }
        img_M_Trip.setOnClickListener {
            val categoryKeyword = "여행"
            // val filteredExerciseMeetingList = MeetingList.filter { it.keyword == categoryKeyword }
            Log.d("SearchActivity", "Category Keyword: $categoryKeyword")
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putParcelableArrayListExtra("MeetingList", ArrayList(MeetingList))
            intent.putExtra("CategoryKeyword", categoryKeyword)
            startActivity(intent)
            activity?.finish()
        }
        img_M_Selfimprovement.setOnClickListener {
            val categoryKeyword = "자기계발"
            // val filteredExerciseMeetingList = MeetingList.filter { it.keyword == categoryKeyword }
            Log.d("SearchActivity", "Category Keyword: $categoryKeyword")
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putParcelableArrayListExtra("MeetingList", ArrayList(MeetingList))
            intent.putExtra("CategoryKeyword", categoryKeyword)
            startActivity(intent)
            activity?.finish()
        }
        img_M_Financial.setOnClickListener {
            val categoryKeyword = "재테크"
            // val filteredExerciseMeetingList = MeetingList.filter { it.keyword == categoryKeyword }
            Log.d("SearchActivity", "Category Keyword: $categoryKeyword")
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putParcelableArrayListExtra("MeetingList", ArrayList(MeetingList))
            intent.putExtra("CategoryKeyword", categoryKeyword)
            startActivity(intent)
            activity?.finish()
        }

        // 검색바 이부분은 전체모임정보가 들어가게 수정해야함(따로요청을 보내야하나?)
        Img_M_SearchBar.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putParcelableArrayListExtra("MeetingList", ArrayList(MeetingList))  // HomeMainFragment에서 MeetingList를 Parcel로 만들어 SearchActivity로 전달
            Log.d("검색바",MeetingList.toString())
            intent.putExtra("isFromSearchBar", true) // 검색 바에서 온 것임을 나타내는 플래그
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchMeetings()
        fetchLatestSchedule()
    }

    private fun fetchMeetings() {
        val service = Server(requireContext()).service
        service.getMeetings().enqueue(object : Callback<List<MeetingVO>> {

            override fun onResponse(
                call: Call<List<MeetingVO>>,
                response: Response<List<MeetingVO>>
            ) {
                Log.d("ody1", response.toString())

                if (response.isSuccessful) {
                    Log.d("ody2", response.body().toString())
                    response.body()?.let { meetings ->
                        // 서버로부터 받은 목록의 처음 10개 아이템만 선택
                        val limitedList = meetings.take(10)
                        MeetingList.clear()
                        MeetingList.addAll(limitedList)
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()

                            Log.d("ody3", MeetingList.toString())
                        }
                    } ?: run {

                        Log.d("HomeMainFragment", "모임 정보가 없습니다.")
                    }
                } else {

                    Log.e("HomeMainFragment", "통신은 성공했으나 요청에 실패했습니다: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<MeetingVO>>, t: Throwable) {
                // 네트워크 요청 실패 시 처리
                Log.e("HomeMainFragment", "네트워크 요청 실패", t)
            }
        })
    }

    private fun fetchLatestSchedule() {
        val userData = PreferenceManager.getUser(requireContext())
        val userId =  userData?.user_id
        val service = Server(requireContext()).service
        service.getLatestSchedule(userId).enqueue(object : Callback<List<ScheduleVO>> {
            override fun onResponse(
                call: Call<List<ScheduleVO>>,
                response: Response<List<ScheduleVO>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { latestSchedules ->

                        myScheduleList.clear()
                        myScheduleList.addAll(latestSchedules)
                        if (::myScheduleAdapter.isInitialized) {
                            myScheduleAdapter.notifyDataSetChanged()

                            Log.d("myScheduleList", latestSchedules.toString())
                            Log.d("myScheduleList", myScheduleList.toString())
                        } } ?: run {
                        Log.d("myScheduleList", "모임 정보가 없습니다.")
                    }
                }else {

                    Log.e("HomeMainFragment5", "응답 실패 : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ScheduleVO>>, t: Throwable) {
                Log.e("HomeMainFragment5", "네트워크 요청 실패", t)
            }
        })
    }

}