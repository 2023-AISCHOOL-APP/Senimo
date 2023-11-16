package com.example.senimoapplication.Club.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.Club.Activity_club.PhotoActivity
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.Club.adapter.GalleryAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentGalleryBinding
import com.example.senimoapplication.databinding.FragmentPhotoViewBinding


class GalleryFragment : Fragment() {
    lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val view = binding.root

        val photoList : ArrayList<GalleryVO> = ArrayList()
        val adapter = GalleryAdapter(requireContext(), R.layout.photo_list, photoList)

        photoList.add(GalleryVO("dfsdfsdfs",1,"양희준","2023-11-22'T'12:00:08.SSS'Z'",12,R.drawable.img_sample))
        photoList.add(GalleryVO("dfsdfsdfs",2,"김도운","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",4,R.drawable.img_sample))
        photoList.add(GalleryVO("dfsdfsdfs",2,"국지호","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",7,R.drawable.img_sample))
        photoList.add(GalleryVO("dfsdfsdfs",2,"최효정","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",9,R.drawable.img_sample))

        binding.rvGallery.adapter = adapter
        binding.rvGallery.layoutManager = GridLayoutManager(requireContext(),3)

        // PostVO 리스트가 비어있는 경우 Announce 텍스트를 보여줌
        if (photoList.isEmpty()) {
            binding.rvGallery.visibility = View.GONE
            binding.tvAnnounceMainPhoto.visibility = View.VISIBLE
            binding.tvAnnounceSubPhoto.visibility = View.VISIBLE
        } else {
            binding.rvGallery.visibility = View.VISIBLE
            binding.tvAnnounceMainPhoto.visibility = View.GONE
            binding.tvAnnounceSubPhoto.visibility = View.GONE
        }




        binding.imgFloatingNewPhoto.setOnClickListener {
           //
        }


        return view
    }

}