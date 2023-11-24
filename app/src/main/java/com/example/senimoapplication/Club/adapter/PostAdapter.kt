package com.example.senimoapplication.Club.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.Activity_club.PostActivity
import com.example.senimoapplication.Club.VO.CommentVO
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Club.VO.getReviewResVO
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.Common.showActivityDialogBox
import com.example.senimoapplication.R
import com.example.senimoapplication.server.Server
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostAdapter(val context: Context, val layout: Int, val data: List<PostVO>) : RecyclerView.Adapter<PostAdapter.ViewHolder> (){

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

    private fun fetchReviewData(holder: ViewHolder, postCode: String) {
        val service = Server(context).service
        val call = service.getReview(postCode)
        call.enqueue(object : Callback<getReviewResVO> {
            override fun onResponse(call: Call<getReviewResVO>, response: Response<getReviewResVO>) {
                if (response.isSuccessful) {
                    val reviewList: List<CommentVO>? = response.body()?.data
                    val jsonResponse = Gson().toJson(reviewList) // Convert to JSON string
                    Log.d("댓글 리스트", jsonResponse)
                    if (reviewList != null) {
                        val commentAdapter = CommentAdapter(context, R.layout.comment_list, reviewList)
                        holder.rvComment.adapter = commentAdapter
                        holder.rvComment.layoutManager = LinearLayoutManager(context)
                    }
                } else {
                    // 리뷰 데이터를 받아오지 못했을 때 처리
                }
            }

            override fun onFailure(call: Call<getReviewResVO>, t: Throwable) {
                // 통신 실패 시 처리
            }
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // 기본 세팅
        holder.rvComment.visibility = GONE
        holder.etComment.visibility = GONE
        holder.tvCommentSend.visibility = GONE
        holder.CommentuserProfileImg.visibility = GONE

        // 게시글 이미지 로드 및 표시
        val postImgUrl = data[position].postImg
        if (postImgUrl == null || postImgUrl == "") {
            holder.imgPost.visibility = GONE
        } else {
            Glide.with(context)
                .load(postImgUrl)
                .placeholder(R.drawable.ic_loading6) // 로딩 중 표시될 이미지
                .error(R.drawable.ic_meeting_profile) // 로딩 실패 시 표시될 이미지
                .into(holder.imgPost)
        }

        // 게시글 유저 이미지 로드 및 표시
        val userImgUrl = data[position].userImg
        Glide.with(context)
            .load(userImgUrl)
            .placeholder(R.drawable.ic_loading6) // 로딩 중 표시될 이미지
            .error(R.drawable.ic_profile_circle) // 로딩 실패 시 표시될 이미지
            .into(holder.userProfileImg)

        holder.tvContent.text = data[position].postContent
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
        holder.tvDateTime.text = formatDate(data[position].createdDt)

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

        holder.tvCommentCnt.text = data[position].reviewCount.toString()

        val postCode = data[position].postCode
        fetchReviewData(holder, postCode)

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


