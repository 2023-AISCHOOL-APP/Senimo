package com.example.senimoapplication.MainPage.adapter_main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.MainPage.VO_main.MyScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.dDate
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.Common.myScheduleDate

class MyScheduleAdapter(val context: Context, val layout: Int, val data : ArrayList<MyScheduleVO>) :
RecyclerView.Adapter<MyScheduleAdapter.ViewHolder>(){

    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tv_M_ScheduleTitle : TextView // 내 일정명
        val tv_M_ScheduleContent : TextView // 내 일정 소개글
        val tv_M_ScheduleDday : TextView // 내 일정 D-Day
        val tv_M_ScheduleDate : TextView // 내 일정 일시

        init {
            tv_M_ScheduleTitle = view.findViewById(R.id.tv_M_ScheduleTitle)
            tv_M_ScheduleContent = view.findViewById(R.id.tv_M_ScheduleContent)
            tv_M_ScheduleDday = view.findViewById(R.id.tv_M_ScheduleDday)
            tv_M_ScheduleDate = view.findViewById(R.id.tv_M_ScheduleDate)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): MyScheduleAdapter.ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyScheduleAdapter.ViewHolder, position: Int) {
        val mySchedule = data[position]

        holder.tv_M_ScheduleTitle.text = data[position].myscheduleTitle
        val contentText = mySchedule.myscheduleContent

        if (contentText.length > 14) {
            val truncatedText = contentText.substring(0, 14) + "..."
            holder.tv_M_ScheduleContent.text = truncatedText
        } else {
            holder.tv_M_ScheduleContent.text = contentText
        }

        holder.tv_M_ScheduleDday.text = dDate(data[position].myscheduleDate)
        holder.tv_M_ScheduleDate.text = myScheduleDate(data[position].myscheduleDate)

    }

    override fun getItemCount(): Int {
        return data.size
    }
}