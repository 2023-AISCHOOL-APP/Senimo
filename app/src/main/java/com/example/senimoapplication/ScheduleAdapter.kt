package com.example.senimoapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.VO.ScheduleVO

class ScheduleAdapter(val context: Context, val layout : Int, val data : ArrayList<ScheduleVO>) :
RecyclerView.Adapter<ScheduleAdapter.ViewHolder>(){
    val inflater = LayoutInflater.from(context)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvClubName : TextView // 모임명
        val tvClubScheduleName : TextView // 일정명
        val tvScheduleDay : TextView // D-Day
        val tvScheduleState : TextView // 모집상태
        val tvScheduleMemNum : TextView // 전체 모집 인원
        val tvScheduleAttendance : TextView // 참가 신청 인원
        val tvScheduleLoca : TextView // 장소
        val tvScheduleFee : TextView // 회비
        val tvScheduleDate : TextView // 일시

        init {
            tvClubName = view.findViewById(R.id.tv_C_name)
            tvClubScheduleName = view.findViewById(R.id.tv_C_ScheduleName)
            tvScheduleDay = view.findViewById(R.id.tv_C_dday)
            tvScheduleState = view.findViewById(R.id.tv_C_State)
            tvScheduleMemNum = view.findViewById(R.id.tv_C_allMember)
            tvScheduleAttendance = view.findViewById(R.id.tv_C_attendance)
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
        holder.tvClubName.text = data[position].clubName
        holder.tvClubScheduleName.text = data[position].scheduleName
        holder.tvScheduleDate.text = data[position].scheduleDate
        holder.tvScheduleLoca.text = data[position].scheduleLoca
        holder.tvScheduleFee.text = data[position].scheduleFee
        holder.tvScheduleDay.text = "${data[position].dday}일 남음"
        holder.tvScheduleMemNum.text = "/ ${data[position].allMembers.toString()}"
        holder.tvScheduleAttendance.text = data[position].attendance.toString()

    }
}