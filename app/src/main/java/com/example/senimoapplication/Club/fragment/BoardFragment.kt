package com.example.senimoapplication.Club.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.Activity_club.PostActivity
import com.example.senimoapplication.Club.VO.CommentVO
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Club.adapter.PostAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentBoardBinding


class BoardFragment : Fragment() {
    lateinit var binding: FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardBinding.inflate(inflater, container, false)
        val view = binding.root

        val postList : ArrayList<PostVO> = ArrayList()

        //가데이터
        val post1 = PostVO("국지호", "rtyrt", 1, "2023-11-22T12:00:08.123Z", "게시물 내용입니다", "wer", arrayListOf(
            CommentVO("양희준", "2023-11-22T12:00:08.123Z", "이것이 최선입니까?", "이미지"),
            CommentVO("다른 사용자", "2023-11-22T12:00:08.123Z", "댓글 내용2", "이미지2")
        ), 2)

        val post2 = PostVO("최효정", "rtyrt", 2, "2023-11-22T12:00:08.123Z", "게시물 내용입니다", "wer", arrayListOf(
            CommentVO("양희준", "2023-11-22T12:00:08.123Z", "이것이 최선입니까?", "이미지"),
            CommentVO("다른 사용자", "2023-11-22T12:00:08.123Z", "댓글 내용2", "이미지2")
        ), 2)

        postList.add(post1)
        postList.add(post2)

        binding.rvBoard.setOnClickListener {
            binding.imgFloatingNewpost.visibility = INVISIBLE
        }


        val adapter = PostAdapter(requireContext(), R.layout.post_list, postList)
        binding.rvBoard.adapter = adapter
        binding.rvBoard.layoutManager = LinearLayoutManager(requireContext())


        binding.imgFloatingNewpost.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent)
        }

        // PostVO 리스트가 비어있는 경우 Announce 텍스트를 보여줌
        if (postList.isEmpty()) {
            binding.rvBoard.visibility = View.GONE
            binding.tvAnnounceMain.visibility = View.VISIBLE
            binding.tvAnnounceSub.visibility = View.VISIBLE
        } else {
            binding.rvBoard.visibility = View.VISIBLE
            binding.tvAnnounceMain.visibility = View.GONE
            binding.tvAnnounceSub.visibility = View.GONE
        }



        return view

    }
}
