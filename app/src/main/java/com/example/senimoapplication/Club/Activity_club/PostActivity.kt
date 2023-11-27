package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.senimoapplication.Club.VO.WritePostResVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.databinding.ActivityPostBinding
import com.example.senimoapplication.server.ImageUploader
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {

    lateinit var binding : ActivityPostBinding
    private var imageUri: Uri? = null // selectedImageUri를 클래스 수준에 선언
    private var imageName: String? = null // 선택된 이미지의 이름을 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // 사진 1장 선택
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                imageUri = uri
                imageName = getFileName(uri)
                binding.imgButton2.setImageURI(uri)
                binding.imgButton2.visibility = ImageView.VISIBLE
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.imgButton2.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.btnNewPost.setOnClickListener {
            // 게시물 등록하고 게시판 화면으로 돌아가기
            val clickedMeeting = intent.getParcelableExtra<MeetingVO>("clickedMeeting")
            //val clubCode = clickedMeeting?.club_code.toString()
//            val userId = UserData.userId.toString()
           // val userId =  PreferenceManager.getUser(this)?.user_id.toString()//intent.getStringExtra("user_id").toString()
            //val postContent = binding.etPostContent.text.toString()
            val writePostResVO = WritePostResVO(
                rows = "",
                userId = PreferenceManager.getUser(this)?.user_id.toString(),
                clubCode =clickedMeeting?.club_code.toString(),
                postContent =binding.etPostContent.text.toString(),
                postImg = imageName.toString()
            )
            Log.d("포스트 보내는값", writePostResVO.toString())
            // 이미지 URI가 있으면 이미지와 함께 일정 정보 전송
            imageUri?.let{
                val imagePart = ImageUploader(this).prepareImagePart(it)
                writePost(writePostResVO,imagePart)
            } ?: run {
            // 이미지 URI가 없는 경우에는 일정 정보만 전송
            writePost(writePostResVO,null)
        }


            // 게시글 등록 후 ClubActivity로 이동
            val intent = Intent(this@PostActivity, ClubActivity::class.java)
            intent.putExtra("clickedMeeting", clickedMeeting) // clickedMeeting 객체를 Intent에 추가하여 ClubActivity로 전달
            intent.putExtra("user_id", PreferenceManager.getUser(this)?.user_id.toString())
            startActivity(intent)
            finish()
        }

        binding.icBack.setOnClickListener {
            finish()
        }

    }

    fun writePost(writePostResVO: WritePostResVO,imagePart: MultipartBody.Part?) {
        val writePostJson = Gson().toJson(writePostResVO)
        val writePostBody =
            writePostJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()) //변환된 JSON 문자열을 RequestBody 객체로 만듭니다.
        Log.d("writePostJson", writePostJson.toString())
        Log.d("writePostBody", writePostBody.toString())
        val service = Server(this).service
        val call = service.writePost(writePostResVO,imagePart)

        call.enqueue(object : Callback<WritePostResVO> {
            override fun onResponse(call: Call<WritePostResVO>, response: Response<WritePostResVO>) {
                Log.d("writePost", response.toString())
                if (response.isSuccessful) {
                    val writePostRes = response.body()
                    if (writePostRes != null && writePostRes.rows == "success") {
                        Toast.makeText(this@PostActivity, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("writePost", "not success")
                    }
                }
            }

            override fun onFailure(call: Call<WritePostResVO>, t: Throwable) {
                Log.e("PostActivity", "writePost 네트워크 요청실패", t)
            }

        })
    }

    // 이미지 URI에서 파일 이름을 추출하는 함수
    fun getFileName(uri: Uri):String?{
        var imageName: String? = null
        val cursor = contentResolver.query(uri,null,null,null,null)
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
}