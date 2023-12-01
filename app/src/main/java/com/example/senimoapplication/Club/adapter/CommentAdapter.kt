package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.VO.CommentVO
import com.example.senimoapplication.Common.myChatListDate
import com.example.senimoapplication.R
import com.google.android.material.imageview.ShapeableImageView

class CommentAdapter(val context: Context, val layout: Int, val data: List<CommentVO>) : RecyclerView.Adapter<CommentAdapter.ViewHolder> (){
    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val tvUserName : TextView
        val tvCommentDate : TextView
        val tvComment : TextView
        val imgUserProfile : ShapeableImageView

        init {
            tvUserName = view.findViewById(R.id.tv_P_C_user)
            tvCommentDate = view.findViewById(R.id.tvCommentTime)
            tvComment = view.findViewById(R.id.tvComment)
            imgUserProfile = view.findViewById(R.id.imgUserProfile)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUserName.text = data[position].userName
        holder.tvCommentDate.text = myChatListDate(data[position].reviewedDt, context)
        holder.tvComment.text = data[position].reviewContent

        // 댓글 유저 이미지 로드 및 표시
        val userImgUrl = data[position].userImg
        Glide.with(context)
            .load(userImgUrl)
            .placeholder(R.drawable.ic_loading6) // 로딩 중 표시될 이미지
            .error(R.drawable.ic_profile_circle) // 로딩 실패 시 표시될 이미지
            .into(holder.imgUserProfile)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}