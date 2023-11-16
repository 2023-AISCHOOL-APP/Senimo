package com.example.senimoapplication.MainPage.Activity_main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.MainPage.VO_main.MeetingVO
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityCreateMeetingBinding

class CreateMeetingActivity : AppCompatActivity() {

    lateinit var binding : ActivityCreateMeetingBinding
    private var imageUri: Uri? = null // selectedImageUri를 클래스 수준에 선언

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
        val view = binding.root
        setContentView(view)

        // 사진 1장 선택
        val pickMediaMain = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker_main","Selected URI: $uri")
                binding.imgMButton.setImageURI(uri)
                binding.imgMButton.visibility = ImageView.VISIBLE

                // 이미지를 선택한 후에 URI를 변수에 저장
                val imageUri = uri


            } else {
                Log.d("PhotoPicker_main", "No media selected")
            }
        }

        binding.imgMButton.setOnClickListener {
            pickMediaMain.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            intent.putExtra("selected_tab","tab2")  // "tab2"는 Fragment2를 나타냅니다
            startActivity(intent)
            finish()
        }

        // val setMeetingList : ArrayList<MeetingVO> = ArrayList()

        var isMeetingTitleLimitExceeded = false
        var isMeetingIntroLimitExceeded = false


        // 모임 이름 - 입력 글자 수 제한
        binding.etMeetingName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전
                val currentLength = charSequence?.length ?: 0
                if (currentLength >= 20) {
                    Toast.makeText(this@CreateMeetingActivity, "20자 이내로 입력해주세요", Toast.LENGTH_SHORT).show()
                    // 입력이 20자를 넘으면 입력을 취소
                    binding.etMeetingName.text.delete(start, start + count)
                    isMeetingTitleLimitExceeded = true
                } else {
                    isMeetingTitleLimitExceeded = false
                }

            }
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력 중
            }

            override fun afterTextChanged(editable: Editable?) {
                // 입력 후
//                val currentLength = editable?.length ?: 0
//                binding.tvMLetterCnt.text = currentLength.toString()
//                if (currentLength >= 20 && !isMeetingTitleLimitExceeded) {
//                    Toast.makeText(this@CreateMeetingActivity, "20자 이내로 입력해주세요", Toast.LENGTH_SHORT).show()
//                    isMeetingTitleLimitExceeded = true
//                } else if (currentLength < 20) {
//                    isMeetingTitleLimitExceeded = false
//                }
            }
        })

        // 모임 소개 - 입력 글자 수 제한
        binding.etMeetingIntro.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전
                val currentLength = charSequence?.length ?: 0
                if (currentLength >= 300) {
                    Toast.makeText(this@CreateMeetingActivity, "300자 이내로 입력해주세요", Toast.LENGTH_SHORT).show()
                    // 입력이 300자를 넘으면 입력을 취소
                    binding.etMeetingIntro.text.delete(start, start + count)
                    isMeetingIntroLimitExceeded = true
                } else {
                    isMeetingIntroLimitExceeded = false
                }
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력 중
            }

            override fun afterTextChanged(editable: Editable?) {
                // 입력 후
                val currentLength = editable?.length ?: 0
                binding.tvMLetterCnt2.text = currentLength.toString()
            }

        })




        // 이미지뷰 클릭 이벤트 처리
        binding.imgMCheckExercise.setOnClickListener {
            exerciseChecked = !exerciseChecked
            updateImageState(binding.imgMCheckExercise, exerciseChecked)
        }

        binding.imgMCheckHobby.setOnClickListener {
            hobbyChecked = !hobbyChecked
            updateImageState(binding.imgMCheckHobby, hobbyChecked)
        }

        binding.imgMCheckConcert.setOnClickListener {
            concertChecked = !concertChecked
            updateImageState(binding.imgMCheckConcert, concertChecked)
        }

        binding.imgMCheckTrip.setOnClickListener {
            tripChecked = !tripChecked
            updateImageState(binding.imgMCheckTrip, tripChecked)
        }

        binding.imgMCheckSelfimprovement.setOnClickListener {
            selfImprovementChecked = !selfImprovementChecked
            updateImageState(binding.imgMCheckSelfimprovement, selfImprovementChecked)
        }

        binding.imgMCheckFinancial.setOnClickListener {
            financialChecked = !financialChecked
            updateImageState(binding.imgMCheckFinancial, financialChecked)
        }


        // 버튼 누르면 인원 수 변경 시키기 (일정 참가자수 상한선 : 30명)
        var meetingMembers : Int = 0
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




        // 모임 만들기 버튼 클릭 시 정보 취합하기
        binding.btnSetMeeting.setOnClickListener {
            val selectedGu = binding.spMGulist.selectedItem.toString()

            // 선택된 키워드를 담을 리스트
            val selectedKeywordList = ArrayList<String>()

            if (exerciseChecked) {
                selectedKeywordList.add("운동")
            }
            if (hobbyChecked) {
                selectedKeywordList.add("취미")
            }
            if (concertChecked) {
                selectedKeywordList.add("전시/공연")
            }
            if (tripChecked) {
                selectedKeywordList.add("여행")
            }
            if (selfImprovementChecked) {
                selectedKeywordList.add("자기계발")
            }
            if (financialChecked) {
                selectedKeywordList.add("재테크")
            }

            // 선택된 키워드가 하나 이상인 경우에만 MeetingVO에 추가
            if (selectedKeywordList.isNotEmpty()) {
                val meetingKeywords = selectedKeywordList.joinToString("/")
                val meetingVO =
                    MeetingVO(
                        selectedGu,
                        binding.etMeetingName.text.toString(),
                        binding.etMeetingIntro.text.toString(),
                        meetingKeywords,
                        0,
                        binding.tvMAllMember.text.toString().toInt(),
                    )

                // 결과를 설정하고 현재 액티비티를 종료
                val intent = Intent(this@CreateMeetingActivity, ClubActivity::class.java)
                intent.putExtra("meetingVO", meetingVO)
                // setResult(RESULT_OK, intent)
                startActivity(intent)

                // 로그로 모임 정보 출력
                Log.d("CreateMeetingActivity", "새로운 모임 생성: $meetingVO")
                Toast.makeText(this@CreateMeetingActivity,"모임이 생성되었습니다",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@CreateMeetingActivity, "모임 생성에 실패하셨습니다", Toast.LENGTH_SHORT).show()
            }

        }

    }

    // 이미지 상태 업데이트 함수
    private fun updateImageState(imageView: ImageView, isChecked : Boolean) {
        val drawableResId = if (isChecked) R.drawable.ic_checkbox_color else R.drawable.ic_checkbox
        imageView.setImageResource(drawableResId)
    }


}