package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.VO.ChatVO
import com.example.senimoapplication.Common.ChattingTime
import com.example.senimoapplication.R
import com.example.senimoapplication.server.Token.PreferenceManager
import com.google.android.material.imageview.ShapeableImageView

class ChatAdapter (val context: Context, val layout: Int, val data: ArrayList<ChatVO>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    val currentUserId = PreferenceManager.getUser(context)?.user_id
    val inflater = LayoutInflater.from(context)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imgUserProfile : ShapeableImageView
        val tvChatUserName : TextView
        val tvChatMessage : TextView
        val tvChatTime : TextView
        val tvChatMyMessage : TextView
        val tvMyChatTime : TextView

        init {
            imgUserProfile = view.findViewById(R.id.img_C_ChatProfile)
            tvChatUserName = view.findViewById(R.id.tvChatUserName)
            tvChatMessage = view.findViewById(R.id.tvChatMessage)
            tvChatTime = view.findViewById(R.id.tvChatTIme)
            tvChatMyMessage = view.findViewById(R.id.tvChatMyMessage)
            tvMyChatTime = view.findViewById(R.id.tvMyChatTIme)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val view = inflater.inflate(layout, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        Log.d("채팅받아온값 adapter1", data[position].toString())
        Log.d("채팅받아온값 adapter2", data[position].userId.toString())
        Log.d("채팅받아온값 adapter3", currentUserId.toString())
        Log.d("채팅받아온값 adapter4", data[position].clubCode.toString())
        Log.d("채팅받아온값 adapter5", data[position].userName.toString())
        Log.d("채팅받아온값 adapter6", data[position].message.toString())
        Log.d("채팅받아온값 adapter7", data[position].imageUri.toString())
        val imageUrl = data[position].imageUri
        if(data[position].userId == currentUserId) {
            // 내가 보낸 메시지
            Log.d("채팅받아온값 내가 보낸메시지", data[position].toString())
            holder.tvMyChatTime.text = ChattingTime(data[position].messageTime)
            holder.tvChatMyMessage.text = data[position].message

            holder.tvChatUserName.visibility = GONE
            holder.tvChatMessage.visibility = GONE
            holder.tvChatTime.visibility = GONE
            holder.imgUserProfile.visibility =GONE

        }else{
            // 다른 사람이 보낸 메시지
            Log.d("채팅받아온값 다른사람이 보낸메시지", data[position].toString())
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
                .error(R.drawable.golf_img) // 로딩 실패 시 표시될 이미지
                .into(holder.imgUserProfile)
            holder.tvChatUserName.text = data[position].userName
            holder.tvChatMessage.text = data[position].message
            holder.tvChatTime.text = ChattingTime(data[position].messageTime)

            holder.tvMyChatTime.visibility = GONE
            holder.tvChatMyMessage.visibility = GONE

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}