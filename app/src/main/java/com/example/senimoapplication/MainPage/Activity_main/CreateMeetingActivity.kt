package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
//import com.example.senimoapplication.Manifest
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityCreateMeetingBinding
import com.example.senimoapplication.server.ImageUploader
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.UserData
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.Manifest.permission.READ_EXTERNAL_STORAGE

class CreateMeetingActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateMeetingBinding
    private var imageUri: Uri? = null // selectedImageUri를 클래스 수준에 선언
    private var imageName: String? = null // 선택된 이미지의 이름을 저장
    private var selectedKeyword: String? = null // 선택된 키워드를 저장하는 변수


    val Meetinginfo: ArrayList<MeetingVO> = ArrayList()

    // 이미지뷰 클릭 상태를 추적하기 위한 변수들
    private var exerciseChecked = false
    private var hobbyChecked = false
    private var concertChecked = false
    private var tripChecked = false
    private var selfImprovementChecked = false
    private var financialChecked = false


    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
        private const val MANAGE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMeetingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // 사진 1장 선택
        val pickMediaMain =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    // 이미지를 선택한 후에 URI를 변수에 저장
                    imageUri = uri
                    imageName = getFileName(uri) // 파일이름 추출
                    Glide.with(this).load(uri).into(binding.imgMButton)
                    Log.d("PhotoPicker_main", "Selected URI: $uri")


                    // 이미지뷰에 이미지 표시
                    binding.imgMButton.setImageURI(uri)
                    binding.imgMButton.visibility = ImageView.VISIBLE
                    binding.imgMIcon.visibility = ImageView.INVISIBLE


                } else {
                    Log.d("PhotoPicker_main", "No media selected")
                }
            }



        // 기존 권한 요청 코드
        binding.imgMButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    // Android 11 이상에서 MANAGE_EXTERNAL_STORAGE 권한 요청
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
                } else {
                    // 권한이 이미 승인된 경우
                    pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            } else {
                // Android 10 이하에서 기존 권한 체크
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    requestStoragePermission()
                }
            }
        }

//// 사진 선택 버튼에 대한 OnClickListener 설정
//        binding.imgMButton.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    READ_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                requestStoragePermission()
//            } else {
//                // PickVisualMediaRequest의 인스턴스를 생성하여 launch 메서드에 전달
//                pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//            }
//        }


        binding.imgMButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                requestStoragePermission()
            }
        }


        // 스피너 초기화
        val spinner = findViewById<Spinner>(R.id.sp_M_gulist)

        // stings.xml에서 문자열 배열을 가져오기   (광산구, 남구, 동구, 북구, 서구)
        val districtArray = resources.getStringArray(R.array.districts)

        // 어댑터 생성하고 스피너에 설정하기
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districtArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        binding.ImgMBackbtnToFrag2.setOnClickListener {
            val intent = Intent(this@CreateMeetingActivity, MainActivity::class.java)
            intent.putExtra("selected_tab", "tab2")  // "tab2"는 Fragment2를 나타냅니다
            startActivity(intent)
            finish()
        }

        // val setMeetingList : ArrayList<MeetingVO> = ArrayList()


        // 회원 모임명 , 소개글 글자 수 제한

        // 회원 모임명
        binding.etMeetingName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전 필요한 로직 (필요한 경우)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력 중 필요한 로직 (필요한 경우)
            }

            override fun afterTextChanged(s: Editable?) {
                val currentLength = s?.length ?: 0
                binding.tvMLetterCnt1.text = "$currentLength"
                if (currentLength > 20) {
                    binding.tvMLetterCnt1.setTextColor(
                        ContextCompat.getColor(
                            this@CreateMeetingActivity,
                            R.color.main
                        )
                    )
                    binding.tvMNameWarning.visibility = View.VISIBLE // 경고 메시지 표시
                } else {
                    binding.tvMLetterCnt1.setTextColor(
                        ContextCompat.getColor(
                            this@CreateMeetingActivity,
                            R.color.txt_gray70
                        )
                    )
                    binding.tvMNameWarning.visibility = View.GONE // 경고 메시지 숨김
                }
            }
        })

        // 회원 소개글
        binding.etMeetingIntro.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전 필요한 로직 (필요한 경우)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력 중 필요한 로직 (필요한 경우)
            }

            override fun afterTextChanged(s: Editable?) {
                val currentLength = s?.length ?: 0
                binding.tvMLetterCnt2.text = "$currentLength"
                if (currentLength > 300) {
                    binding.tvMLetterCnt2.setTextColor(
                        ContextCompat.getColor(
                            this@CreateMeetingActivity,
                            R.color.main
                        )
                    )
                    binding.tvMIntroWarning.visibility = View.VISIBLE // 경고 메시지 표시
                } else {
                    binding.tvMLetterCnt2.setTextColor(
                        ContextCompat.getColor(
                            this@CreateMeetingActivity,
                            R.color.txt_gray70
                        )
                    )
                    binding.tvMIntroWarning.visibility = View.GONE // 경고 메시지 숨김
                }
            }
        })


        // 이미지뷰 클릭 이벤트 처리
        binding.imgMCheckExercise.setOnClickListener {
            setSelectedKeyword("운동")
        }

        binding.imgMCheckHobby.setOnClickListener {
            setSelectedKeyword("취미")
        }

        binding.imgMCheckConcert.setOnClickListener {
            setSelectedKeyword("전시/공연")
        }

        binding.imgMCheckTrip.setOnClickListener {
            setSelectedKeyword("여행")
        }

        binding.imgMCheckSelfimprovement.setOnClickListener {
            setSelectedKeyword("자기계발")
        }

        binding.imgMCheckFinancial.setOnClickListener {
            setSelectedKeyword("재테크")
        }


        // 버튼 누르면 인원 수 변경 시키기 (일정 참가자수 상한선 : 30명)
        var meetingMembers: Int = 0
        binding.imgMPlus.setOnClickListener {
            if (meetingMembers < 30) {
                meetingMembers += 10
                if (meetingMembers > 30) {
                    meetingMembers = 30
                }
                binding.tvMAllMember.text = meetingMembers.toString()
            }
        }

        binding.imgMMinus.setOnClickListener {
            if (meetingMembers > 0) {
                meetingMembers -= 10
                if (meetingMembers < 0) {
                    meetingMembers = 0
                }
                binding.tvMAllMember.text = meetingMembers.toString()
            }
        }

        binding.ImgMBackbtnToFrag2.setOnClickListener {
            val intent = Intent(this@CreateMeetingActivity, MainActivity::class.java)
            intent.putExtra("selected_tab", "M_tab2")
            startActivity(intent)
            finish()
        }


        // 모임 만들기 버튼 클릭 시 정보 취합하기
        binding.btnSetMeeting.setOnClickListener {
            val selectedGu = binding.spMGulist.selectedItem.toString()

            if (selectedKeyword != null) {
                // 이미지를 업로드하고, 업로드 성공 시 모임 정보를 서버에 보냄
                if (imageUri != null) {
                    val realPath =
                        ImageUploader(this).getRealPathFromURI(contentResolver, imageUri!!)

                }
                val meetingVO =
                    MeetingVO(

                        gu = selectedGu,
                        title = binding.etMeetingName.text.toString(),
                        content = binding.etMeetingIntro.text.toString(),
                        keyword = selectedKeyword!!,
                        attendance = 0, // 참석자수 0명 고정
                        allMember = binding.tvMAllMember.text.toString().toInt(),
                        imageUri = imageName.toString(), // 이미지 URI 사용
                        club_code = "", // db에서 uuid로 생성된 값으로 저장되서 MeetingVO형식 맞추기위해 사용한값
                        userId = UserData.userId // 로그인한 사용자 id 정보 받아와야함
                        // selectedGu,
//                        binding.etMeetingName.text.toString(),
//                        binding.etMeetingIntro.text.toString(),
//                        meetingKeywords,
//                        0,
//                        binding.tvMAllMember.text.toString().toInt(),
//                        binding.imgMButton.toString(),
//                        binding.imgMButton.setImageResource(R.drawable.golf_img)

                    )
                // 이미지 URI가 있으면 이미지와 함께 모임 정보 전송
                imageUri?.let {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val imagePart = ImageUploader(this).prepareImagePart(it)
                        sendMeetingInfo(meetingVO, imagePart)
                        Log.d("imagePart", imagePart.toString())
                    } else {
                        Toast.makeText(this, "저장소 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    // 이미지 URI가 없는 경우에는 모임 정보만 전송
                    sendMeetingInfo(meetingVO, null)
                }
                Log.d("CreateMeeting1", meetingVO.toString())
                // 결과를 설정하고 현재 액티비티를 종료
//                val intent = Intent(this@CreateMeetingActivity, ClubActivity::class.java)
//                intent.putExtra("meetingVO", meetingVO)
//                // setResult(RESULT_OK, intent)
//                startActivity(intent)
            }
            // 로그로 모임 정보 출력
//                Log.d("CreateMeetingActivity", "새로운 모임 생성: $meetingVO")
//                Toast.makeText(this@CreateMeetingActivity,"모임이 생성되었습니다",Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this@CreateMeetingActivity, "모임 생성에 실패하셨습니다", Toast.LENGTH_SHORT).show()
//            }

//            // 선택된 키워드를 담을 리스트
//            val selectedKeywordList = ArrayList<String>()
//
//            if (exerciseChecked) {
//                selectedKeywordList.add("운동")
//            }
//            if (hobbyChecked) {
//                selectedKeywordList.add("취미")
//            }
//            if (concertChecked) {
//                selectedKeywordList.add("전시/공연")
//            }
//            if (tripChecked) {
//                selectedKeywordList.add("여행")
//            }
//            if (selfImprovementChecked) {
//                selectedKeywordList.add("자기계발")
//            }
//            if (financialChecked) {
//                selectedKeywordList.add("재테크")
//            }

//            // 선택된 키워드가 하나 이상인 경우에만 MeetingVO에 추가
//            if (selectedKeywordList.isNotEmpty()) {
//                val meetingKeywords = selectedKeywordList.joinToString("/")
//                val meetingVO =
//                    MeetingVO(
//                        selectedGu,
//                        binding.etMeetingName.text.toString(),
//                        binding.etMeetingIntro.text.toString(),
//                        meetingKeywords,
//                        0,
//                        binding.tvMAllMember.text.toString().toInt(),
//                        binding.imgMButton.toString()
////                        binding.imgMButton.setImageResource(R.drawable.golf_img)
//                    )
//
//                // 결과를 설정하고 현재 액티비티를 종료
//                val intent = Intent(this@CreateMeetingActivity, ClubActivity::class.java)
//                intent.putExtra("meetingVO", meetingVO)
//                // setResult(RESULT_OK, intent)
//                startActivity(intent)
//
//                // 로그로 모임 정보 출력
//                Log.d("CreateMeetingActivity", "새로운 모임 생성: $meetingVO")
//                Toast.makeText(this@CreateMeetingActivity,"모임이 생성되었습니다",Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this@CreateMeetingActivity, "모임 생성에 실패하셨습니다", Toast.LENGTH_SHORT).show()
//            }


        }

    }

    // 선택된 키워드를 설정하고 UI를 업데이트하는 함수
    private fun setSelectedKeyword(keyword: String) {
        selectedKeyword = keyword
        updateKeywordCheckState()
    }

    // 모든 키워드의 상태를 업데이트하는 함수
    private fun updateKeywordCheckState() {
        updateImageState(binding.imgMCheckExercise, "운동" == selectedKeyword)
        updateImageState(binding.imgMCheckHobby, "취미" == selectedKeyword)
        updateImageState(binding.imgMCheckConcert, "전시/공연" == selectedKeyword)
        updateImageState(binding.imgMCheckTrip, "여행" == selectedKeyword)
        updateImageState(binding.imgMCheckSelfimprovement, "자기계발" == selectedKeyword)
        updateImageState(binding.imgMCheckFinancial, "재테크" == selectedKeyword)
    }

    // 이미지 상태 업데이트 함수
    private fun updateImageState(imageView: ImageView, isChecked: Boolean) {
        val drawableResId = if (isChecked) R.drawable.ic_checkbox_color else R.drawable.ic_checkbox
        imageView.setImageResource(drawableResId)
    }

    // 이미지 URI에서 파일 이름을 추출하는 함수
    private fun getFileName(uri: Uri): String? {
        var imageName: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (it.moveToFirst()) {
                    imageName = it.getString(nameIndex)
                }
            }
        }
        return imageName
    }

    //모임생성 요청 함수
    private fun sendMeetingInfo(meetingVO: MeetingVO, imagePart: MultipartBody.Part?) {
        val meetingJson = Gson().toJson(meetingVO) //MeetingVO 객체를 JSON 문자열로 변환
        val meetingBody =
            meetingJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()) //변환된 JSON 문자열을 RequestBody 객체로 만듭니다.
        Log.d("meetingJson", meetingJson.toString())
        Log.d("meetingBody", meetingBody.toString())
        val service = Server(this).service
        service.createMeeting(meetingBody, imagePart).enqueue(object : Callback<MeetingVO> {
            override fun onResponse(call: Call<MeetingVO>, response: Response<MeetingVO>) {
                if (response.isSuccessful) {
                    // 서버로부터 성공적으로 응답을 받았을 떄의 처리
                    Log.d("CreateMeeting", response.toString())
                    Toast.makeText(this@CreateMeetingActivity, "모임이 생성되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("CreateMeetingm", meetingVO.toString())
                    Log.d("CreateMeetingm1", response.body().toString())
                    // 모임 생성 후 내 모임 창으로 이동
                    val intent = Intent(this@CreateMeetingActivity, ClubActivity::class.java)
                    intent.putExtra("CreateMeeting", response.body())
                    // setResult(RESULT_OK, intent)
                    startActivity(intent)
                    finish()
                } else {
                    //서버로부터 에러 응답을 받았을때 처리
                    Toast.makeText(
                        this@CreateMeetingActivity,
                        "모임이 생성에 실패했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MeetingVO>, t: Throwable) {
                // 통신 실패 시 처리
                Log.d("CreateMeeting", "서버 연결 실패", t)
            }
        })
    }


    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "저장소 접근 권한이 부여되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "저장소 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }


//    // 권한 요청 결과 처리
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Log.d("PermissionDebug1", "grantResults: ${grantResults.joinToString()}")
//        Log.d("PermissionDebug1", "grantResults[0]: ${grantResults[0]}")
//        Log.d("PermissionDebug1", "PackageManager.PERMISSION_GRANTED: ${PackageManager.PERMISSION_GRANTED}")
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            Log.d("PermissionDebug2","성공")
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//
//                Log.d("PermissionDebug", "grantResults: ${grantResults.joinToString()}")
//                Log.d("PermissionDebug", "grantResults[0]: ${grantResults[0]}")
//                Log.d("PermissionDebug", "PackageManager.PERMISSION_GRANTED: ${PackageManager.PERMISSION_GRANTED}")
//                Toast.makeText(this, "저장소 접근 권한이 부여되었습니다.", Toast.LENGTH_SHORT).show()
//                // 필요한 작업 수행
//            } else {
//                Toast.makeText(this, "저장소 접근 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//
//    // 권한 요청 메서드
//    private fun requestStoragePermission() {
//        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
//    }


    }
}