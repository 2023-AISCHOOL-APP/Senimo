package com.example.senimoapplication.MainPage.Activity_main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.MainPage.fragment_main.MypageFragment
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityEditMyPageBinding

class EditMyPageActivity : AppCompatActivity() {

    private lateinit var myProfile: MyPageVO // MyPageVO 객체를 담을 변수


    lateinit var binding: ActivityEditMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // myProfile 변수 초기화
        myProfile = MyPageVO(
            "drawable/golf_img.jpg",
            "",
            "",
            0,
            "",
            ""
        )

        binding = ActivityEditMyPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // 뒤로가기 버튼
        binding.imgMBackbtnToFrag4.setOnClickListener {
            val intent = Intent(this@EditMyPageActivity,MainActivity::class.java)
            // 탭 4를 선택한 것으로 설정
            intent.putExtra("selected_tab","M_tab4")
            startActivity(intent)
            finish()
        }


        // 사진 1장 선택
        val pickMediaMain = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker_main","Selected URI: $uri")
//                binding.imgMEditPhoto.setImageURI(uri)
//                binding.imgMEditPhoto.visibility = ImageView.VISIBLE

                // Gride를 사용하여 이미지 로딩
                Glide.with(this@EditMyPageActivity)
                    .load(uri) // 이미지 URI
                    .centerCrop() // 이미지가 ImageView를 가득 채우도록 조정
                    .into(binding.imgMEditMypageImg) // 이미지를 설정할 ImageView

                // 이미지를 선택한 후에 URI를 변수에 저장
                val imageUri = uri


            } else {
                Log.d("PhotoPicker_main", "No media selected")
            }
        }

        binding.imgMEditPhoto.setOnClickListener {
            pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }



    }

}