package com.example.senimoapplication.Club.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.senimoapplication.Club.adapter.GalleryAdapter
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    lateinit var binding: FragmentGalleryBinding

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris: List<Uri>? ->
        // 여기에서 URI 리스트를 처리
        if (uris != null) {
            uploadPhotosToServer(uris)
        }
    }

    private fun uploadPhotosToServer(uris: List<Uri>) {
        // 사진을 서버로 업로드하는 로직 구현
    }

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
        binding.rvGallery.layoutManager = GridLayoutManager(requireContext(), 3)

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
            pickMedia.launch(null) // 여기서 null을 전달하면 기본 설정으로 사진을 선택할 수 있습니다.
        }

        return view
    }
}
