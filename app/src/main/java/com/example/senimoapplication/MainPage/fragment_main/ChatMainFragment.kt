package com.example.senimoapplication.MainPage.fragment_main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.MainPage.adapter_main.ChatListAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.MainPage.VO_main.ChatListVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ChatMainFragment : Fragment() {

    private val chatList = mutableListOf<ChatListVO>() // ChatListVO 객체를 담을 리스트
    private lateinit var chatListAdapter: ChatListAdapter
    var chatListVO: ArrayList<ChatListVO> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_main, container, false)

        // 현재 시간에 가까운 ISO 8601 형식의 문자열 생성
        val currentIsoTimeSting = generateCurrentIsoTimeString()

        chatListAdapter = ChatListAdapter(requireContext(), R.layout.chat_list, chatList)
        // RecyclerView 설정
        val rv_M_ChatList = view.findViewById<RecyclerView>(R.id.rv_M_ChatList)
        rv_M_ChatList.adapter = chatListAdapter
        rv_M_ChatList.layoutManager = LinearLayoutManager(requireContext())

        loadChatRooms()
        // RecyclerView 아이템 클릭 시 처리
        chatListAdapter.setOnItemClickListener(object : ChatListAdapter.OnItemClickListener {
            override fun onItemClick(chatListVO: ChatListVO) {
                // 클릭된 아이템의 정보를 사용하여 ChatFragment로 이동 또는 다른 처리를 수행
                // ChatFragment로 이동
                (requireActivity() as MainActivity).navigateToChatFragment(chatListVO)

                // ChatFragment로 이동되었는지 확인
                Log.d("ChatMainFragment", "ChatFragment added to fl_ChatRoom")
            }
        })

        return view
    }

    private fun loadChatRooms() {
        val userId =PreferenceManager.getUser(requireContext())?.user_id
        val service = Server(requireContext()).service
        if (userId != null) {
            service.getChatRooms(userId).enqueue(object : Callback<ArrayList<ChatListVO>> {
                override fun onResponse(call: Call<ArrayList<ChatListVO>>, response: Response<ArrayList<ChatListVO>>) {
                    if (response.isSuccessful) {
                        //Log.d("ChatMainFragment", "loadChatRooms 실행은 됨")
                        chatListVO = response.body()!!
                        Log.d("ChatMainFragment1", response.body().toString())
                        val jsonResponse = Gson().toJson(response.body())
                        Log.d("ChatMainFragment 받아온값", jsonResponse.toString())
                        if (response.body() != null) {
                            chatList.clear()
                            chatList.addAll(response.body()!!)
                            if (::chatListAdapter.isInitialized) {
                                chatListAdapter.notifyDataSetChanged()
                            }
                        }
                    } else {
                        // 에러 처리
                    }
                }

                override fun onFailure(call: Call<ArrayList<ChatListVO>>, t: Throwable) {
                    // 통신 실패 처리
                }
            })
        }
    }
}

fun generateCurrentIsoTimeString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(Date())
}