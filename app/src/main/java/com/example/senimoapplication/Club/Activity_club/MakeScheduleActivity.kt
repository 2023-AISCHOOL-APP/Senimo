package com.example.senimoapplication.Club.Activity_club

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.databinding.ActivityMakeScheduleBinding


class MakeScheduleActivity : ComponentActivity() {

    lateinit var binding : ActivityMakeScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMakeScheduleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 사진 1장 선택
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.imgButton.setImageURI(uri)
                binding.imgButton.visibility = ImageView.VISIBLE
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.imgButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val setScheduleList : ArrayList<ScheduleVO> = ArrayList()

        var isTitleLimitExceeded = false
        var isIntroLimitExceeded = false

        // 일정 제목 - 입력 글자 수 제한
        binding.etScheduleName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전
                val currentLength = charSequence?.length ?: 0
                if (currentLength >= 20) {
                    Toast.makeText(this@MakeScheduleActivity, "20자 이내로 입력해주세요", Toast.LENGTH_SHORT).show()
                    // 입력이 20자를 넘으면 입력을 취소
                    binding.etScheduleName.text.delete(start, start + count)
                    isTitleLimitExceeded = true
                } else {
                    isTitleLimitExceeded = false
                }
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력 중
            }

            override fun afterTextChanged(editable: Editable?) {
                // 입력 후
                val currentLength = editable?.length ?: 0
                binding.tvLetterCnt.text = currentLength.toString()
            }
        })

        // 일정 소개 - 입력 글자 수 제한
        binding.etScheduleIntro.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전
                val currentLength = charSequence?.length ?: 0
                if (currentLength >= 300) {
                    Toast.makeText(this@MakeScheduleActivity, "300자 이내로 입력해주세요", Toast.LENGTH_SHORT).show()
                    // 입력이 300자를 넘으면 입력을 취소
                    binding.etScheduleIntro.text.delete(start, start + count)
                    isIntroLimitExceeded = true
                } else {
                    isIntroLimitExceeded = false
                }
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력 중

            }

            override fun afterTextChanged(editable: Editable?) {
                // 입력 후
                val currentLength = editable?.length ?: 0
                binding.tvLetterCnt2.text = currentLength.toString()
            }
        })

        // 날짜 설정
        binding.etScheduleDate.setOnClickListener{
            showDatePickerDialog()
        }









        // 버튼 누르면 인원 수 변경 시키기 (일정 참가자수 상한선 : 30명)
        var members : Int = 0
        binding.imgPlus.setOnClickListener {
            if (members < 30) {
                members += 10
                if (members > 30) {
                    members = 30
                }
                binding.tvAllMember.text = members.toString()
            }
        }

        binding.imgMinus.setOnClickListener {
            if (members > 0) {
                members -= 10
                if (members < 0) {
                    members = 0
                }
                binding.tvAllMember.text = members.toString()
            }
        }




        // 일정 등록 버튼 클릭 시 정보 취합하기
        binding.btnSetSchedule.setOnClickListener {
            setScheduleList.add(ScheduleVO(
                "모임명", // 나중에 수정필요
                binding.etScheduleName.text.toString(),
                binding.etScheduleIntro.text.toString(),
                binding.etScheduleDate.text.toString(),
                binding.etScheduleFee.text.toString().toInt(), // 도운이가 수정함 fee를 int로 바꿔서 에러뜸
                binding.etScheduleLoca.text.toString(),
                binding.tvAllMember.text.toString().toInt(),
                0,"모집중")
            )
        }








//        // 사진 여러장 선택하기
//        val pickMultipleMedia =
//            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
//                // Callback is invoked after the user selects media items or closes the
//                // photo picker.
//                if (uris.isNotEmpty()) {
//                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
//                } else {
//                    Log.d("PhotoPicker", "No media selected")
//                }
//            }
//        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // 여기서 선택된 날짜를 사용할 수 있습니다.
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.etScheduleDate.setText(selectedDate)
            },
            year, month, day
        )

        // 현재 날짜를 최소 날짜로 설정하여 과거 날짜를 선택하지 못하도록 합니다.
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }
}