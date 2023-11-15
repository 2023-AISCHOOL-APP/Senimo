package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.VO.CommentVO
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.R
import com.google.android.material.imageview.ShapeableImageView

class CommentAdapter (val context: Context, val layout : Int, val data : ArrayList<CommentVO>) : RecyclerView.Adapter<CommentAdapter.ViewHolder> (){
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
        holder.tvCommentDate.text = formatDate(data[position].time)
        holder.tvComment.text = data[position].comment
        //holder.imgUserProfile.setImageResource()
    }

    override fun getItemCount(): Int {
        return data.size
    }

}