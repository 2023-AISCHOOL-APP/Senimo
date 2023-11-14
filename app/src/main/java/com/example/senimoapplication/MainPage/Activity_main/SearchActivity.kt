package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.RecyclerItemClickListener
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.MainPage.adapter_main.MeetingAdapter
import com.example.senimoapplication.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    private var MeetingList : ArrayList<MeetingVO>? = null
    private var filteredMeetingList : ArrayList<MeetingVO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Intent에서 MeetingList를 가져옵니다
        MeetingList = intent.getSerializableExtra("MeetingList") as? ArrayList<MeetingVO>

        binding.ImgMSearchIcon.setOnClickListener {
            val searchText = binding.tvMSearchBar.text.toString()

            // 검색 텍스트를 기반으로 MeetingList 필터링
            filteredMeetingList = (MeetingList?.filter { it.title.contains(searchText, ignoreCase = true) } as ArrayList<MeetingVO>?)!!

            if (MeetingList != null && filteredMeetingList!!.isNotEmpty()){
                // 클릭 시, tv_M_CategoryTitle와 recyclerView를 보이도록 설정
                binding.tvMCategoryTitle.visibility = View.VISIBLE
                val recyclerView = view.findViewById<RecyclerView>(R.id.rv_M_CategoryMeeting)
                recyclerView.visibility = View.VISIBLE

                // RecyclerView에 어댑터 설정과 새로고침
                // 필터링된 검색 결과만 나오게 수정함 MeetingList -> filteredMeetingList
                val adapter = MeetingAdapter(applicationContext, R.layout.meeting_list, filteredMeetingList!!)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)

                adapter.notifyDataSetChanged() // 어댑터 새로고침
                binding.tvMCategoryTitle.visibility = View.INVISIBLE

            } else {
                // 검색 결과가 없을 때, 필요한 뷰를 숨김
                binding.tvMCategoryTitle.visibility = View.GONE
                val recyclerView = view.findViewById<RecyclerView>(R.id.rv_M_CategoryMeeting)
                recyclerView.visibility = View.GONE
            }

            // 필터링된 목록에서 항목을 클릭하는 경우
            // 모임 홈 페이지로 이동
            binding.rvMCategoryMeeting.addOnItemTouchListener(
                RecyclerItemClickListener(this@SearchActivity, binding.rvMCategoryMeeting, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                        // MeetingList가 null이 아닐 때만 실행
                        // 클릭된 아이템의 MeetingVO 가져오기
                        filteredMeetingList?.get(position)?.let { clickedSearchPage ->

                            // 새로운 액티비티로 이동
                            val intent = Intent(this@SearchActivity, ClubActivity::class.java)
                            intent.putExtra("clickedSearchPage",clickedSearchPage)
                            startActivity(intent)
                        }


                    }
                })
            )




        }

        binding.ImgMBackbtnToFrag1.setOnClickListener {
            val intent = Intent(this@SearchActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }


    }
}