package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.VO.loadGalleryVO
import com.example.senimoapplication.Common.photoUploadTime
import com.example.senimoapplication.Common.showFragmentDialogBox
import com.example.senimoapplication.R

class PhotoViewAdapter (val activity: AppCompatActivity, val context: Context, val layout : Int, val data : ArrayList<loadGalleryVO>):RecyclerView.Adapter<PhotoViewAdapter.ViewHolder>(){
    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvUserRole : TextView
        val tvUserName : TextView
        val tvUploadTime : TextView
        val tvLikeCnt : TextView
        val imgbtnLike : ImageView
        val imgbtnCancel : ImageView
        val imgUserProfile : ImageView
        val imgView : ImageView
        val imgbtnDelete : ImageView

        init {
            tvUserRole = view.findViewById(R.id.tvUserLevelPhoto)
            tvUserName = view.findViewById(R.id.tvUserNamePhoto)
            tvUploadTime = view.findViewById(R.id.tvUploadTime)
            tvLikeCnt = view.findViewById(R.id.tvLikeCnt)
            imgbtnLike = view.findViewById(R.id.imgbtnLike)
            imgbtnCancel = view.findViewById(R.id.imgbtnCancle)
            imgUserProfile = view.findViewById(R.id.userProfile3)
            imgView = view.findViewById(R.id.imgView)
            imgbtnDelete = view.findViewById(R.id.imgbtnDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewAdapter.ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewAdapter.ViewHolder, position: Int) {
        val photoUserImg = data[position].userImg
        Glide.with(context)
            .load(photoUserImg)
            .placeholder(R.drawable.ic_loading6) // 로딩 중 표시될 이미지
            .error(R.drawable.ic_profile_circle) // 로딩 실패 시 표시될 이미지
            .into(holder.imgUserProfile)

        val item = data[position]
        when (item.clubRole){
            1 -> {
                holder.tvUserRole.text = "모임장"
                holder.tvUserRole.setBackgroundResource(R.drawable.user_level_leader)
            }
            2 ->{
                holder.tvUserRole.text = "운영진"
                holder.tvUserRole.setBackgroundResource(R.drawable.user_level_oper)
            }
            else ->{
                holder.tvUserRole.text = "일반"
                holder.tvUserRole.setBackgroundResource(R.drawable.user_level_basic)
            }
        }
        holder.tvUserName.text = item.userName
        holder.tvUploadTime.text = photoUploadTime(item.uploadedDt)

        holder.imgbtnCancel.setOnClickListener {
            activity?.finish()
        }

        Glide.with(context).load(item.imgThumbName).into(holder.imgView)

        holder.imgbtnDelete.setOnClickListener{
            showFragmentDialogBox(context,"사진을 삭제하시겠습니까?","삭제하기","사진이 삭제되었습니다.")
            // 사진 삭제
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}