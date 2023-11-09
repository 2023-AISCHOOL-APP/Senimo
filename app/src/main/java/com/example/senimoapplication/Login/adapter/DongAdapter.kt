package com.example.senimoapplication.Login.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.R


class DongAdapter(val layout: Int, var list: ArrayList<String>, val context: Context) : RecyclerView.Adapter<DongAdapter.ViewHolder>() {

  val inflater = LayoutInflater.from(context)

  class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    val tvDong = view.findViewById<TextView>(R.id.tvDong)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = inflater.inflate(layout, parent, false)

    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.tvDong.text = list[position]
  }

  override fun getItemCount(): Int {
    return list.size
  }

  // 새로운 데이터로 어댑터를 업데이트하는 함수
  fun updateData(newList: List<String>) {
    Log.d("DongAdapter", "New List: $newList")
//    list.clear()
//    list.addAll(newList)
    list = ArrayList(newList)
    notifyDataSetChanged()
  }
}