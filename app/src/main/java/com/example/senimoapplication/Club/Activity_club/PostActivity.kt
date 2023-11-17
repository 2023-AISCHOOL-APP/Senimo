package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Club.adapter.PostAdapter
import com.example.senimoapplication.Club.fragment.BoardFragment
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityMakeScheduleBinding
import com.example.senimoapplication.databinding.ActivityPostBinding
import com.example.senimoapplication.databinding.FragmentBoardBinding
import java.sql.Timestamp

class PostActivity : AppCompatActivity() {

    lateinit var binding : ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // 사진 1장 선택
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.imgButton2.setImageURI(uri)
                binding.imgButton2.visibility = ImageView.VISIBLE
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.imgButton2.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.btnNewPost.setOnClickListener {
            // 게시물 등록하고 게시판 화면으로 돌아가기
            finish()
        }

        binding.icBack.setOnClickListener {
            finish()
        }

    }
}