package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.VO.AllMemberResVO
import com.example.senimoapplication.Club.VO.MemberRoleResVO
import com.example.senimoapplication.Club.VO.MemberVO
import com.example.senimoapplication.Club.fragment.MemberManager
import com.example.senimoapplication.Common.showFragmentDialogBox
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MemberAdapter(
    val context: Context,
    val layout: Int,
    val data: ArrayList<MemberVO>
) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    val inflater = LayoutInflater.from(context)


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView
        val tvUserLevel: TextView
        val imgUserProfile: ShapeableImageView
        val btnMore: ImageView

        init {
            tvUserName = view.findViewById(R.id.tv_C_userName)
            tvUserLevel = view.findViewById(R.id.tv_C_userLevel)
            imgUserProfile = view.findViewById(R.id.userProfile)
            btnMore = view.findViewById(R.id.imgbtnMore)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUserName.text = data[position].userName

        // userLevel에 따라 텍스트 설정하기
        when (data[position].clubRole) {
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

        val imageUrl = data[position].imgUri
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.loading) // 로딩 중 표시될 이미지
            .error(R.drawable.golf_img) // 로딩 실패 시 표시될 이미지
            .into(holder.imgUserProfile)

        // 회원 정보 페이지로 이동
        holder.imgUserProfile.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("selected_tab", "M_tab4")
            context.startActivity(intent)
        }

        // 회원 관리 기능

        holder.btnMore.setOnClickListener { view ->
        val member = data[position].userName
        val popupMenu = PopupMenu(context, view)
        val menuInflater = popupMenu.menuInflater
        menuInflater.inflate(R.menu.member_option_menu, popupMenu.menu)
        val memberManager = MemberManager()

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.member_option1 -> {
                    // 운영진으로 전환
                    showFragmentDialogBox(view.context, "운영진으로 임명할까요?", "임명하기", "운영진이 되었습니다")
                    true
                }

                R.id.member_option2 -> {
                    // 일반회원으로 전환
                    showFragmentDialogBox(view.context, "일반 회원으로 전환할까요?", "전환하기", "일반 회원이 되었습니다")
                    true
                }

                R.id.member_option3 -> {
                    showFragmentDialogBox(
                        view.context,
                        "모임장 권한을 위임하시겠습니까?",
                        "위임하기",
                        "권한이 위임되었습니다"
                    )
                    // 모임장 위임하기

                    true
                }
                R.id.member_option4 -> {
                    // 강퇴하기
                    showFragmentDialogBox(view.context, "이 회원을 모임에서 내보낼까요?", "내보내기", "모임에서 내보냈습니다.")
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
