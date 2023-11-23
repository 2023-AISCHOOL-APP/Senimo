package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.VO.ScheduleMemberVO
import com.example.senimoapplication.R
import com.google.android.material.imageview.ShapeableImageView

class ScheduleMemberAdapter(val context: Context, val layout : Int, val data : List<ScheduleMemberVO>): RecyclerView.Adapter<ScheduleMemberAdapter.ViewHolder> (){

    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val tvUserName : TextView
        val tvUserLevel : TextView
        val imgUserProfile : ShapeableImageView
        val imgbtnMore : ImageView

        init {
            tvUserName = view.findViewById(R.id.tv_C_userName)
            tvUserLevel = view.findViewById(R.id.tv_C_userLevel)
            imgUserProfile = view.findViewById(R.id.userProfile)
            imgbtnMore = view.findViewById(R.id.imgbtnMore)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUserName.text = data[position].userName
        when(data[position].clubRole){
            1 -> {
                holder.tvUserLevel.text = "모임장"
                holder.tvUserLevel.setBackgroundResource(R.drawable.user_level_leader)
            }
            2 -> {
                holder.tvUserLevel.text = "운영진"
                holder.tvUserLevel.setBackgroundResource(R.drawable.user_level_oper)
            }
            3 -> {
                holder.tvUserLevel.text = "일반"
                holder.tvUserLevel.setBackgroundResource(R.drawable.user_level_basic)
            }
        }

        Glide.with(context)
            .load(data[position].userImg)
            .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
            .error(R.drawable.ic_profile_circle) // 로딩 실패 시 표시될 이미지
            .into(holder.imgUserProfile)
        holder.imgbtnMore.visibility = INVISIBLE
    }

    override fun getItemCount(): Int {
        return data.size
    }
}