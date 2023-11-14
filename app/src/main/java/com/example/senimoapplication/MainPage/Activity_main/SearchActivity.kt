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

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    lateinit var meetingList: ArrayList<MeetingVO>
    private var filteredMeetingList: List<MeetingVO> = ArrayList()

    // 로그 메세지 태그
    companion object {
        private const val TAG = "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Intent에서 MeetingList를 받아옴
        meetingList = intent.getParcelableArrayListExtra("MeetingList") ?: ArrayList()

        // 확인을 위한 로그
        for (meeting in meetingList) {
            Log.d("Meetingtest1", "Title: ${meeting.title}, Content: ${meeting.content}, Keyword: ${meeting.keyword}")
        }

//        MeetingList = intent.getParcelableArrayListExtra("MeetingList")!!

        Log.d("test3", meetingList.toString())


        binding.ImgMSearchIcon.setOnClickListener {
            val searchText = binding.tvMSearchBar.text.toString()

            Log.d("test1", searchText)

            // 검색 텍스트를 기반으로 MeetingList 필터링
            filteredMeetingList =
                meetingList?.filter { it.title.contains(searchText, ignoreCase = true) }
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
        }

        binding.ImgMBackbtnToFrag1.setOnClickListener {
            val intent = Intent(this@SearchActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val searchRecyclerView = view.findViewById<RecyclerView>(R.id.rv_M_CategoryMeeting)
        searchRecyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this@SearchActivity,
                searchRecyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position != RecyclerView.NO_POSITION && position < filteredMeetingList.size) {
                            val clickedSearchPage = filteredMeetingList[position]
                            val intent = Intent(this@SearchActivity, ClubActivity::class.java)
                            intent.putExtra("clickedSearchPage", clickedSearchPage)
                            startActivity(intent)
                        }
                    }
                })
        )
    }
}
