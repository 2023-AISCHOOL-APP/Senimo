package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.dDate
import com.example.senimoapplication.Common.formatDate

class ScheduleAdapter(val context: Context, val layout : Int, val data : ArrayList<ScheduleVO>) :
RecyclerView.Adapter<ScheduleAdapter.ViewHolder>(){
    val inflater = LayoutInflater.from(context)
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
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvClubScheduleName.text = data[position].scheduleName
        holder.tvScheduleDate.text = formatDate(data[position].scheduleDate)
        holder.tvScheduleLoca.text = data[position].scheduleLoca
        holder.tvScheduleFee.text = "${data[position].scheduleFee}원"
        holder.tvScheduleDay.text = dDate(data[position].scheduleDate)
        holder.tvScheduleState.text = data[position].state
        holder.tvScheduleMemNum.text = "${data[position].attendance}/${data[position].allMembers.toString()}"


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



    }


}