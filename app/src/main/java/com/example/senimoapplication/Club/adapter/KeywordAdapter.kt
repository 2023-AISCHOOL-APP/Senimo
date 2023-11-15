package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.R

class KeywordAdapter (val context : Context, val layout : Int, val data : ArrayList<String> ):RecyclerView.Adapter<KeywordAdapter.ViewHolder>(){

    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvKeyword : TextView = view.findViewById(R.id.tvKeyword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvKeyword.text = data[position]
        for (keyword in data) {
            when (keyword) {
                "운동" -> holder.tvKeyword.setBackgroundResource(R.drawable.keyword)
                "취미" -> holder.tvKeyword.setBackgroundResource(R.drawable.keyword_color2)
                "전시/공연" -> holder.tvKeyword.setBackgroundResource(R.drawable.keyword_color3)
                "여행" -> holder.tvKeyword.setBackgroundResource(R.drawable.keyword_color4)
                "자기계발" -> holder.tvKeyword.setBackgroundResource(R.drawable.keyword_color5)
                "재테크" -> holder.tvKeyword.setBackgroundResource(R.drawable.keyword_color6)
                // 기타 키워드에 대한 처리
                else -> holder.tvKeyword.setBackgroundResource(R.drawable.keyword_color6)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}