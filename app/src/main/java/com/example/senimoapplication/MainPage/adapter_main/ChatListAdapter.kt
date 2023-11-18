package com.example.senimoapplication.MainPage.adapter_main

import com.example.senimoapplication.MainPage.VO_main.ChatListVO
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.unit.min
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Common.myChatListDate
import com.example.senimoapplication.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.zip.DataFormatException

class ChatListAdapter(val context: Context, val layout: Int, val data: List<ChatListVO>)
    : RecyclerView.Adapter<ChatListAdapter.ViewHolder>(){

    private var onItemClickListener: OnItemClickListener? = null

    // OnItemClickListener 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(chatListVO: ChatListVO)
    }

    // 외부에서 OnItemClickListener를 설정할 수 있도록 하는 메서드
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val img_M_ChatImg : View
        val tv_M_ChatTitle : TextView
        val tv_M_ChatContent : TextView
        val tv_M_Time : TextView
        val tv_M_New : TextView
        val v_M_NewShape: View
        init {
            img_M_ChatImg = view.findViewById(R.id.img_M_ChatImg) // 채팅 유저 프로필
            tv_M_ChatTitle = view.findViewById(R.id.tv_M_ChatTitle) // 채팅 모임명
            tv_M_ChatContent = view.findViewById(R.id.tv_M_ChatContent) // 채팅 내용
            tv_M_Time = view.findViewById(R.id.tv_M_Time) // 채팅 시간
            tv_M_New = view.findViewById(R.id.tv_M_New) // 새로 온 채팅
            v_M_NewShape = view.findViewById(R.id.v_M_NewShape)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layout,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListAdapter.ViewHolder, position: Int) {
        val CHATLIST_TITLE_MAX_TEXT_LENGTH = 16 // 최대 채팅 제목 수
        val CHATLIST_CONTENT_MAX_TEXT_LENGTH = 36 // 최대 채팅 내용 수


        // 채팅 제목, 내용 가져오기
        val meetingTitle = data[position].meetingTitle
        val meetingContent = data[position].recentlyMessage

        // 글자 수가 최대 길이보다 길 경우 생략 부호(...) 추가하여 자르기

        val meetingTitle_truncatedName = if ( meetingTitle.length > CHATLIST_TITLE_MAX_TEXT_LENGTH) {
            meetingTitle.substring(0, CHATLIST_TITLE_MAX_TEXT_LENGTH) + "..."
        } else {
            meetingTitle // 글자 수 최대 길이 이하인 경우 그대로 표시
        }


        val meetingContent_truncatedName = if ( meetingContent.length > CHATLIST_CONTENT_MAX_TEXT_LENGTH) {
            meetingContent.substring(0, CHATLIST_CONTENT_MAX_TEXT_LENGTH) + "..."
        } else {
            meetingContent // 글자 수 최대 길이 이하인 경우 그대로 표시
        }

        holder.tv_M_ChatTitle.text = meetingTitle_truncatedName
        holder.tv_M_ChatContent.text = meetingContent_truncatedName
        val isoDateString = data[position].time // 이미 ISO 8601 형식의 문자열인 경우
        holder.tv_M_Time.text = myChatListDate(isoDateString, context) // 상대적인 시간 표시

        // new 필드 값을 가져와서 TextView에 표시
        val newCount = data[position].newMessagesCount
        if (newCount > 0) {
            holder.tv_M_New.visibility = View.VISIBLE
            holder.tv_M_New.text = "N"
            holder.v_M_NewShape.visibility = View.VISIBLE

        } else {
            holder.tv_M_New.visibility = View.GONE
            holder.v_M_NewShape.visibility = View.GONE
        }

        // 로그 확인
        Log.d("ChatListAdapter", "Position $position = New Count : $newCount")


        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(data[position])
        }




    }

    override fun getItemCount(): Int {
        return data.size
    }

    // 시간을 상대적인 시간으로 변환하는 함수
//    private fun formatRelativeTime(context: Context, messageTime: Long): String {
//        val currentTime = System.currentTimeMillis()
//        val diff = currentTime - messageTime
//        val seconds = diff / 1000
//        val minutes = seconds / 60
//        val hours = minutes / 60
//        val days = hours / 24
//
//        return when {
//            seconds < 60 -> context.getString(R.string.just_now)
//            minutes < 60 -> context.getString(R.string.minutes_ago, minutes.toInt())
//            hours < 24 -> context.getString(R.string.hours_ago, hours.toInt())
//            days < 2 -> context.getString(R.string.days_ago, days.toInt())
//            else -> SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(messageTime)
//        }
//    }
}