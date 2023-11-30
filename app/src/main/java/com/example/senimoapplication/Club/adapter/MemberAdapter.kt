package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.Activity_club.userProfileActivity
import com.example.senimoapplication.Club.VO.MemberVO
import com.example.senimoapplication.Club.fragment.HomeFragment
import com.example.senimoapplication.Club.fragment.MemberManager
import com.example.senimoapplication.Common.showAlertDialogBox
import com.example.senimoapplication.Common.showDeleteDialogBox
import com.example.senimoapplication.Common.showLeaderUpdateDialogBox
import com.example.senimoapplication.Common.showUpdateDialogBox
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.server.Token.PreferenceManager
import com.example.senimoapplication.server.Token.UserData.userId
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MemberAdapter(
    val context: Context,
    val layout: Int,
    val data: ArrayList<MemberVO>,
    private val clubLeader : String?,
    private val fragment: HomeFragment
) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    val inflater = LayoutInflater.from(context)
    val userData = PreferenceManager.getUser(context)
    var userId = userData?.user_id

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
                holder.btnMore.visibility = INVISIBLE
            }
        }

        val imageUrl = data[position].imgUri
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.animation_loading) // 로딩 중 표시될 이미지
            .error(R.drawable.golf_img) // 로딩 실패 시 표시될 이미지
            .into(holder.imgUserProfile)

        // 회원 정보 페이지로 이동
        holder.imgUserProfile.setOnClickListener {
            val intent = Intent(context, userProfileActivity::class.java)
            intent.putExtra("selected_user", data[position].userId)
            Log.d("userProfile","모임 멤버 리스트 보내는 값 ${data[position].userId}")
            context.startActivity(intent)
        }


        Log.d("leader:","${clubLeader}")
        if(userId == clubLeader){
            holder.btnMore.visibility = VISIBLE
            holder.btnMore.setOnClickListener { view ->
                val popupMenu = PopupMenu(context, view)
                val menuInflater = popupMenu.menuInflater
                menuInflater.inflate(R.menu.member_option_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { item ->
                    val user = data[position].userId
                    Log.d("member_user:","${user}")
                    val clubCode = data[position].clubCode
                    Log.d("member_clubCode:","${clubCode}")
                    when (item.itemId) {
                        R.id.member_option1 -> {
                            Log.d("memberAdatper_menu:","tab1")
                            // 운영진으로 전환
                            showUpdateDialogBox(view.context, "운영진으로 임명할까요?", "임명하기", "운영진이 되었습니다", user, clubCode, 2,fragment)
                            Log.d("getclickedMeetinghome:","운영진 위임")
                            true

                        }

                        R.id.member_option2 -> {
                            Log.d("memberAdatper_menu:","tab2")
                            // 일반회원으로 전환
                            showUpdateDialogBox(view.context, "일반 회원으로 전환할까요?", "전환하기", "일반 회원이 되었습니다", user, clubCode, 3,fragment)
                            Log.d("getclickedMeetinghome:","일반 전환")
                            true
                        }

                        R.id.member_option3 -> {

                            // 모임장 위임하기
                            showLeaderUpdateDialogBox(view.context,"모임장 권한을 위임하시겠습니까?","위임하기", "권한이 위임되었습니다", user, clubCode, 1,fragment)
                            Log.d("getclickedMeetinghome:","모임장 위임")

                            true
                        }
                        R.id.member_option4 -> {

                            if(data[position].userId == clubLeader){
                                showAlertDialogBox(view.context, "모임장은 내보낼 수 없습니다","확인")
                            }else{
                                Log.d("getclickedMeetinghome:","강제 회원 탈퇴")
                                showDeleteDialogBox(view.context,"이 회원을 모임에서 내보낼까요?","내보내기","회원을 내보냈습니다",user, clubCode,fragment)
                            }


                            true
                        }
                        else -> false
                    }
                }

                popupMenu.show()
            }
        }else{
            holder.btnMore.visibility = INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
