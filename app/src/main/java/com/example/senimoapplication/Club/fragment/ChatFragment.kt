package com.example.senimoapplication.Club.fragment

import com.example.senimoapplication.MainPage.VO_main.ChatListVO
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.senimoapplication.R


class ChatFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        return view
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