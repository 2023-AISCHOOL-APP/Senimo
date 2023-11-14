package com.example.senimoapplication.Club.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.senimoapplication.Club.Activity_club.PhotoActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentBoardBinding
import com.example.senimoapplication.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    lateinit var binding: FragmentGalleryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnFloatingNewPhoto.setOnClickListener {
            val intent = Intent(requireContext(),PhotoActivity::class.java)
            startActivity(intent)
        }



















        return view
    }

}