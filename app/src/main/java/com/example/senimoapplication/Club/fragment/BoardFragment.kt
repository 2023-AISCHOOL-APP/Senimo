package com.example.senimoapplication.Club.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.Activity_club.PostActivity
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Club.VO.getPostResVO
import com.example.senimoapplication.Club.adapter.PostAdapter
import com.example.senimoapplication.Common.PostDeleteListener
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentBoardBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import com.example.senimoapplication.server.Token.UserData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardFragment : Fragment(), PostDeleteListener {
    lateinit var binding: FragmentBoardBinding
    var clickedMeeting: MeetingVO? = null
    private var isScrolling = false
    private var isAtTop = true
    var clubCode: String? = null
    var postList : ArrayList<PostVO> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardBinding.inflate(inflater, container, false)
        val view = binding.root
        clickedMeeting = activity?.intent?.getParcelableExtra<MeetingVO>("clickedMeeting")
        Log.d("보드프래그먼트 받아온값", clickedMeeting.toString())
        val userId = PreferenceManager.getUser(requireContext())?.user_id.toString()

        binding.rvBoard.setOnClickListener {
            binding.imgFloatingNewpost.visibility = INVISIBLE
        }

        // 게시글 데이터 가져오기 함수 실행
        fetchPostList()

        // 작성하기 버튼
        binding.imgFloatingNewpost.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity::class.java)
            intent.putExtra("clickedMeeting", clickedMeeting)
            intent.putExtra("user_id", userId)
            Log.d("보드프래그먼트 회원 아이디", userId)
            Log.d("보드프래그먼트 모임코드", clickedMeeting?.club_code.toString())
            startActivity(intent)
        }

        return view
    }

    override fun onDeletePost() {
        // 삭제 이벤트 발생 시, 여기에서 원하는 작업을 수행합니다.
        // 예: 게시글 목록 다시 가져오기 등
        fetchPostList()
    }

    private fun fetchPostList() {
        clubCode = clickedMeeting?.club_code.toString()
        val service = Server(requireContext()).service
        val call = service.getPostContent(clubCode)
        call.enqueue(object : Callback<getPostResVO> {
            override fun onResponse(call: Call<getPostResVO>, response: Response<getPostResVO>) {
                if (response.isSuccessful) {
                    val postList: List<PostVO>? = response.body()?.data
                    val jsonResponse = Gson().toJson(postList) // Convert to JSON string
                    Log.d("게시글 리스트1", postList.toString())
                    Log.d("게시글 리스트", jsonResponse)
                    if (postList != null) {
                        val postAdapter = PostAdapter(requireContext(), R.layout.post_list, postList, this@BoardFragment,clickedMeeting)
                        binding.rvBoard.adapter = postAdapter
                        binding.rvBoard.layoutManager = LinearLayoutManager(requireContext())
                        binding.tvAnnounceMain.visibility = GONE
                        binding.tvAnnounceSub.visibility = GONE
                    }
                } else {
                    Log.d("BoardFragment", "게시글 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<getPostResVO>, t: Throwable) {
                Log.e("BoardFragment", "서버 통신 실패", t)
            }

        })
    }
}
