package com.example.senimoapplication.Common

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
    }

    override fun getItemCount(): Int {
        return data.size
    }
}