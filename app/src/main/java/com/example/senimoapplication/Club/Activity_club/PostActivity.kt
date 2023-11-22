package com.example.senimoapplication.Club.Activity_club

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senimoapplication.Club.VO.PostVO
import com.example.senimoapplication.Club.VO.WritePostResVO
import com.example.senimoapplication.Club.adapter.PostAdapter
import com.example.senimoapplication.Club.fragment.BoardFragment
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityMakeScheduleBinding
import com.example.senimoapplication.databinding.ActivityPostBinding
import com.example.senimoapplication.databinding.FragmentBoardBinding
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp

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
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
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
            val clubCode = clickedMeeting?.club_code.toString()
//            val userId = UserData.userId.toString()
            val userId = intent.getStringExtra("user_id").toString()
            val postContent = binding.etPostContent.text.toString()
            val postImg = imageName.toString()

            Log.d("포스트 회원 아이디", userId)
            Log.d("포스트 모임코드", clubCode)
            writePost(userId, clubCode, postContent, postImg)
        }

        binding.icBack.setOnClickListener {
            finish()
        }

    }

    fun writePost(userId: String, clubCode: String, postContent: String, postImg: String) {
        val service = Server(this).service
        val call = service.writePost(userId, clubCode, postContent, postImg)

        call.enqueue(object : Callback<WritePostResVO> {
            override fun onResponse(call: Call<WritePostResVO>, response: Response<WritePostResVO>) {
                Log.d("writePost", response.toString())
                if (response.isSuccessful) {
                    val writePostRes = response.body()
                    if (writePostRes != null && writePostRes.rows == "success") {
                        Toast.makeText(this@PostActivity, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@PostActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
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
}