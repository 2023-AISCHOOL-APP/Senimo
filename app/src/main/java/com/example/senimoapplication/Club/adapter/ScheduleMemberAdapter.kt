package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.VO.ScheduleMemberVO
import com.example.senimoapplication.R
import com.google.android.material.imageview.ShapeableImageView

class ScheduleMemberAdapter(val context: Context, val layout : Int, val data : ArrayList<ScheduleMemberVO>): RecyclerView.Adapter<ScheduleMemberAdapter.ViewHolder> (){

    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val tvUserName : TextView
        val tvUserLevel : TextView
        val imgUserProfile : ShapeableImageView
        val imgUserLevel : ImageView

        init {
            tvUserName = view.findViewById(R.id.tv_C_userName)
            tvUserLevel = view.findViewById(R.id.tv_C_userLevel)
            imgUserProfile = view.findViewById(R.id.userProfile)
            imgUserLevel = view.findViewById(R.id.imgUserLevel)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUserName.text = data[position].userName
        holder.tvUserLevel.text = data[position].userLevel
        holder.imgUserProfile.setImageResource(data[position].imgId)
        holder.imgUserLevel.setImageResource(data[position].sticker)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}