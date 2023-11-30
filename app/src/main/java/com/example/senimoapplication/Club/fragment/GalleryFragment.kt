package com.example.senimoapplication.Club.fragment


import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.Club.VO.loadGalleryVO
import com.example.senimoapplication.Club.adapter.GalleryAdapter
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.FragmentGalleryBinding
import com.example.senimoapplication.server.ImageUploader
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryFragment : Fragment() {
    var clickedMeeting: MeetingVO? = null // MeetingVO? 타입으로 선언
    private var imageName: String? = null // 선택된 이미지의 이름을 저장
    // private var photoList : String? = null
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var adapter: GalleryAdapter
    // ActivityResultLauncher를 프래그먼트의 프로퍼티로 선언
    private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>
    var photoList: ArrayList<loadGalleryVO> = ArrayList()
    private var isUploading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("사진첩 불러오기","on1View")
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        clickedMeeting = activity?.intent?.getParcelableExtra<MeetingVO>("clickedMeeting")
        Log.d("이미지처음받는클럽코드..",clickedMeeting?.club_code.toString())

        // ActivityResultLauncher 초기화
        pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                if (uris.isNotEmpty()) {
                    Log.d("clickPhotoPicker", "Number of items selected: ${uris.size}")
                    Log.d("clickPhotoPicker", "Number of items selected: ${uris}")

                    uploadPhotosToServer(uris)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        // 여기에서 갤러리 데이터 및 어댑터를 설정합니다.


        adapter = GalleryAdapter(requireContext(), R.layout.photo_list, photoList)
        loadGallery()
        // 갤러리 데이터 추가
//        photoList.add(GalleryVO("dfsdfsdfs", 1, "양희준", "2023-11-22T12:00:08.123Z", R.drawable.img_sample))
//        photoList.add(GalleryVO("dfsdfsdfs", 2, "김도운", "2023-11-22T12:00:08.123Z", R.drawable.img_sample2))
//        photoList.add(GalleryVO("dfsdfsdfs", 2, "국지호", "2023-11-21T12:00:08.123Z'", R.drawable.img_sample3))
//        photoList.add(GalleryVO("dfsdfsdfs", 2, "최효정", "2023-10-22T12:00:08.123Z'", R.drawable.img_sample4))

        // 리사이클러뷰에 어댑터 설정
        binding.rvGallery.adapter = adapter
        binding.rvGallery.layoutManager = GridLayoutManager(activity?.applicationContext, 3)


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadGallery() // 프래그먼트가 준비되면 갤러리 데이터 로딩
        Log.d("사진첩 불러오기","onView")

    }

    // 이미지 URI에서 파일 이름을 추출하는 함수
    fun getFileName(uri: Uri):String?{
        var imageName: String? = null
        val cursor = requireActivity().contentResolver.query(uri,null,null,null,null)
        cursor?.use{
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (it.moveToFirst()) {
                    imageName = it.getString(nameIndex)
                }
            }
        }
        return imageName
    }

    // 서버에 업로드 함수
    private fun uploadPhotosToServer(uris: List<Uri>) {
        isUploading = true
        // MultipartBody.Part 리스트 준비
        val parts = uris.map { uri -> ImageUploader(requireContext()).prepareImagePart(uri) }
        // 각 URI에서 파일 이름 추출
        val imageNames = uris.map { uri ->
            getFileName(uri)
        }
        Log.d("이미지입니다parts.",parts.toString())
        Log.d("이미지입니다.",imageNames.toString())
        val userData = PreferenceManager.getUser(requireContext())
        //imageName = getFileName(parts)
        // 사용자 ID와 모임 코드를 GalleryVO 객체에 설정
        Log.d("이미지클럽코드입니다..",clickedMeeting?.club_code.toString())
        val galleryInfo = GalleryVO(
            userImg = userData?.user_img, // 이미지 사진으로 교체
            clubRole = 1,
            userName = userData?.user_name,
            uploadedDt = "",
            imgThumbName = imageNames,
            userId = userData?.user_id,
            clubcode = clickedMeeting?.club_code.toString()
        )
        Log.d("이미지입니다 유저 아이디",userData?.toString()!!)
        val service = Server(requireContext()).service
        val call = service.uploadPhotos(galleryInfo,parts)
        call.enqueue(object : Callback<GalleryVO> {
            override fun onResponse(call: Call<GalleryVO>, response: Response<GalleryVO>) {
                if(response.isSuccessful){
                    val Gallery = response.body()
                    loadGallery()
                    Log.d("GalleryFragment",Gallery.toString())
                    Toast.makeText(requireContext(), "사진이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                }
                isUploading = false
                // 업로드 성공 처리
            }

            override fun onFailure(call: Call<GalleryVO>, t: Throwable) {
                // 업로드 실패 처리
                isUploading = false
            }
        })
    }

    // 사진첩 정보 로드 함수
    private fun loadGallery() { //onComplete: (() -> Unit)? = null
        val clubCode = clickedMeeting?.club_code ?: return
        Log.d("galleryList club_code : ",clubCode.toString())
        val service = Server(requireContext()).service
        val call = service.getGallery(clubCode) // 서버에 사진첩 정보 요청

        call.enqueue(object : Callback<List<loadGalleryVO>> {
            override fun onResponse(call: Call<List<loadGalleryVO>>, response: Response<List<loadGalleryVO>>) {
                if (response.isSuccessful) {
                    val galleryList = response.body() ?: emptyList()
                    if (galleryList.isEmpty() && !isUploading) {
                        // 빈 결과에 대한 처리. 예: 사용자에게 알림 표시
                        Toast.makeText(context, "사진첩이 비어있습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val galleryList = response.body()
                        Log.d("galleryList0", response.toString())
                        Log.d("galleryList1", galleryList?.get(0)?.imgThumbName.toString())
                        val jsonResponse = Gson().toJson(galleryList)
                        //Log.d("galleryList1: ",galleryList[0].imgThumbName.toString())
                        Log.d("galleryList2 : ", jsonResponse.toString())
//                    Log.d("galleryList3: ", jsonResponse.toString())
                        // updateGallery(galleryList) // 받은 데이터로 UI 업데이트
                        if (galleryList != null) {
                            updateGallery(galleryList)
                        }
//                        onComplete?.invoke()
//
                    }
                } else {
                    // 에러 처리
                    Log.d("galleryList fail : ",response.toString())
                    Toast.makeText(requireContext(), "갤러리 정보 로드 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<loadGalleryVO>>, t: Throwable) {
                // 통신 실패 처리
                Toast.makeText(requireContext(), "갤러리 통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateGallery(galleryList: List<loadGalleryVO>) {
        Log.d("galleryList5: ",galleryList.toString())
        adapter.data.clear()
        // photoList = galleryList as ArrayList<loadGalleryVO>
        adapter.data.addAll(galleryList)
        adapter.notifyDataSetChanged()
    }



    // 사진첩 조회 함수

}
