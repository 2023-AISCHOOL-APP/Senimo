package com.example.senimoapplication.Login.adapter_main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.R
import com.example.senimoapplication.MainPage.VO_main.MeetingVO

class MeetingAdapter (val context : Context, val layout : Int, val data : ArrayList<MeetingVO>)
    : RecyclerView.Adapter<MeetingAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        // onCreateViewHolder가 inflate한 레이아웃의 View들의 아이디값을 통해 찾아오는 곳
        val tv_M_Title : TextView  // 모임명
        val tv_M_Content : TextView // 모임 내용
        val tv_M_Keyword : TextView // 키워드
        val tv_M_attendance : TextView // 참가 인원
        val tv_M_allMember : TextView // 전체 인원
        // val tv_M_Person : TextView
        val Img_M_Meeting : ImageView // 모임 사진
        // val img_M_Backicon : ImageView
        init {
            tv_M_Title = view.findViewById(R.id.tv_M_Title)
            tv_M_Content = view.findViewById(R.id.tv_M_Content)
            tv_M_Keyword = view.findViewById(R.id.tv_M_Keyword)
            tv_M_attendance = view.findViewById(R.id.tv_M_attendance)
            tv_M_allMember = view.findViewById(R.id.tv_M_allMember)
            // tv_M_Person = view.findViewById(R.id.tv_M_attendance)
            Img_M_Meeting = view.findViewById(R.id.Img_M_Meeting)
            // img_M_Backicon = view.findViewById(R.id.img_M_Backicon)
        }

    }

    // 1) 한 칸에 들어갈 디자인을 눈에 보이는 View로 바꿔서 ViewHolder클래스로 보내주는 기능
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // ViewHolder클래스가 찾아온 뷰들을 컨트롤 할 수 있는 곳
        // 데이터 + 디자인
        holder.tv_M_Title.text = data[position].title
        holder.tv_M_Content.text = data[position].content
        holder.tv_M_Keyword.text = data[position].keyword
        holder.tv_M_attendance.text = data[position].attendance.toString()
        holder.tv_M_allMember.text = "/${data[position].allMember.toString()}명"
        // holder.tv_M_Person.text = data[position].person
        holder.Img_M_Meeting.setImageResource(R.drawable.golf_img)
        // holder.img_M_Backicon.setImageResource(R.drawable.ic_back_black)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}