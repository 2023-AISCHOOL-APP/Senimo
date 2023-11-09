package com.example.senimoapplication.Login.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.R


class GuAdapter(val layout: Int, val list: ArrayList<String>, val context: Context) : RecyclerView.Adapter<GuAdapter.ViewHolder>() {
  val inflater = LayoutInflater.from(context)

  interface OnItemClickListener {
    fun onItemClick(position: Int) {}
  }

  var itemClickListener: OnItemClickListener? = null

  inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    val tvGu = view.findViewById<TextView>(R.id.tvGu)

    init {
      itemView.setOnClickListener {
        itemClickListener?.onItemClick(adapterPosition)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = inflater.inflate(layout, parent, false)

    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.tvGu.text = list[position]
  }

  override fun getItemCount(): Int {
    return list.size
  }
}