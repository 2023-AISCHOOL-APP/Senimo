package com.example.senimoapplication.MainPage.adapter_main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.MainPage.VO_main.MyScheduleVO
import com.example.senimoapplication.R
import com.example.senimoapplication.Common.dDate
import com.example.senimoapplication.Common.myScheduleDate


class MyScheduleAdapter(val context: Context, val layout: Int, val data : ArrayList<ScheduleVO>) :

RecyclerView.Adapter<MyScheduleAdapter.ViewHolder>(){

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(schedule: ScheduleVO)
    }

    private var itemClickListener : OnItemClickListener? = null

    // 외부에서 클릭 리스너를 설정할 수 있게 메서드 추가
    fun setOnClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val Img_M_Meeting: ImageView = view.findViewById(R.id.Img_M_Meeting)
        val tvScheduleTitle: TextView = view.findViewById(R.id.tv_M_ScheduleTitle)
        val tv_M_ScheduleContent: TextView = view.findViewById(R.id.tv_M_ScheduleContent)
        val tv_M_ScheduleDday: TextView = view.findViewById(R.id.tv_M_ScheduleDday)
        val tv_M_ScheduleDate: TextView = view.findViewById(R.id.tv_M_ScheduleDate)

        init {
            Img_M_Meeting.setOnClickListener {
                Log.d("MyScheduleAdapter", "Item clicked: ${data[adapterPosition].scheTitle}")
                // 클릭 리스너 호출
                itemClickListener?.onItemClick(data[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): MyScheduleAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyScheduleAdapter.ViewHolder, position: Int) {

        val schedule = data[position]

        // Glide를 사용하여 이미지 로드 및 표시
        Log.d("getCombinewdData", "이미지값 확인 : ${data[position].scheImg}")
        Glide.with(context)
            // .load(imageUri)
            .load(data[position].scheImg)
            .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
            .error(R.drawable.ic_meeting_profile) // 로딩 실패 시 표시될 이미지
            .into(holder.Img_M_Meeting)

        // 제목
        val title = schedule.scheTitle
        if(title.length > 13){
            val truncatedText = title.substring(0, 14) + "..."
            holder.tvScheduleTitle.text = truncatedText
        } else {
            holder.tvScheduleTitle.text = title
        }

        // 내용
        val contentText = schedule.scheContent
        if (contentText.length > 14) {
            val truncatedText = contentText.substring(0, 14) + "..."
            holder.tv_M_ScheduleContent.text = truncatedText
        } else {
            holder.tv_M_ScheduleContent.text = contentText
        }

        holder.tv_M_ScheduleDday.text = dDate(data[position].scheDate)
        holder.tv_M_ScheduleDate.text = myScheduleDate(data[position].scheDate)

    }

    override fun getItemCount(): Int {
        return data.size
    }
}