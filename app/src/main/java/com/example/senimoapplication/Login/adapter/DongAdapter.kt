package com.example.senimoapplication.Login.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.R
import okhttp3.internal.addHeaderLenient


class DongAdapter(val layout: Int, var list: ArrayList<String>, val context: Context) : RecyclerView.Adapter<DongAdapter.ViewHolder>() {

  val inflater = LayoutInflater.from(context)
  var selectedPosition = -1

  interface OnItemClickListener {
    fun onItemClick(position: Int)
  }

  var itemClickListener: OnItemClickListener? = null

  inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    val tvDong = view.findViewById<TextView>(R.id.tvDong)

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

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.tvDong.text = list[position]

    val selectedColor = ContextCompat.getColor(context, R.color.txt_gray30)
    val selectedTextColor = ContextCompat.getColor(context, R.color.white)
    val nonSelectedColor = ContextCompat.getColor(context, R.color.white)
    val nonSelectedTextColor = ContextCompat.getColor(context, R.color.txt_gray80)

    if(position == selectedPosition && selectedPosition != -1){
      holder.itemView.setBackgroundColor(selectedColor)
      holder.tvDong.setTextColor(selectedTextColor)
    }else{
      holder.itemView.setBackgroundColor(nonSelectedColor)
      holder.tvDong.setTextColor(nonSelectedTextColor)
    }
  }

  override fun getItemCount(): Int {
    return list.size
  }

  // 새로운 데이터로 어댑터를 업데이트하는 함수
  fun updateData(newList: List<String>) {
    Log.d("DongAdapter", "New List: $newList")
    list = ArrayList(newList)
    notifyDataSetChanged()
  }

  // 선택한 동이름을 추출하기 위한 함수
  fun getSelectedDongName(): String {
    return list[selectedPosition]
  }
}