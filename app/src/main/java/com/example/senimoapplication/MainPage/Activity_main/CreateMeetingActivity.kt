package com.example.senimoapplication.MainPage.Activity_main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
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
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityCreateMeetingBinding
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

class CreateMeetingActivity : AppCompatActivity() {

    lateinit var binding : ActivityCreateMeetingBinding
    private var imageUri: Uri? = null // selectedImageUri를 클래스 수준에 선언
    private var imageName: String? = null // 선택된 이미지의 이름을 저장
    private var selectedKeyword : String? = null // 선택된 키워드를 저장하는 변수
    var meetingMembers: Int = 30

    val Meetinginfo: ArrayList<MeetingVO> = ArrayList()

    // 이미지뷰 클릭 상태를 추적하기 위한 변수들
    private var exerciseChecked = false
    private var hobbyChecked = false
    private var concertChecked = false
    private var tripChecked = false
    private var selfImprovementChecked = false
    private var financialChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent_meetingVO: MeetingVO? = intent.getParcelableExtra("MeetingVO")
        val title: String? = intent.getStringExtra("title")
        val btnTitle: String? = intent.getStringExtra("btnTitle")

        Log.d("click", "$intent_meetingVO")
        setupKeywordImageViews()
        if (intent_meetingVO != null) {
            Log.d("시작부분 확인", "모임수정하는부분")

            // 모임 수정하기
            binding.tvMToptitle.text = title
            binding.btnSetMeeting.text = btnTitle
            binding.tvMLetterCnt1.text = intent_meetingVO.title.length.toString()
            binding.tvMLetterCnt2.text = intent_meetingVO.content.length.toString()

            setSelectedKeyword(intent_meetingVO.keyword)
            intent_meetingVO.title?.let { title ->
                binding.etMeetingName.text = Editable.Factory.getInstance().newEditable(title)
            }
            intent_meetingVO.content?.let { content ->
                binding.etMeetingIntro.text = Editable.Factory.getInstance().newEditable(content)
            }


            // 모임명
            binding.etMeetingName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
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
                                R.color.point
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

            // 모임 소개글
            binding.etMeetingIntro.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
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
                                R.color.point
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

            // 지역 선택 스피너
            val spinner = findViewById<Spinner>(R.id.sp_M_gulist)
            val districtArray = resources.getStringArray(R.array.districts)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districtArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            intent_meetingVO?.gu?.let { selectedDistrict ->
                val position = districtArray.indexOf(selectedDistrict)
                if (position >= 0) {
                    spinner.setSelection(position)
                }
            }

            // 인원 수
            binding.tvMAllMember.text = intent_meetingVO.allMember.toString()
            setClubMembers { updatedMembers -> binding.tvMAllMember.text = updatedMembers.toString() }
            meetingMembers = intent_meetingVO.allMember


            // 뒤로가기 버튼
            binding.ImgMBackbtnToFrag2.setOnClickListener { finish() }

            // 사진 세팅
            binding.imgMButton.visibility = ImageView.VISIBLE
            binding.imgMIcon.visibility = ImageView.INVISIBLE

            // 이미지가 존재하는 경우에만 로드
            if (!intent_meetingVO.imageUri.isNullOrEmpty()) {
                Glide.with(this).load(intent_meetingVO.imageUri).into(binding.imgMButton)
            }
            // 이미지 선택
            val pickMediaMain = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    imageUri = uri
                    imageName = getFileName(uri) // 파일이름 추출
                    intent_meetingVO.imageUri = uri.toString()
                    Glide.with(this).load(uri).into(binding.imgMButton)
                    Log.d("PhotoPicker_main","선택된 URI: $uri")
                } else {
                    Glide.with(this).load(intent_meetingVO.imageUri).into(binding.imgMButton)
                    binding.imgMIcon.visibility = ImageView.VISIBLE
                    Log.d("PhotoPicker_main", "미디어가 선택되지 않았습니다.")
                }
            }
            binding.imgMButton.setOnClickListener {
                pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            // 수정 내용 취합 및 전송
            binding.btnSetMeeting.setOnClickListener {
                val selectedGu = binding.spMGulist.selectedItem.toString()
                val userData = PreferenceManager.getUser(this)
                if (selectedKeyword != null) {
                    // imageName이 null인지 체크하고 값을 설정
                    val finalImageUri = if (imageName != null) imageName.toString() else intent_meetingVO.imageUri
                    Log.d("finalImageUri",finalImageUri.toString())
                    val meetingVO =
                        MeetingVO(
                            gu = selectedGu,
                            title = binding.etMeetingName.text.toString(),
                            content = binding.etMeetingIntro.text.toString(),
                            keyword = selectedKeyword!!,
                            attendance = intent_meetingVO.attendance,
                            allMember = binding.tvMAllMember.text.toString().toInt(),
                            imageUri = finalImageUri, // 이미지 URI 사용
                            club_code = intent_meetingVO.club_code, // db에서 uuid로 생성된 값으로 저장되서 MeetingVO형식 맞추기위해 사용한값
                            userId = userData?.user_id // 로그인한 사용자 id 정보 받아와야함
                        )
                    Log.d("click 모임 수정 정보 전송", meetingVO.toString())
                    imageUri?.let{
                        val imagePart = ImageUploader(this).prepareImagePart(it)
                        modifyMeeting(meetingVO,imagePart)
                        Log.d("click 모임 수정 정보 전송완료", imagePart.toString())
                    } ?: run {
                        // 이미지 URI가 없는 경우에는 일정 정보만 전송
                        modifyMeeting(meetingVO,null)
                    }
                }
            }

        } // 모임생성
        else {
            Log.d("시작부분 확인", "else로 시작")
            // 모임 생성 하기
            val pickMediaMain = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    imageUri = uri
                    imageName = getFileName(uri) // 파일이름 추출
                    Glide.with(this).load(uri).into(binding.imgMButton)
                    Log.d("PhotoPicker_main","Selected URI: $uri")
                    binding.imgMButton.setImageURI(uri)
                    binding.imgMButton.visibility = ImageView.VISIBLE
                    binding.imgMIcon.visibility = ImageView.INVISIBLE
                } else {
                    Log.d("PhotoPicker_main", "No media selected")
                }
            }
            binding.imgMButton.setOnClickListener {
                pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            // 지역 선택 스피너
            val spinner = findViewById<Spinner>(R.id.sp_M_gulist)
            val districtArray = resources.getStringArray(R.array.districts)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districtArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // 뒤로가기 버튼
            binding.ImgMBackbtnToFrag2.setOnClickListener {
                val intent = Intent(this@CreateMeetingActivity, MainActivity::class.java)
                intent.putExtra("selected_tab", "tab2")  // "tab2"는 Fragment2를 나타냅니다
                startActivity(intent)
                finish()
            }

            // 모임명
            binding.etMeetingName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
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
                                R.color.point
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

            // 모임 소개글
            binding.etMeetingIntro.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
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
                                R.color.point
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

            setSelectedKeyword("운동")
            updateKeywordCheckState()

            // 버튼 누르면 인원수 변경
            setClubMembers { updatedMembers ->
                binding.tvMAllMember.text = updatedMembers.toString()
            }

            // 뒤로가기 버튼
            binding.ImgMBackbtnToFrag2.setOnClickListener {
                val intent = Intent(this@CreateMeetingActivity, MainActivity::class.java)
                intent.putExtra("selected_tab", "M_tab2")
                startActivity(intent)
                finish()
            }

            // 모임 만들기 버튼 클릭 시 정보 취합하기
            binding.btnSetMeeting.setOnClickListener {
                Log.d("CreateMeeting", "버튼 클릭됨") // 버튼 클릭 로그
                val selectedGu = binding.spMGulist.selectedItem.toString()
                val userData = PreferenceManager.getUser(this)
                if (selectedKeyword != null) {
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
                            userId = userData?.user_id // 로그인한 사용자 id 정보 받아와야함
                        )
                    Log.d("click CreateMeeting 기능",meetingVO.toString())
                    // 이미지 URI가 있으면 이미지와 함께 모임 정보 전송
                    imageUri?.let {
                            val imagePart = ImageUploader(this).prepareImagePart(it)
                            sendMeetingInfo(meetingVO, imagePart)
                            Log.d("imagePart", imagePart.toString())
                    } ?: run {
                        // 이미지 URI가 없는 경우에는 모임 정보만 전송
                        sendMeetingInfo(meetingVO, null)
                    }
                }
            }
        }
    }
    // 모임 멤버 인원 설정 함수
    fun setClubMembers(onMemberChanged: (Int) -> Unit) {
        // 버튼 누르면 인원 수 변경 시키기 (일정 참가자수 상한선 : 50명)
        val incrementAmount = 5 // 증가 또는 감소할 인원 수

        binding.imgMPlus.setOnClickListener {
            if (meetingMembers + incrementAmount <= 50) {
                meetingMembers += incrementAmount
                binding.tvMAllMember.text = meetingMembers.toString()
            }
        }

        binding.imgMMinus.setOnClickListener {
            if (meetingMembers - incrementAmount >= 0) {
                meetingMembers -= incrementAmount
                binding.tvMAllMember.text = meetingMembers.toString()
            }
        }

    }


    // 키워드 클릭 리스너
    private fun setupKeywordImageViews() {
        // 키워드와 해당하는 이미지뷰를 매핑
        val keywordImageViewMap = mapOf(
            binding.imgMCheckExercise to "운동",
            binding.imgMCheckHobby to "취미",
            binding.imgMCheckConcert to "전시/공연",
            binding.imgMCheckTrip to "여행",
            binding.imgMCheckSelfimprovement to "자기계발",
            binding.imgMCheckFinancial to "재테크"
        )

        // 각 이미지뷰에 동일한 클릭 리스너 설정
        keywordImageViewMap.forEach { (imageView, keyword) ->
            imageView.setOnClickListener { setSelectedKeyword(keyword) }
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
    private fun updateImageState(imageView: ImageView, isChecked : Boolean) {
        val drawableResId = if (isChecked) R.drawable.ic_checkbox_color else R.drawable.ic_checkbox
        imageView.setImageResource(drawableResId)
    }

    // 이미지 URI에서 파일 이름을 추출하는 함수
    private fun getFileName(uri: Uri):String?{
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

    //모임 생성 요청 함수
    private fun sendMeetingInfo(meetingVO: MeetingVO,imagePart: MultipartBody.Part?) {
        val meetingJson = Gson().toJson(meetingVO) //MeetingVO 객체를 JSON 문자열로 변환
        val meetingBody =
            meetingJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()) //변환된 JSON 문자열을 RequestBody 객체로 만듭니다.
        Log.d("meetingJson", meetingJson.toString())
        Log.d("meetingBody", meetingBody.toString())
        val service = Server(this).service
        service.createMeeting(meetingVO,imagePart).enqueue(object : Callback<MeetingVO> {
            override fun onResponse(call: Call<MeetingVO>, response: Response<MeetingVO>) {
                if (response.isSuccessful) {
                    // 서버로부터 성공적으로 응답을 받았을 떄의 처리
                    Log.d("CreateMeetingr",response.toString())
                    Toast.makeText(this@CreateMeetingActivity,"모임이 생성되었습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("CreateMeetingm",meetingVO.toString())
                    Log.d("CreateMeetingm1",response.body().toString())
                    // 모임 생성 후 내 모임 창으로 이동
                    val intent = Intent(this@CreateMeetingActivity, ClubActivity::class.java)
                    intent.putExtra("clickedMeeting", response.body())
                    // setResult(RESULT_OK, intent)
                    startActivity(intent)
                    finish()
                } else {
                    Log.d("모임생성 에러 :", response.body().toString())
                    Log.d("모임생성 에러 :", response.errorBody().toString())
                    //서버로부터 에러 응답을 받았을때 처리
                    Toast.makeText(this@CreateMeetingActivity,"모임이 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MeetingVO>, t: Throwable) {
                // 통신 실패 시 처리
                Log.d("CreateMeeting","서버 연결 실패",t)
            }
        })
    }

    // 모임 수정 처리 함수
    private fun modifyMeeting(meetingVO: MeetingVO,imagePart: MultipartBody.Part?) {
        val service = Server(this).service
        service.modifyMeeting(meetingVO,imagePart).enqueue(object : Callback<MeetingVO> {
            override fun onResponse(call: Call<MeetingVO>, response: Response<MeetingVO>) {
                if (response.isSuccessful) {
                    val modifyResult = response.body()
                    if (modifyResult != null) {
                        Log.d("ModifyMeeting", "Response: ${response.body()}")

                        // 서버 응답에서 반환된 이미지 경로를 사용하여 MeetingVO 객체 업데이트
                        val updatedMeetingVO = meetingVO.copy(
                            imageUri = modifyResult.imageUri
                        )
                        Toast.makeText(this@CreateMeetingActivity, "모임 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()

                        // 성공적으로 수정됐을 때 결과 반환
                        val returnIntent = Intent()
                        returnIntent.putExtra("CreateMeeting", updatedMeetingVO) // 수정된 MeetingVO 객체 반환
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    } else {
                        Toast.makeText(this@CreateMeetingActivity, "모임 정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CreateMeetingActivity, "모임 정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MeetingVO>, t: Throwable) {
                Log.d("ModifyMeeting", "서버 연결 실패: ${t.message}")
                Toast.makeText(this@CreateMeetingActivity, "서버 연결 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

private fun <T> Call<T>.enqueue(callback: Callback<MeetingVO>) {

}
