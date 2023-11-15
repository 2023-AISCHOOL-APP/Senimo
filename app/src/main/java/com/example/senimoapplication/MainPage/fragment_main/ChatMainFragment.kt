package com.example.senimoapplication.MainPage.fragment_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.MainPage.VO_main.ChatListVO
import com.example.senimoapplication.MainPage.adapter_main.ChatListAdapter
import com.example.senimoapplication.R

class ChatMainFragment : Fragment() {

    private val chatList = mutableListOf<ChatListVO>() // ChatListVO 객체를 담을 리스트


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_main, container, false)

        // chatList에 ChatListVO 데이터를 추가
        chatList.add(ChatListVO("user1_profile.jpg", "모임 제목 1", "오늘 다같이 모여서 대화도 하고 맛있는 것도 먹으러 가나요?", System.currentTimeMillis() - 1000 * 60 * 5, 3))
        chatList.add(ChatListVO("user2_profile.jpg", "모임 제목 2", "저기요!", System.currentTimeMillis() - 1000 * 60 * 30, 1))

        // RecyclerView 설정
        val rv_M_ChatList = view.findViewById<RecyclerView>(R.id.rv_M_ChatList)
        rv_M_ChatList.layoutManager = LinearLayoutManager(requireContext())
        val chatListAdapter = ChatListAdapter(requireContext(), R.layout.chat_list, chatList)
        rv_M_ChatList.adapter = chatListAdapter


        return view
    }


}