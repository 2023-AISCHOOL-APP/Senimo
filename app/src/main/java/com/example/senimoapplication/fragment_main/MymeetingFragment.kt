package com.example.senimoapplication.fragment_main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Activity_main.CreateMeetingActivity
import com.example.senimoapplication.ClubActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.RecyclerItemClickListener
import com.example.senimoapplication.VO_main.MeetingVO
import com.example.senimoapplication.adapter_main.MeetingAdapter


class MymeetingFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mymeeting, container, false)

        val Img_M_Creatmeet_Circle = view.findViewById<ImageView>(R.id.Img_M_Creatmeet_Circle)





        Img_M_Creatmeet_Circle.setOnClickListener {
            val intent = Intent(requireContext(), CreateMeetingActivity::class.java)
            startActivity(intent)
            // activity?.finish()
        }

        return view
    }


}
