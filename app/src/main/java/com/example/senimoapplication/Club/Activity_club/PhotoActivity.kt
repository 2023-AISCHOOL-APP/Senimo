package com.example.senimoapplication.Club.Activity_club

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.Club.adapter.ImageViewPagerAdapter
import com.example.senimoapplication.Common.photoUploadTime
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityPhotoBinding

class PhotoActivity : AppCompatActivity() {
    lateinit var binding : ActivityPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val clickedPhoto = intent.getSerializableExtra("clickedPhoto") as? GalleryVO

        if (clickedPhoto != null) {
            when (clickedPhoto.clubRole) {
                1 -> {
                    binding.tvUserLevelPhoto.text = "모임장"
                    binding.tvUserLevelPhoto.setBackgroundResource(R.drawable.user_level_leader)
                }
                2 -> {
                    binding.tvUserLevelPhoto.text = "운영진"
                    binding.tvUserLevelPhoto.setBackgroundResource(R.drawable.user_level_oper)
                }
            }

            binding.tvUserNamePhoto.text = clickedPhoto.userName
            binding.tvUploadTime.text = photoUploadTime(clickedPhoto.uploadedDt)
            binding.tvLikeCnt.text = clickedPhoto.photoLikes.toString()

            var likecnt = clickedPhoto.photoLikes
            var isLiked = false
            binding.imgbtnLike.setOnClickListener {
                if(isLiked){
                    binding.imgbtnLike.setImageResource(R.drawable.ic_heart)
                    isLiked = false
                    likecnt = clickedPhoto.photoLikes
                }else{
                    binding.imgbtnLike.setImageResource(R.drawable.ic_fullheart)
                    isLiked = true
                    likecnt += 1

                }
                binding.tvLikeCnt.text = likecnt.toString()
            }


            binding.imgbtnCancle.setOnClickListener {
                finish()
            }
            binding.imgView.setImageResource(clickedPhoto.imgUri)


        } else {
            // clickedPhoto가 null인 경우 처리 (오류 처리 등)
            // 예를 들어, 오류 메시지를 사용자에게 표시하거나 이전 화면으로 이동하는 등의 동작을 수행할 수 있습니다.
        }
    }
}