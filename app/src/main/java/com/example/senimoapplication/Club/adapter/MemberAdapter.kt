package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.VO.MemberVO
import com.example.senimoapplication.Common.ClubMemberDeleteDialog
import com.example.senimoapplication.Common.ClubMemberLevelSwitchDialog1
import com.example.senimoapplication.Common.ClubMemberLevelSwitchDialog2
import com.example.senimoapplication.Common.ClubMemberLevelSwitchDialog3
import com.example.senimoapplication.R
import com.google.android.material.imageview.ShapeableImageView

class MemberAdapter(val context: Context, val layout : Int, val data : ArrayList<MemberVO>): RecyclerView.Adapter<MemberAdapter.ViewHolder> (){

    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val tvUserName : TextView
        val tvUserLevel : TextView
        val imgUserProfile : ShapeableImageView
        val imgUserLevel : ImageView
        val btnMore : ImageView

        init {
            tvUserName = view.findViewById(R.id.tv_C_userName)
            tvUserLevel = view.findViewById(R.id.tv_C_userLevel)
            imgUserProfile = view.findViewById(R.id.userProfile)
            imgUserLevel = view.findViewById(R.id.imgUserLevel)
            btnMore = view.findViewById(R.id.btnMore)
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

        // 회원 관리 기능
        holder.btnMore.setOnClickListener { view ->
            val member = data[position].userName
            val popupMenu = PopupMenu(context, view)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.member_option_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.member_option1 -> {
                        // 운영진으로 전환
                        val dialog = ClubMemberLevelSwitchDialog1()
                        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                        dialog.show(fragmentManager, "ClubMemberLevelSwitchDialog1")
                        true
                    }

                    R.id.member_option2 -> {
                        // 일반회원으로 전환
                        val dialog = ClubMemberLevelSwitchDialog2()
                        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                        dialog.show(fragmentManager, "ClubMemberLevelSwitchDialog2")
                        true
                    }

                    R.id.member_option3 -> {
                        // 모임장 위임하기
                        val dialog = ClubMemberLevelSwitchDialog3()
                        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                        dialog.show(fragmentManager, "ClubMemberLevelSwitchDialog3")
                        true
                    }
                    R.id.member_option4 -> {
                        // 강퇴하기
                        val dialog = ClubMemberDeleteDialog()
                        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                        dialog.show(fragmentManager, "ClubMemberDeleteDialog")
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }





    }

    override fun getItemCount(): Int {
        return data.size
    }






}