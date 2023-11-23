package com.example.senimoapplication.MainPage.fragment_main

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.senimoapplication.Club.Activity_club.ScheduleActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.server.Retrofit.ApiService
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.VO_main.MyScheduleVO
import com.example.senimoapplication.MainPage.adapter_main.MeetingAdapter
import com.example.senimoapplication.MainPage.adapter_main.MyScheduleAdapter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// HomeMainFragment
class HomeMainFragment : Fragment() {

    // 멤버변수로 선언, adapter를 HomeMainFragment 클래스의 멤버 변수로 변수로 선언해야함
    private lateinit var adapter: MeetingAdapter
    private lateinit var myScheduleAdapter: MyScheduleAdapter
    val MeetingList: ArrayList<MeetingVO> = ArrayList()
    val myScheduleList: ArrayList<MyScheduleVO> = ArrayList()
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

        // MyScheduleVO 객체 생성
        // val mySchedule = MyScheduleVO(R.drawable.tea_img,"충장로 먹부림 모임", "일이삼사오육칠팔구십십일십이십삼십사", "2023-11-18T12:00:08.123Z")

        // MyScheduleVO 객체를 리스트에 추가
        // val myScheduleList = ArrayList<MyScheduleVO>()
        // myScheduleList.add(mySchedule)

        // 내 일정 RecyclerView 어댑터 생성 및 설정
        myScheduleAdapter = MyScheduleAdapter(requireContext(), R.layout.myschedule_list, myScheduleList)

        rv_M_MySchedule.adapter = myScheduleAdapter
        rv_M_MySchedule.layoutManager = LinearLayoutManager(requireContext())

        myScheduleAdapter.setOnClickListener(object : MyScheduleAdapter.OnItemClickListener {
            override fun onItemClick(schedule: MyScheduleVO) {
                // 클릭된 아이템에 대한 처리
                val intent = Intent(requireContext(), ScheduleActivity::class.java)
                intent.putExtra("scheduleData", schedule)
                startActivity(intent)
            }
        })

        // 모임 RecyclerView 어댑터 생성 및 설정
        adapter = MeetingAdapter(requireContext(), R.layout.meeting_list, MeetingList)
        rv_M_PopularMeeting.adapter = adapter
        rv_M_PopularMeeting.layoutManager = LinearLayoutManager(requireContext())

        // 모든 모임 목록을 보이게 하기
        adapter.setShowAllItems(true)

        // 모임 가데이터
//        MeetingList.add(MeetingVO("동구","동명동 골프 모임", "같이 골프 합시다~", "운동", 7,20,R.drawable.golf_img.toString()))
//        MeetingList.add(MeetingVO("북구","동명동 티 타임", "우리 같이 차 마셔요~", "취미", 5,10,R.drawable.tea_img.toString()))
//        MeetingList.add(MeetingVO("남구","운암동 수영 모임", "헤엄 헤엄~", "운동", 8,30,R.drawable.tea_img.toString()))
//        MeetingList.add(MeetingVO("광산구","열정 모임!!", "열정만 있다면 모두 가능합니다~", "자기계발", 8,10,R.drawable.tea_img.toString()))
        // adapter.notifyDataSetChanged() // 어댑터 새로고침


        //fetchMeetings() Q: 왜 clickedMeetinghome에서 userId값을 못불러올까
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

                    }
                })
        )


        // 카테고리 클릭 시 SearchActivity로 이동
//        img_M_Excercise.setOnClickListener {
//            startSearchActivity("운동")
//        }
//        img_M_Hobby.setOnClickListener {
//            // val intent = Intent(requireContext(), ClubActivity::class.java)
//            startSearchActivity("취미")
//        }
//        img_M_Concert.setOnClickListener {
//            startSearchActivity("전시","공연")
//        }
//        img_M_Trip.setOnClickListener {
//            startSearchActivity("여행")
//        }
//        img_M_Selfimprovement.setOnClickListener {
//            startSearchActivity("자기계발")
//        }
//        img_M_Financial.setOnClickListener {
//            startSearchActivity("재테크")
//        }

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

        fetchMeetings() // 여기에서 데이터 로딩을 호출합니다.
        fetchLatestSchedule()
    }

    private fun fetchMeetings() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.70.234:3000") // 실제 서버 주소
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val retrofit = Server().retrofit


        // 서버에 요청을 보낼 '전화기'를 만들어요.
        val service = Server(requireContext()).service
        // '전화'를 걸어요. 서버에 데이터를 달라고 요청해요.
        service.getMeetings().enqueue(object : Callback<List<MeetingVO>> {
            // 서버에서 답이 오면 이 부분이 실행돼요.
            override fun onResponse(
                call: Call<List<MeetingVO>>,
                response: Response<List<MeetingVO>>
            ) {
                Log.d("ody1", response.toString())
                // 서버 응답이 null인지 확인합니다.
                if (response.isSuccessful) {
                    Log.d("ody2", response.body().toString())
                    response.body()?.let { meetings ->
                        // null이 아니면 기존 목록을 지우고 새 데이터로 채웁니다.
                        MeetingList.clear()
                        MeetingList.addAll(meetings)
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()// 어댑터에 데이터 변경을 알립니다.

                            Log.d("ody3", MeetingList.toString())
                        }
                    } ?: run {
                        // 응답이 null이면 사용자에게 알려줄 수 있는 방법을 사용하세요.
                        // 예를 들어, Toast 메시지를 표시합니다.
//                        Toast.makeText(context, "모임 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("HomeMainFragment", "모임 정보가 없습니다.")
                    }
                } else {
                    // HTTP 상태 코드가 성공 범위가 아닌 경우 오류 메시지를 표시합니다.
//                    Toast.makeText(context, "요청에 실패했습니다: ${response.code()}", Toast.LENGTH_SHORT).show()
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
        val userId = "T2"
        val service = Server(requireContext()).service
        service.getLatestSchedule(userId).enqueue(object : Callback<List<MyScheduleVO>> {
            override fun onResponse(
                call: Call<List<MyScheduleVO>>,
                response: Response<List<MyScheduleVO>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { latestSchedules ->
                        // 여기서 latestSchedules에 대한 처리를 합니다.
                        // 예: myScheduleList에 데이터 추가 후 어댑터에 알림
                         myScheduleList.clear()
                         myScheduleList.addAll(latestSchedules)
                        if (::myScheduleAdapter.isInitialized) {
                            myScheduleAdapter.notifyDataSetChanged()// 어댑터에 데이터 변경을 알립니다.

                            Log.d("myScheduleList", latestSchedules.toString())
                            Log.d("myScheduleList", myScheduleList.toString())
                        } } ?: run {
                        // 응답이 null이면 사용자에게 알려줄 수 있는 방법을 사용하세요.
                        // 예를 들어, Toast 메시지를 표시합니다.
                        // Toast.makeText(context, "모임 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("HomeMainFragment5", "모임 정보가 없습니다.")
                    }
                }else {
                    // 서버로부터 응답이 있으나, 오류 발생
                    Log.e("HomeMainFragment5", "응답 실패 : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<MyScheduleVO>>, t: Throwable) {
                // 네트워크 요청 실패 시 처리
                Log.e("HomeMainFragment5", "네트워크 요청 실패", t)
            }
        })
    }

}