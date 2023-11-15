package com.example.senimoapplication.Club.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.senimoapplication.Club.Activity_club.PhotoActivity
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.Club.adapter.GalleryAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {
    lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val view = binding.root
        val photosList : ArrayList<GalleryVO> = ArrayList()
        val adapter = GalleryAdapter(requireContext(), R.layout.photo_list, photosList)


        photosList.add(GalleryVO("dfsdfsdfs"))
        photosList.add(GalleryVO("dfsdfsdfs"))
        photosList.add(GalleryVO("dfsdfsdfs"))
        photosList.add(GalleryVO("dfsdfsdfs"))

        binding.rvGallery.adapter = adapter
        binding.rvGallery.layoutManager = GridLayoutManager(requireContext(),3)


        // PostVO 리스트가 비어있는 경우 Announce 텍스트를 보여줌
        if (photosList.isEmpty()) {
            binding.rvGallery.visibility = View.GONE
            binding.tvAnnounceMainPhoto.visibility = View.VISIBLE
            binding.tvAnnounceSubPhoto.visibility = View.VISIBLE
        } else {
            binding.rvGallery.visibility = View.VISIBLE
            binding.tvAnnounceMainPhoto.visibility = View.GONE
            binding.tvAnnounceSubPhoto.visibility = View.GONE
        }


        binding.btnFloatingNewPhoto.setOnClickListener {
            val intent = Intent(requireContext(), PhotoActivity::class.java)
            startActivity(intent)
            // 이미지 여러 장 선택하기
//            val pickMultipleMedia =
//                registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
//                    if (uris.isNotEmpty()) {
//                        Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
//                        for (uri in uris) {
//                            // 각 이미지 URI를 처리하는 코드를 여기에 추가
//                            Log.d("PhotoPicker", "Selected media URI: $uri")
//
//                            // 선택한 이미지 URI를 사용하여 원하는 작업을 수행할 수 있습니다.
//                        }
//                    } else {
//                        Log.d("PhotoPicker", "No media selected")
//                    }
//                }
//
//            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        return view
    }

}