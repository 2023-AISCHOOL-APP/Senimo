package com.example.senimoapplication.Login.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.R


class GuAdapter(val layout: Int, val list: ArrayList<String>, val context: Context) : RecyclerView.Adapter<GuAdapter.ViewHolder>() {
  val inflater = LayoutInflater.from(context)

  interface OnItemClickListener {
    fun onItemClick(position: Int) {}
  }

  var itemClickListener: OnItemClickListener? = null

  // 선택 시켜 놓을 아이템의 위치
  var selectedPosition = -1

  inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    val tvGu = view.findViewById<TextView>(R.id.tvGu)

    init {
      itemView.setOnClickListener {
        itemClickListener?.onItemClick(adapterPosition)
        val previousPosition = selectedPosition
        selectedPosition = adapterPosition

        notifyItemChanged(previousPosition)

        notifyItemChanged(selectedPosition)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = inflater.inflate(layout, parent, false)

    return ViewHolder(view)
  }

  // 클릭 시 배경색, 글자색 변경
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.tvGu.text = list[position]

    val selectedColor = ContextCompat.getColor(context, R.color.main)
    val selectedTextColor = ContextCompat.getColor(context, R.color.white)
    val nonSelectedColor = ContextCompat.getColor(context, R.color.txt_gray10)
    val nonSelectedTextColor = ContextCompat.getColor(context, R.color.txt_gray90)

    if(position == selectedPosition){
      holder.itemView.setBackgroundColor(selectedColor)
      holder.tvGu.setTextColor(selectedTextColor)
    }else{
      holder.itemView.setBackgroundColor(nonSelectedColor)
      holder.tvGu.setTextColor(nonSelectedTextColor)
    }
  }

  override fun getItemCount(): Int {
    return list.size
  }

  // 선택된 항목의 문자열을 반환하는 메소드
  fun getSelectedItem(): String {
    return list[selectedPosition]
  }



}