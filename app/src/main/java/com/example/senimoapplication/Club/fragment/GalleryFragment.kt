package com.example.senimoapplication.Club.fragment


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    // private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>
    lateinit var clubCode : String

    var photoList: ArrayList<loadGalleryVO> = ArrayList()
    private var isUploading = false

    // Fragment 내에서 실행할 함수
    private fun fetchDataFromServer() {
        // 코루틴을 사용하여 비동기 작업 실행
        lifecycleScope.launch(Dispatchers.IO) {
            val spf = activity?.getSharedPreferences("club", Context.MODE_PRIVATE)
            clubCode = spf?.getString("clubcode", "null").toString()
            // 서버에서 이미지 받아오는 코드 (여기에 작성)
            loadGallery(clubCode)
            Log.d("adapter동작","7")
            // 10초 대기
            delay(300) // 10초를 밀리초로 변환하여 지정

            // 이미지 받아오기가 완료된 후에 UI 작업 수행
            launch(Dispatchers.Main) {
                // UI 업데이트 등을 수행 (예: RecyclerView 업데이트)
                adapter.notifyDataSetChanged()
                Log.d("adapter동작","8")
                if (photoList.isEmpty()) {
                    binding.rvGallery.visibility = View.GONE
                    binding.tvAnnounceMainPhoto.visibility = View.VISIBLE
                    binding.tvAnnounceSubPhoto.visibility = View.VISIBLE
                } else {
                    binding.rvGallery.visibility = View.VISIBLE
                    binding.tvAnnounceMainPhoto.visibility = View.GONE
                    binding.tvAnnounceSubPhoto.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("사진첩 불러오기","on1View")
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        // clickedMeeting = activity?.intent?.getParcelableExtra<MeetingVO>("clickedMeeting")
        Log.d("이미지처음받는클럽코드..",clickedMeeting?.club_code.toString())

        val spf = activity?.getSharedPreferences("club", Context.MODE_PRIVATE)
        clubCode = spf?.getString("clubcode", "null").toString()

        // loadGallery(clubCode)
        // 여기에서 갤러리 데이터 및 어댑터를 설정합니다.
        Log.d("photo", photoList.size.toString())
        // loadGallery(clubCode)
        //Thread.sleep(5000)
        adapter = GalleryAdapter(requireContext(), R.layout.photo_list, photoList)

        //updateGallery(photoList)
        // 리사이클러뷰에 어댑터 설정
        binding.rvGallery.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvGallery.adapter = adapter

        // 이미지 추가 버튼 클릭 이벤트 처리
        binding.imgFloatingNewPhoto.setOnClickListener {
            Log.d("click", "버튼 클릭")
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
        loadGallery(clubCode)
        fetchDataFromServer()
        return binding.root
    }

    val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            if (uris.isNotEmpty()) {
               // Log.d("clickPhotoPicker", "Number of items selected: ${uris.size}")
                //Log.d("clickPhotoPicker", "Number of items selected: ${uris}")

                uploadPhotosToServer(uris)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
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
                    loadGallery(clubCode)
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
    private fun loadGallery(clubCode : String) { //onComplete: (() -> Unit)? = null
        // val clubCode = clickedMeeting?.club_code ?: return
        Log.d("galleryList club_code : ",clubCode)
        val service = Server(requireContext()).service
        val call = service.getGallery(clubCode) // 서버에 사진첩 정보 요청

        call.enqueue(object : Callback<List<loadGalleryVO>> {
            override fun onResponse(call: Call<List<loadGalleryVO>>, response: Response<List<loadGalleryVO>>) {
                if (response.isSuccessful) {
                    val galleryList = response.body() ?: emptyList()
                    Log.d("resultGallery", galleryList.size.toString())
                    Log.d("resultGallery", galleryList.size.toString())
                    if (galleryList.isEmpty() && !isUploading) {
                    } else {
                        val galleryList = response.body()
                        Log.d("galleryList0", response.toString())
                        Log.d("galleryList1", galleryList?.get(0)?.imgThumbName.toString())
                        val jsonResponse = Gson().toJson(galleryList)
                        Log.d("galleryList2 : ", jsonResponse.toString())
                        // updateGallery(galleryList) // 받은 데이터로 UI 업데이트
                        Log.d("adapter동작","111111111111---")
                            if (galleryList != null) {
                                updateGallery(galleryList)
                                photoList = galleryList as ArrayList<loadGalleryVO>
                                Log.d("adapter동작","111111111111")

                        }
                    }
                } else {
                    // 에러 처리
                    Log.d("adapter동작","66666666666666")
                    Log.d("galleryList fail : ",response.toString())
                    //Toast.makeText(requireContext(), "갤러리 정보 로드 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<loadGalleryVO>>, t: Throwable) {
                // 통신 실패 처리
                //Toast.makeText(requireContext(), "갤러리 통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateGallery(galleryList: List<loadGalleryVO>) {
        Log.d("galleryList5: ",galleryList.toString())
        adapter.data.clear()
        // photoList = galleryList as ArrayList<loadGalleryVO>
        adapter.data.addAll(galleryList)
        adapter.notifyDataSetChanged()
        Log.d("adapter동작","222222222")
    }
}
