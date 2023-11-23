package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.dDate
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import kotlin.math.min

class ScheduleAdapter(val context: Context, val layout: Int, val data: List<ScheduleVO>) :
RecyclerView.Adapter<ScheduleAdapter.ViewHolder>(){

    private var showAllItems = false // 플래그 추가
    private var itemClickListener : OnItemClickListener? = null

    val inflater = LayoutInflater.from(context)

    fun setShowAllItems(showAll : Boolean) {
        showAllItems = showAll
        notifyDataSetChanged() // 변경 사항을 반영하기 위해 어댑터를 갱신
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val tvClubScheduleName : TextView // 일정명
        val tvScheduleDay : TextView // D-Day
        val tvScheduleState : TextView // 모집상태
        val tvScheduleMemNum : TextView // 모집 인원
        val tvScheduleLoca : TextView // 장소
        val tvScheduleFee : TextView // 회비
        val tvScheduleDate : TextView // 일시

        init {

            tvClubScheduleName = view.findViewById(R.id.tv_C_ScheduleName)
            tvScheduleDay = view.findViewById(R.id.tv_C_dday)
            tvScheduleState = view.findViewById(R.id.tv_C_State)
            tvScheduleMemNum = view.findViewById(R.id.tv_C_allMember)
            tvScheduleLoca = view.findViewById(R.id.tvRealLoca)
            tvScheduleFee = view.findViewById(R.id.tvRealFee)
            tvScheduleDate = view.findViewById(R.id.tvRealTime)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (showAllItems) {
            data.size // 플래그에 따라 항목 수가 달라집니다.
        } else {
            // 최대 2개까지만 표시
            min(2, data.size)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val MAX_TEXT_LENGTH = 10 // 최대 글자 수

        // 모임 제목 가져오기
        val scheduleName = data[position].scheTitle // ScheduleVO에서 가져옴

        // 글자 수가 최대 길이보다 길 경우 생략 부호(...) 추가하여 자르기
        val truncatedName = if (scheduleName.length > MAX_TEXT_LENGTH) {
            scheduleName.substring(0, MAX_TEXT_LENGTH) + "..."
        } else {
            scheduleName // 글자 수 최대 길이 이하인 경우 그대로 표시
        }

        // holder.tvClubScheduleName.text = data[position].scheduleName
        holder.tvClubScheduleName.text = truncatedName
        holder.tvScheduleDate.text = formatDate(data[position].scheDate)
        holder.tvScheduleLoca.text = data[position].scheLoca
        holder.tvScheduleFee.text = "${data[position].scheFee}원"
        holder.tvScheduleDay.text = dDate(data[position].scheDate)
        holder.tvScheduleState.text = if (data[position].maxNum <= data[position].joinedMembers) "모집마감" else "모집중"
        holder.tvScheduleMemNum.text = "${data[position].joinedMembers}/${data[position].maxNum.toString()}"


        val state = holder.tvScheduleState.text
        val dDay = holder.tvScheduleDay.text


        // d-day 스타일 변경
        val dDayList = mutableListOf<String>()
        for (i in 0..10) {
            val dDay = if (i == 0) {
                "D-day"
            } else {
                "D-${i}"
            }
            dDayList.add(dDay)
        }

        if (dDay in dDayList){
            holder.tvScheduleDay.setBackgroundResource(R.drawable.dday_soon)
        }


        // 모집 상태 태그 스타일 변경
        if(state == "모집마감"){
            holder.tvScheduleState.setBackgroundResource(R.drawable.tag_close)
            holder.tvScheduleState.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            // 해당 항목의 데이터 가져오기
            val clickedScehdule = data[position]

            // ClubActivity로 데이터 전달을 위한 Intent 생성
            val intent = Intent(context, ClubActivity::class.java)

            // ClubActivity 시작
            context.startActivity(intent)

        }

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }



    }


}