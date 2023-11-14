package com.example.senimoapplication.Club.Activity_club

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Club.adapter.PostAdapter
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




    }
}