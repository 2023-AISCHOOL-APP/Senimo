package com.example.senimoapplication.Club.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
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

        val photoList : ArrayList<GalleryVO> = ArrayList()
        val adapter = GalleryAdapter(requireContext(), R.layout.photo_list, photoList)

        photoList.add(GalleryVO("dfsdfsdfs",1,"양희준","2023-11-22T12:00:08.123Z",R.drawable.img_sample))
        photoList.add(GalleryVO("dfsdfsdfs",2,"김도운","2023-11-22T12:00:08.123Z",R.drawable.img_sample2))
        photoList.add(GalleryVO("dfsdfsdfs",2,"국지호","2023-11-21T12:00:08.123Z'",R.drawable.img_sample3))
        photoList.add(GalleryVO("dfsdfsdfs",2,"최효정","2023-10-22T12:00:08.123Z'",R.drawable.img_sample4))

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

        // 사진 여러장 선택하기
        val pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (uris.isNotEmpty()) {
                    Log.d("clickPhotoPicker", "Number of items selected: ${uris.size}")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }


        binding.imgFloatingNewPhoto.setOnClickListener {
            Log.d("click","버튼 클릭")
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }


        return view
    }
}