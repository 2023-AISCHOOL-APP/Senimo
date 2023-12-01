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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.Activity_club.PostActivity
import com.example.senimoapplication.Club.VO.CommentVO
import com.example.senimoapplication.Club.VO.DeletePostResVO
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Club.VO.WriteReviewResVO
import com.example.senimoapplication.Club.VO.getReviewResVO
import com.example.senimoapplication.Common.PostUpdateListener
import com.example.senimoapplication.Common.formatDate
import com.example.senimoapplication.Common.showPostDialogBox
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.R
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostAdapter(val context: Context, val layout: Int, val data: List<PostVO>, val listener: PostUpdateListener, val clickedMeeting: MeetingVO?) : RecyclerView.Adapter<PostAdapter.ViewHolder> (){
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

    companion object{
        fun deletePostData(activity: Activity, postCode: String, listener: PostUpdateListener) {
            val service = Server(activity).service
            val call = service.deletePost(postCode)
            call.enqueue(object : Callback<DeletePostResVO> {
                override fun onResponse(
                    call: Call<DeletePostResVO>,
                    response: Response<DeletePostResVO>
                ) {
                    Log.d("게시글 삭제", response.toString())
                    if (response.isSuccessful) {
                        val deletePostRes = response.body()
                        if (deletePostRes != null && deletePostRes.rows == "success") {
                            Log.d("deletePost", "${postCode} 삭제 성공")
                            // 삭제 성공 시, 인터페이스를 통해 삭제 이벤트 전달
                            listener.onUpdatePost()
                        } else {
                            Log.d("deletePost", "${postCode} 삭제 실패")
                        }
                    }
                }

                override fun onFailure(call: Call<DeletePostResVO>, t: Throwable) {
                    Log.e("deletePost", "deletePost 네트워크 요청 실패", t)
                }
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postCode = data[position].postCode

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

        Glide.with(context)
            .load(userImgUrl)
            .placeholder(R.drawable.ic_loading6) // 로딩 중 표시될 이미지
            .error(R.drawable.ic_profile_circle) // 로딩 실패 시 표시될 이미지
            .into(holder.CommentuserProfileImg)

        val content = data[position].postContent
        holder.tvContent.text = if (content.length > 40) {
            content.substring(0, 40) + "..."
        } else {
            content
        }

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

        val userData = PreferenceManager.getUser(context)
        val loginedUserId = userData?.user_id
        val postUserId = data[position].userId
        if (loginedUserId == postUserId) {
            holder.imgPostMore.visibility == VISIBLE
        } else {
            holder.imgPostMore.visibility = GONE
        }

        holder.imgPostMore.setOnClickListener { view->
            val popupMenu = PopupMenu(view.context, view)
            val menuInflater = popupMenu.menuInflater

            menuInflater.inflate(R.menu.option_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_option1 -> {
                        // 게시물 수정
                        val intent = Intent(view.context, PostActivity::class.java)
                        intent.putExtra("clickedPost",data[position])
                        intent.putExtra("clickedMeeting", clickedMeeting)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        view.context.startActivity(intent)
                        true
                    }

                    R.id.menu_option2 -> {
                        // 게시물 삭제
                        showPostDialogBox(view.context as Activity,"게시물을 삭제하시겠어요?", "삭제하기", "게시물이 삭제되었습니다.", postCode, listener)
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()

        }

        holder.tvCommentCnt.text = data[position].reviewCount.toString()

        // 리뷰 데이터 가져호는 함수 실행
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
                    holder.tvContent.text = if (content.length > 40) {
                        content.substring(0, 40) + "..."
                    } else {
                        content
                    }

                } else {
                    // 현재 상태가 GONE이면 VISIBLE로 변경
                    holder.rvComment.visibility = VISIBLE
                    holder.etComment.visibility = VISIBLE
                    holder.tvCommentSend.visibility = VISIBLE
                    holder.CommentuserProfileImg.visibility = VISIBLE
                    holder.tvContent.text = content
                }

                // 상태 토글
                isContentVisible = !isContentVisible
            }
        }

        holder.tvCommentSend.setOnClickListener {
            val userData = PreferenceManager.getUser(context)
            val userId = userData?.user_id
            val postCode = data[position].postCode
            val reviewContent = holder.etComment.text.toString()
            val writeReviewResVO = WriteReviewResVO(userId = userId, postCode = postCode, reviewContent = reviewContent)

            writeReview(writeReviewResVO)
            holder.etComment.text.clear()
            listener.onUpdatePost()
        }
    }

    fun writeReview(writeReviewResVO: WriteReviewResVO) {
        val service = Server(context).service
        val call = service.writeReview(writeReviewResVO)
        call.enqueue(object : Callback<WriteReviewResVO> {
            override fun onResponse(
                call: Call<WriteReviewResVO>,
                response: Response<WriteReviewResVO>
            ) {
                if (response.isSuccessful) {
                    val writeReviewRes = response.body()
                    if (writeReviewRes != null) {
                        Log.d("댓글 등록", writeReviewRes.toString())
                        Toast.makeText(context, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("댓글 등록", "댓글 등록 실패")
                    }
                } else {
                    Log.d("댓글 등록", "응답 실패함")
                }
            }

            override fun onFailure(call: Call<WriteReviewResVO>, t: Throwable) {
                Log.e("댓글 등록 요청", "댓글 등록 요청 실패", t)
            }

        })
    }
}


