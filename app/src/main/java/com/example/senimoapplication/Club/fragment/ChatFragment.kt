package com.example.senimoapplication.Club.fragment

import com.example.senimoapplication.MainPage.VO_main.ChatListVO
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.VO.ChatVO
import com.example.senimoapplication.Club.adapter.ChatAdapter
import com.example.senimoapplication.Club.adapter.GalleryAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentBoardBinding
import com.example.senimoapplication.databinding.FragmentChatBinding
import com.example.senimoapplication.server.SocketManager
import com.example.senimoapplication.server.Token.PreferenceManager
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    val chatMessageList : ArrayList<ChatVO> = ArrayList()
    private lateinit var adapter: ChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) //// 채팅 위로 안올라게 하는 코드

        // 가데이터
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample3", "asdf","김도운","2023-11-22T12:00:08.123Z", "배고프네요... 이제 점심먹으러 갈까여?!!!"))
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample2", "qwer","국지호","2023-11-22T12:00:08.123Z", "뭐 먹으러가실?"))
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample3", "asdf","김도운","2023-11-22T12:00:08.123Z", "형 뭐가 땡기십니까?"))
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample2", "qwer","국지호","2023-11-22T12:00:08.123Z", "인사과장.. 정해 놓은 거 아니었어?"))
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample3", "asdf","김도운","2023-11-22T12:00:08.123Z", "형님이 오늘 드시고 싶은 메뉴로 모시려고 했습니돴!"))
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample2", "qwer","국지호","2023-11-22T12:00:08.123Z", "비도오는데 국밥?"))
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample3", "asdf","김도운","2023-11-22T12:00:08.123Z", "저도 좋습니다 형님"))
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample2", "qwer","국지호","2023-11-22T12:00:08.123Z", "쉬는시간에 바로 나와"))
//        chatMessageList.add(ChatVO("1","R.drawable.img_sample3", "asdf","김도운","2023-11-22T12:00:08.123Z", "넵!"))

        adapter = ChatAdapter(requireContext(), R.layout.chat_message_item, chatMessageList)
        binding.rvChatting.adapter = adapter
        binding.rvChatting.layoutManager = LinearLayoutManager(requireContext())


        // 채팅방 입장시 스크롤 하단으로 고정
        binding.chatScroll.post {
            binding.chatScroll.fullScroll(View.FOCUS_DOWN)
        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatListVO = arguments?.getParcelable<ChatListVO>("chatListVO")
        val roomId = chatListVO?.club_code
        val clubCode = chatListVO?.club_code
        val userImageUri = chatListVO?.userImg
        val userName = chatListVO?.user_name // 또는 적절한 사용자 이름 필드 사용
        val currentUserId = PreferenceManager.getUser(requireContext())?.user_id

        SocketManager.connect()
        SocketManager.getSocket().emit("joinRoom",roomId)
        SocketManager.getSocket().off("chat message")
        SocketManager.getSocket().on("chat message") { args ->
            val currentTime = getCurrentISOTime()
            // 메시지 수신 처리
            if (args[0] is String) {
                val message = args[0] as String
                Log.d("메시지 수신처리 : ",message)
                // 메시지 처리 로직 // 예: UI에 메시지 표시
                activity?.runOnUiThread {
                    // 채팅 목록에 메시지 추가
                    val messageVO = Gson().fromJson(message, ChatVO::class.java)
                    chatMessageList.add(messageVO)
                    adapter.notifyItemInserted(chatMessageList.size-1)

                    Log.d("메시지 갱신처리 : ",messageVO.toString())
                    //                    // 채팅 스크롤을 가장 최근 메시지 위치로 이동
                    //binding.rvChatting.scrollToPosition(chatMessageList.size - 1)
                    binding.rvChatting.smoothScrollToPosition(adapter.itemCount - 1)
                    //chatMessageList.add(ChatVO(clubCode, userImageUri, "userId", "정태녕", currentTime, message))
//                    adapter.notifyDataSetChanged()

                }
            }
        }

        SocketManager.getSocket().on("past messages") { args ->
            if (args[0] is String) {
                val messagesJson = args[0] as String
                val pastMessages = Gson().fromJson(messagesJson, Array<ChatVO>::class.java).toList()
                chatMessageList.clear()
                chatMessageList.addAll(pastMessages)
                adapter.notifyItemInserted(chatMessageList.size-1)
                binding.rvChatting.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }

        // 메시지 전송 로직
        binding.imgSendBtn.setOnClickListener {
            val message = binding.etMessage.text.toString()
            val messageVO = ChatVO(roomId, userImageUri, currentUserId, userName, getCurrentISOTime(), message)
            val messageJson = Gson().toJson(messageVO)
            SocketManager.getSocket().emit("chat message", roomId, messageJson)
            Log.d("메시지 송신처리 : ",message)
            // 로컬 채팅 목록에도 메시지 추가
//            chatMessageList.add(messageVO)
//            adapter.notifyItemInserted(chatMessageList.size-1)
            // 채팅 스크롤을 가장 최근 메시지 위치로 이동
            binding.rvChatting.smoothScrollToPosition(adapter.itemCount - 1)
            binding.etMessage.text.clear() // 메시지 전송 후 입력창 초기화
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        SocketManager.disconnect()
    }

    companion object {
        fun newInstance(chatListVO: ChatListVO): Fragment {
            val args = Bundle()
            Log.d("채팅받아온값",chatListVO.club_code)
            val jsonResponse = Gson().toJson(chatListVO.toString())
            Log.d("채팅받아온값", Gson().toJson(chatListVO.meetingTitle))
            Log.d("채팅받아온값", Gson().toJson(chatListVO))
            args.putParcelable("chatListVO", chatListVO)
            return ChatFragment().apply {
                arguments = args
            }

        }
    }


    fun getCurrentISOTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        //dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

}