package com.example.senimoapplication.Club.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.Activity_club.MakeScheduleActivity
import com.example.senimoapplication.Club.Activity_club.PostActivity
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.Common.showActivityDialogBox
import com.example.senimoapplication.R
import com.google.android.material.imageview.ShapeableImageView


class PostAdapter(val context: Context, val layout : Int, val data: ArrayList<PostVO>) : RecyclerView.Adapter<PostAdapter.ViewHolder> (){

    val inflater = LayoutInflater.from(context)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvUserName : TextView
        val tvUserLevel : TextView
        val tvContent  : TextView
        val imgPostMore : ImageView
        val tvDateTime : TextView
        val rvComment : RecyclerView
        val tvBtnMore : TextView
        val tvCommentCnt : TextView
        val imgPost : ImageView
        val etComment : EditText
        val CommentuserProfileImg : ShapeableImageView
        val userProfileImg : ShapeableImageView
        val tvCommentSend : TextView
        val tvCommentTitle : TextView
        val postCard : CardView


        init{
            tvUserName = view.findViewById(R.id.tv_P_userName)
            tvUserLevel = view.findViewById(R.id.tv_P_userLevel)
            tvContent = view.findViewById(R.id.tv_P_Content)
            imgPostMore = view.findViewById(R.id.imgPostMore)
            tvDateTime = view.findViewById(R.id.tv_P_DateTime)
            rvComment = view.findViewById(R.id.rvComment)
            tvBtnMore = view.findViewById(R.id.tvBtnMore)
            tvCommentCnt = view.findViewById(R.id.tvCommentCnt)
            imgPost = view.findViewById(R.id.imgPost)
            etComment = view.findViewById(R.id.etComment)
            tvCommentSend = view.findViewById(R.id.tvCommentSend)
            CommentuserProfileImg = view.findViewById(R.id.userProfilecomment)
            userProfileImg = view.findViewById(R.id.userProfile2)
            tvCommentTitle = view.findViewById(R.id.tvCommentTitle)
            postCard = view.findViewById(R.id.postcard)
        }}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // 기본 세팅
        holder.rvComment.visibility = GONE
        holder.etComment.visibility = GONE
        holder.tvCommentSend.visibility = GONE
        holder.CommentuserProfileImg.visibility = GONE

        holder.imgPost.setImageResource(R.drawable.img_sample)
        holder.userProfileImg.setImageResource(R.drawable.img_sample)
        holder.tvContent.text = data[position].post_content
        holder.tvUserName.text = data[position].user_name
        when(data[position].club_role){
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
        holder.tvDateTime.text = formatDate(data[position].created_dt)



        holder.imgPostMore.setOnClickListener { view->
            val popupMenu = PopupMenu(view.context, view)
            val menuInflater = popupMenu.menuInflater

            menuInflater.inflate(R.menu.option_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_option1 -> {
                        // 게시물 수정
                        val intent = Intent(view.context, PostActivity::class.java)
                        view.context.startActivity(intent)
                        true
                    }

                    R.id.menu_option2 -> {
                        // 게시물 삭제
                        showActivityDialogBox(view.context as Activity,"게시물을 삭제하시겠어요?", "삭제하기", "게시물이 삭제되었습니다.")
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()

        }


        holder.tvCommentCnt.text = data[position].reviewed_cnt.toString()


        val commentAdapter = CommentAdapter(context, R.layout.comment_list, data[position].review_content)
        holder.rvComment.adapter = commentAdapter
        holder.rvComment.layoutManager = LinearLayoutManager(context)



        // 게시물 내용, 더보기 버튼, 댓글을 눌렀을 때 게시물 확장하는 코드
        val clickableViews = listOf(holder.tvContent, holder.tvCommentTitle, holder.tvBtnMore, holder.imgPost)

        // 초기 상태는 GONE으로 설정
        var isContentVisible = false

        clickableViews.forEach { view ->
            view.setOnClickListener {
                if (isContentVisible) {
                    // 현재 상태가 VISIBLE이면 GONE으로 변경
                    holder.rvComment.visibility = GONE
                    holder.etComment.visibility = GONE
                    holder.tvCommentSend.visibility = GONE
                    holder.CommentuserProfileImg.visibility = GONE

                } else {
                    // 현재 상태가 GONE이면 VISIBLE로 변경
                    holder.rvComment.visibility = VISIBLE
                    holder.etComment.visibility = VISIBLE
                    holder.tvCommentSend.visibility = VISIBLE
                    holder.CommentuserProfileImg.visibility = VISIBLE
                }

                // 상태 토글
                isContentVisible = !isContentVisible
            }
        }
    }
}


