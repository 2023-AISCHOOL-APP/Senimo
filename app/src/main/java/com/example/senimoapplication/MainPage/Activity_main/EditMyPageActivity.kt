package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.senimoapplication.Login.adapter.DongAdapter
import com.example.senimoapplication.Login.adapter.GuAdapter
import com.example.senimoapplication.MainPage.VO_main.MyPageVO
import com.example.senimoapplication.MainPage.fragment_main.MypageFragment
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityEditMyPageBinding
import com.example.senimoapplication.server.Retrofit.ApiService
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class EditMyPageActivity : AppCompatActivity() {

    // private lateinit var myProfile: MyPageVO // MyPageVO 객체를 담을 변수
    private var imageUri: Uri? = null // 클래스 수준 변수로 선언

    lateinit var GuAdapter : GuAdapter
    lateinit var DongAdapter: DongAdapter
    lateinit var binding: ActivityEditMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_page)

        binding = ActivityEditMyPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // PreferenceManager를 통해 현재 사용자의 데이터 가져오기
        val userData = PreferenceManager.getUser(this@EditMyPageActivity)
        val userId =  userData?.user_id
        userId?.let {
            // 로그로 userData 확인
            Log.d("EditMyPageActivity", "가져온 데이터: $userData")

            Glide.with(this)
                .load(userData?.user_img)
                .placeholder(R.drawable.animation_loading)
                .error(R.drawable.ic_profile_circle)
                .centerCrop()
                .into(binding.imgMEditMypageImg)

            binding.etMUserName.setText(userData?.user_name)
            binding.etMUserBirth.setText(userData?.birth_year.toString())
            binding.etMGender.setText(userData?.gender)
            binding.etMMyPageIntro.setText(userData?.user_introduce)
        } ?: run {
            // userData가 null인 경우 로그 출력
            Log.e("EditMyPageActivity", "No user data available")
        }


//        myProfile = intent.getParcelableExtra<MyPageVO>("myProfileData") ?: MyPageVO()
//        // 인텐트에서 소개글 길이 받기
//        val introLength = intent.getIntExtra("introLength", 0)
//        binding.tvMLetterCntMyPage.text = introLength.toString()

        // 사진 1장 선택
        val pickMediaMain = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker_main","Selected URI: $uri")
//                binding.imgMEditPhoto.setImageURI(uri)
//                binding.imgMEditPhoto.visibility = ImageView.VISIBLE

                imageUri = uri // 클래스 수준 변수에 URI 저장
                Glide.with(this)
                    .load(imageUri)
                    .error(R.drawable.ic_profile_circle) // 로드 실패 시 기본 이미지
                    .into(binding.imgMEditMypageImg)

            } else {
                Log.d("PhotoPicker_main", "No media selected")
            }
        }

//        myProfile = intent.getParcelableExtra<MyPageVO>("myProfileData") ?: MyPageVO()
//        // 뷰에 데이터 설정
//        Glide.with(this).load(myProfile.img).into(binding.imgMEditMypageImg)
//        binding.etMUserName.setText(myProfile.name)
//        binding.etMUserBirth.setText(myProfile.birth.toString())
//        binding.etMGender.setText(myProfile.gender)
//        binding.etMMyPageIntro.setText(myProfile.intro)


        binding.imgMEditPhoto.setOnClickListener {
            pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }




        val gwangjuDistricts = ArrayList<String>()
        val gwangsanList = arrayListOf<String>("고룡동","광산동","남산동","내산동","대산동","덕림동","도덕동","도산동","도천동","도호동","동림동","동산동","동호동","두정동","등임동","명도동",
            "명화동","박호동","복룡동","본덕동","북산동","비아동","사호동","산막동","산수동","산월동","산정동","삼거동","삼도동","서봉동","선동","선암동","소촌동","송대동",
            "송산동","송정동","송촌동","송치동","송학동","수완동","신가동","신동","신룡동","신창동","신촌동","쌍암동","안청동","양동","양산동","어룡동","연산동","오산동",
            "오선동","오운동","옥동","왕동","요기동","용곡동","용동","용봉동","우산동","운남동","운수동","월계동","월곡동","월전동","유계동","임곡동","장덕동","장록동",
            "장수동","지산동","지정동","지죽동","지평동","진곡동","하남동","하산동","황룡동","흑석동")
        val southList = arrayListOf<String>("구동","구소동","노대동","대지동","덕남동","도금동","방림동","백운동","봉선동","사동","서동","석정동","송하동","승촌동","신장동","압촌동","양과동",
            "양림동","양촌동","원산동","월산동","월성동","이장동","임암동","주월동","지석동","진월동","칠석동","행암동","회장동")
        val eastList = arrayListOf<String>("계림동", "광산동", "궁동", "금남로", "금동", "남동", "내남동", "대의동", "대인동", "동명동", "불로동", "산수동", "서석동", "선교동", "소태동",
            "수기동","용산동","용연동","운림동","월남동","장동","지산동","충장로","학동","호남동","황금동")
        val northList = arrayListOf<String>("각화동","금곡동","누문동","대촌동","덕의동","동림동","두암동","망월동","매곡동","문흥동","본촌동","북동","삼각동","생용동","수곡동","신안동","신용동",
            "양산동","연제동","오룡동","오치동","용강동","용두동","용봉동","용전동","우산동","운암동","운정동","월충동","유동","일곡동","임동","장등동","중흥동","지야동","청풍동",
            "충효동","태령동","풍향동","화암동","효령동")
        val westList = arrayListOf<String>("광천동","금호동","내방동","농성동","덕흥동","동천동","마륵동","매월동","벽진동","상무동","서창동","세하동","쌍촌동","양동","용두동","유촌동","치평동",
            "풍암동","화정동")

        gwangjuDistricts.addAll(gwangsanList)
        gwangjuDistricts.addAll(southList)
        gwangjuDistricts.addAll(eastList)
        gwangjuDistricts.addAll(northList)
        gwangjuDistricts.addAll(westList)

        // GuAdapter 설정
        val editGuList = arrayListOf<String>("광주 전체","광산구","남구","동구","북구","서구")
        val GuAdapter = GuAdapter(R.layout.gu_list, editGuList, applicationContext)
        binding.rvMEditGu.adapter = GuAdapter
        binding.rvMEditGu.layoutManager = LinearLayoutManager(this)

        // 현재 선택된 구 설정
        // val selectedGuIndex = editGuList.indexOf(myProfile.gu)
        val selectedGuIndex = editGuList.indexOf(userData?.user_gu)

        // 현재 선택된 동 설정
        val selectedDongIndex : Int



        // DongAdapter 설정
        DongAdapter = DongAdapter(R.layout.dong_list, gwangjuDistricts, applicationContext)
        binding.rvMEditDong.adapter = DongAdapter

        val girdLayoutManager = GridLayoutManager(applicationContext, 2)
        binding.rvMEditDong.layoutManager = girdLayoutManager



        GuAdapter.itemClickListener = object : GuAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = editGuList[position]

                // rvGu의 아이템을 클릭할 때마다 DongAdapter를 새로 업데이트
                when (item) {
                    "광주 전체" -> {
                        DongAdapter.updateData(gwangjuDistricts)
                    }
                    "광산구" -> {
                        DongAdapter.updateData(gwangsanList)
                    }
                    "남구" -> {
                        DongAdapter.updateData(southList)
                    }
                    "동구" -> {
                        DongAdapter.updateData(eastList)
                    }
                    "북구" -> {
                        DongAdapter.updateData(northList)
                    }
                    "서구" -> {
                        DongAdapter.updateData(westList)
                    }
                    else -> {
                        // 기본적으로 allList를 보여줌
                        DongAdapter.updateData(gwangjuDistricts)
                    }
                }
                binding.rvMEditDong.smoothScrollToPosition(0)
            }
        }

        if (selectedGuIndex != -1) {
            GuAdapter.selectedPosition = selectedGuIndex
            GuAdapter.notifyDataSetChanged()

            // 사용자의 현재 구 위치에 따라 동 목록 설정
            val currentDongList = when (userData?.user_gu) {
                "광산구" -> gwangsanList
                "남구" -> southList
                "동구" -> eastList
                "북구" -> northList
                "서구" -> westList
                else -> gwangjuDistricts // 기본값
            }

            // DongAdapter 업데이트
            DongAdapter.updateData(currentDongList)

            // 현재 선택된 동 위치 확인 및 설정
            // selectedDongIndex = currentDongList.indexOf(myProfile.dong)
            selectedDongIndex = currentDongList.indexOf(userData?.user_dong)
            if (selectedDongIndex != -1) {
                DongAdapter.selectedPosition = selectedDongIndex
                DongAdapter.notifyDataSetChanged()
            }
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val intent = Intent(this@EditMyPageActivity, MainActivity::class.java)
                intent.putExtra("selected_tab", "M_tab4")
                startActivity(intent)
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)

        // 회원 소개글 글자 수 제한
        var isUserIntroLimitExceeded = false

        binding.etMMyPageIntro.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val currentLength = s?.length ?: 0
                binding.tvMLetterCntMyPage.text = currentLength.toString()
                if (currentLength >= 300) {
                    Toast.makeText(this@EditMyPageActivity, "300자 이내로 입력해주세요.", Toast.LENGTH_SHORT).show()
                    binding.etMMyPageIntro.text.delete(start, start+count)
                    isUserIntroLimitExceeded = true
                }else{
                    isUserIntroLimitExceeded = false
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val currentLength = s?.length ?:0
                binding.tvMLetterCntMyPage.text = currentLength.toString()
            }
        })



        binding.btnMSave.setOnClickListener {
            // 사용자 입력 데이터 가져오기
            val updatedName = binding.etMUserName.text.toString()
            val updatedBirth = binding.etMUserBirth.text.toString().toIntOrNull() ?: 0
            val updatedGender = binding.etMGender.text.toString()
            val updatedIntro = binding.etMMyPageIntro.text.toString()

            // GuAdapter에서 선택된 항목 가져오기
            val selectedGu = GuAdapter.getSelectedItem()
            val selectedDong = DongAdapter.getSelectedDongName()
            val imageUriString = imageUri?.toString() ?: "" // URI를 String으로 변환

            // 사용자가 입력한 데이터로 myProfile 객체를 업데이트

            val userData = PreferenceManager.getUser(this@EditMyPageActivity)
            val userId =  userData?.user_id

            val updateProfile = MyPageVO(
                img = imageUriString,
                name = updatedName,
                gu = selectedGu,
                dong = selectedDong,
                birth = updatedBirth,
                gender = updatedGender,
                intro = updatedIntro,
                userId = userId.toString()
                // badges = listOf() // 뱃지 정보는 현재 상황에 맞게 설정
            )

            // 로그 출력
            Log.d("EditProfile","수정된 프로필 정보 : $updateProfile ")


            val service = Server(this).service
            val call = service.updateUserProfile(updateProfile)

            call.enqueue(object : retrofit2.Callback<MyPageVO> {
                override fun onResponse(call: Call<MyPageVO>, response: Response<MyPageVO>) {
                    if (response.isSuccessful) {
                        val returnIntent = Intent()
                        returnIntent.putExtra("updatedProfileData", response.body())
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    } else {
                        Log.d("updatedProfileData", "not success")
                    }
                }

                override fun onFailure(call: Call<MyPageVO>, t: Throwable) {
                    Log.e("EditMyPageActivity", "updqtedProfileData 네트워크 요청실패", t)
                }

            })

            val intent = Intent(this@EditMyPageActivity, MainActivity::class.java)
            intent.putExtra("selected_tab","M_tab4")
            startActivity(intent)
            finish()
        }

        // 뒤로가기 버튼
        binding.imgMBackbtnToFrag4.setOnClickListener {
            val intent = Intent(this@EditMyPageActivity,MainActivity::class.java)
            // 탭 4를 선택한 것으로 설정
            intent.putExtra("selected_tab","M_tab4")
            startActivity(intent)
            finish()
        }






    }


}



