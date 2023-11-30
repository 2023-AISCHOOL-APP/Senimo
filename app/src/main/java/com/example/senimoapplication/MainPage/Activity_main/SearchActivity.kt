package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.adapter_main.MeetingAdapter
import com.example.senimoapplication.databinding.ActivitySearchBinding
import android.os.Parcelable
import android.text.Editable
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintSet

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    lateinit var AllClubList: ArrayList<MeetingVO>
    private var filteredMeetingList: List<MeetingVO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Intent에서 MeetingList와 CategoryKeyword를 받아옴
        AllClubList = intent.getParcelableArrayListExtra<MeetingVO>("AllClubList") ?: ArrayList()
        Log.d("SearchActivity", "받아온 모임 리스트: $AllClubList")

        val categoryKeyword = intent.getStringExtra("CategoryKeyword") ?: ""
        Log.d("SearchActivity", "받아온 카테고리 키워드: $categoryKeyword")


        val isFromSearchBar = intent.getBooleanExtra("isFromSearchBar", false)

        if(!isFromSearchBar) {
            val categoryKeyword = intent.getStringExtra("CategoryKeyword") ?: ""

            binding.tvMCategoryTitle.text = "검색결과"
            binding.tvMCategoryTitle.visibility = View.VISIBLE
        } else {
            binding.tvMCategoryTitle.visibility = View.INVISIBLE
        }

        // 카테고리 키워드에 따라 MeetingList 필터링
        filteredMeetingList = AllClubList.filter { it.keyword == categoryKeyword }

        // tv_M_SearchBar의 android:text 설정
        binding.tvMSearchBar.text = Editable.Factory.getInstance().newEditable(categoryKeyword)

        // 확인을 위한 로그
        for (meeting in AllClubList) {
            Log.d("받아온 AllClubList", "Title: ${meeting.title}, Content: ${meeting.content}, Keyword: ${meeting.keyword}")
        }

//        MeetingList = intent.getParcelableArrayListExtra("MeetingList")!!

        Log.d("받아온 AllClubList 전체", AllClubList.toString())

        // RecyclerView 초기 설정
        val searchRecyclerView = view.findViewById<RecyclerView>(R.id.rv_M_CategoryMeeting)
        val adapter = MeetingAdapter(applicationContext, R.layout.meeting_list, filteredMeetingList)
        searchRecyclerView.adapter = adapter
        searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)



        binding.ImgMSearchIcon.setOnClickListener {
            val searchText = binding.tvMSearchBar.text.toString()

            Log.d("돋보기 클릭 시 정보 받아왔는지 확인", searchText)

            // 검색 텍스트를 기반으로 MeetingList 필터링
            filteredMeetingList =
                AllClubList?.filter { it.title.contains(searchText, ignoreCase = true) }
                    ?: emptyList()

            // 클릭 시, tv_M_CategoryTitle와 recyclerView를 보이도록 설정
            binding.tvMCategoryTitle.visibility =
                if (filteredMeetingList.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            val searchRecyclerView = view.findViewById<RecyclerView>(R.id.rv_M_CategoryMeeting)

            // RecyclerView에 어댑터 설정과 새로고침
            val adapter =
                MeetingAdapter(applicationContext, R.layout.meeting_list, filteredMeetingList)
            searchRecyclerView.adapter = adapter

            searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)

            adapter.notifyDataSetChanged()
            adapter.setShowAllItems(true)
        }

        binding.imgMBackbtnToFrag1.setOnClickListener {
            val intent = Intent(this@SearchActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val intent = Intent(this@SearchActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // val searchRecyclerView = view.findViewById<RecyclerView>(R.id.rv_M_CategoryMeeting)
        searchRecyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this@SearchActivity,
                searchRecyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position != RecyclerView.NO_POSITION && position < filteredMeetingList.size) {
                            val clickedSearchPage = filteredMeetingList[position]
                            Log.d("SearchMeeting",clickedSearchPage.toString())
                            val intent = Intent(this@SearchActivity, ClubActivity::class.java)
                            intent.putExtra("clickedMeeting", clickedSearchPage)
                            startActivity(intent)
                        }
                    }
                })
        )

        setupKeywordTextViews()
    }

    private fun setupKeywordTextViews() {
        binding.tvMKeyword.setOnClickListener {
            updateKeywordVisibility(binding.tvMKeyword, binding.tvMKeyword2, binding.tvMKeyword3)
        }
        binding.tvMKeyword2.setOnClickListener {
            updateKeywordVisibility(binding.tvMKeyword2, binding.tvMKeyword, binding.tvMKeyword3)
        }
        binding.tvMKeyword3.setOnClickListener {
            updateKeywordVisibility(binding.tvMKeyword3, binding.tvMKeyword, binding.tvMKeyword2)
        }
    }

    private fun updateKeywordVisibility(clickedView: TextView, otherView1: TextView, otherView2: TextView) {
        clickedView.visibility = View.GONE

        // ConstraintSet을 사용하여 레이아웃 업데이트
        val constraintSet = ConstraintSet()
        val constraintLayout = binding.constraintLayout
        constraintSet.clone(constraintLayout)

        val marginTop = 10 // marginTop 값

        when (clickedView) {
            otherView1 -> {
                // otherView1을 클릭한 경우
                otherView1.visibility = View.GONE
                constraintSet.connect(otherView2.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(otherView2.id, ConstraintSet.TOP, R.id.tv_M_Locationmeeting, ConstraintSet.BOTTOM, marginTop)
            }
            otherView2 -> {
                // otherView2를 클릭한 경우
                otherView2.visibility = View.GONE
            }
            // otherView3가 없는 경우 아무 동작을 하지 않습니다.
        }

        // 변경된 제약 조건 적용
        constraintSet.applyTo(constraintLayout)

    }

}
