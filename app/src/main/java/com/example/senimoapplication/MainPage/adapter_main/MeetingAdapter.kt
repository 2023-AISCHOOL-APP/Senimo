package com.example.senimoapplication.MainPage.adapter_main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.R
import com.example.senimoapplication.MainPage.VO_main.MeetingVO

class MeetingAdapter(val context: Context, val layout: Int, val data: List<MeetingVO>)
    : RecyclerView.Adapter<MeetingAdapter.ViewHolder>(){

    private var showAllItems = false // 플래그 추가

    fun setShowAllItems(showAll : Boolean) {
        showAllItems = showAll
        notifyDataSetChanged() // 변경 사항을 반영하기 위해 어댑터를 갱신
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var itemClickListener : OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        // onCreateViewHolder가 inflate한 레이아웃의 View들의 아이디값을 통해 찾아오는 곳
        val tv_M_Gu : TextView // 구
        val tv_M_Title : TextView  // 모임명
        val tv_M_Content : TextView // 모임 내용
        val tv_M_Keyword : TextView // 키워드
        val tv_M_attendance : TextView // 참가 인원
        val tv_M_allMember : TextView // 전체 인원
        val Img_M_Meeting : ImageView // 모임 사진
        init {
            tv_M_Gu = view.findViewById(R.id.tv_M_Gu)
            tv_M_Title = view.findViewById(R.id.tv_M_Title)
            tv_M_Content = view.findViewById(R.id.tv_M_Content)
            tv_M_Keyword = view.findViewById(R.id.tv_M_Keyword)
            tv_M_attendance = view.findViewById(R.id.tv_M_attendance)
            tv_M_allMember = view.findViewById(R.id.tv_M_allMember)
            Img_M_Meeting = view.findViewById(R.id.Img_M_Meeting)
        }
    }

    // 1) 한 칸에 들어갈 디자인을 눈에 보이는 View로 바꿔서 ViewHolder클래스로 보내주는 기능
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layout,parent,false)
        val holder = ViewHolder(view)

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(holder.adapterPosition)
        }

        return holder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val TITLE_MAX_TEXT_LENGTH = 11 // 최대 글자 수
        val CONTENT_MAX_TEXT_LENGTH = 18 // 최대 내용 수

        // 모임 타이틀 , 소개글 가져오기
        val title = data[position].title
        val content = data[position].content

        // 글자 수가 최대 길이보다 길 경우 생략 부호(...) 추가하여 자르기
        val title_truncatedName = truncateText(title, TITLE_MAX_TEXT_LENGTH)
        val content_truncatedName = truncateText(content, CONTENT_MAX_TEXT_LENGTH)

        holder.tv_M_Gu.text = data[position].gu
        holder.tv_M_Title.text = title_truncatedName
        holder.tv_M_Content.text = content_truncatedName
        holder.tv_M_Keyword.text = data[position].keyword

        when(data[position].keyword){
            "운동" -> holder.tv_M_Keyword.setBackgroundResource(R.drawable.keyword)
            "취미" -> holder.tv_M_Keyword.setBackgroundResource(R.drawable.keyword_color2)
            "전시/공연" -> holder.tv_M_Keyword.setBackgroundResource(R.drawable.keyword_color3)
            "여행" -> holder.tv_M_Keyword.setBackgroundResource(R.drawable.keyword_color4)
            "자기계발" -> holder.tv_M_Keyword.setBackgroundResource(R.drawable.keyword_color5)
            "재테크" -> holder.tv_M_Keyword.setBackgroundResource(R.drawable.keyword_color6)
            else -> holder.tv_M_Keyword.setBackgroundResource(R.drawable.keyword_color6)
        }
        holder.tv_M_attendance.text = data[position].attendance.toString()
        holder.tv_M_allMember.text = "/${data[position].allMember.toString()}명"

        // Glide를 사용하여 이미지 로드 및 표시
        val imageUrl = data[position].imageUri
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_loading6) // 로딩 중 표시될 이미지
            .error(R.drawable.ic_meeting_profile) // 로딩 실패 시 표시될 이미지
            .into(holder.Img_M_Meeting)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun truncateText(text: String, maxLength: Int): String {
        val lines = text.split("\n")
        val firstLine = lines.firstOrNull() ?: ""
        val remainingLines = lines.drop(1)

        val truncatedFirstLine = if (firstLine.length > maxLength) {
            firstLine.substring(0, maxLength - 3) + "..."
        } else {
            firstLine
        }

        return if (remainingLines.isEmpty()) {
            truncatedFirstLine
        } else {
            "$truncatedFirstLine"
        }
    }
}