package com.example.senimoapplication.Club.fragment

import com.example.senimoapplication.MainPage.VO_main.ChatListVO
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.VO.ChatVO
import com.example.senimoapplication.Club.adapter.ChatAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentChatBinding
import com.example.senimoapplication.server.SocketManager


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

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val adapter = ChatAdapter(requireContext(), R.layout.chat_message_item, chatMessageList)
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
        SocketManager.connect()

        SocketManager.getSocket().on("chat message") { args ->
            // 메시지 수신 처리
            if (args[0] is String) {
                val message = args[0] as String
                // 메시지 처리 로직 // 예: UI에 메시지 표시
                activity?.runOnUiThread {
                    // 채팅 목록에 메시지 추가
                    chatMessageList.add(ChatVO("chatRoomId", "userImageUri", "userId", "userName", "messageTime", message))
                    adapter.notifyDataSetChanged()
                    // 채팅 스크롤을 가장 최근 메시지 위치로 이동
                    binding.rvChatting.scrollToPosition(chatMessageList.size - 1)
                }
            }
        }

        // 메시지 전송 로직
        binding.imgSendBtn.setOnClickListener {
            val message = binding.etMessage.text.toString()
            SocketManager.getSocket().emit("new message", message)
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
            args.putParcelable("chatListVO", chatListVO)
            val fragment = ChatFragment()
            fragment.arguments = args
            return fragment
        }
    }
}