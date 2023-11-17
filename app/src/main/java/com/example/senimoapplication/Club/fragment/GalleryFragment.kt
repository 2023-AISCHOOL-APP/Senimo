package com.example.senimoapplication.Club.fragment


import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.Club.adapter.GalleryAdapter
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding

    // ActivityResultLauncher를 프래그먼트의 프로퍼티로 선언
    private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActivityResultLauncher 초기화
        pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                if (uris.isNotEmpty()) {
                    Log.d("clickPhotoPicker", "Number of items selected: ${uris.size}")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
    }

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

        // 여기에서 갤러리 데이터 및 어댑터를 설정합니다.
        val photoList: ArrayList<GalleryVO> = ArrayList()
        val adapter = GalleryAdapter(requireContext(), R.layout.photo_list, photoList)

        // 갤러리 데이터 추가
        photoList.add(GalleryVO("dfsdfsdfs", 1, "양희준", "2023-11-22T12:00:08.123Z", R.drawable.img_sample))
        photoList.add(GalleryVO("dfsdfsdfs", 2, "김도운", "2023-11-22T12:00:08.123Z", R.drawable.img_sample2))
        photoList.add(GalleryVO("dfsdfsdfs", 2, "국지호", "2023-11-21T12:00:08.123Z'", R.drawable.img_sample3))
        photoList.add(GalleryVO("dfsdfsdfs", 2, "최효정", "2023-10-22T12:00:08.123Z'", R.drawable.img_sample4))

        // 리사이클러뷰에 어댑터 설정
        binding.rvGallery.adapter = adapter
        binding.rvGallery.layoutManager = GridLayoutManager(requireContext(), 3)


        // 포스트가 없을 경우 안내 메시지 표시
        if (photoList.isEmpty()) {
            binding.rvGallery.visibility = View.GONE
            binding.tvAnnounceMainPhoto.visibility = View.VISIBLE
            binding.tvAnnounceSubPhoto.visibility = View.VISIBLE
        } else {
            binding.rvGallery.visibility = View.VISIBLE
            binding.tvAnnounceMainPhoto.visibility = View.GONE
            binding.tvAnnounceSubPhoto.visibility = View.GONE
        }

        // 이미지 추가 버튼 클릭 이벤트 처리
        binding.imgFloatingNewPhoto.setOnClickListener {
            Log.d("click", "버튼 클릭")
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        return binding.root
    }
}
