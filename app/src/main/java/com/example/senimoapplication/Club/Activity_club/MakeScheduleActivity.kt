package com.example.senimoapplication.Club.Activity_club



import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.databinding.ActivityMakeScheduleBinding



class MakeScheduleActivity : ComponentActivity() {

    lateinit var binding : ActivityMakeScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMakeScheduleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.icBack.setOnClickListener {
            val intent = Intent(this@MakeScheduleActivity, ClubActivity::class.java)
            startActivity(intent)
            finish()
        }



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


        binding.timePicker.visibility = GONE
        binding.calendarView.visibility = GONE


        // 날짜 설정
        binding.btnScheduleDate.setOnClickListener{
            binding.calendarView.visibility = VISIBLE
            binding.timePicker.visibility = View.GONE
            binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                binding.btnScheduleDate.text ="$selectedDate"
            }

        }


        // 시간 설정
        binding.btnScheduleTime.setOnClickListener {
            binding.calendarView.visibility = View.GONE
            binding.timePicker.visibility = View.VISIBLE
            binding.timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                val selectedHour = if (hourOfDay > 12) {
                    hourOfDay - 12 // 오후 시간을 12시간 형식으로 변환
                } else if (hourOfDay == 0) {
                    12 // 자정 시간을 12시간 형식으로 변환
                } else {
                    hourOfDay // 오전 시간은 그대로 유지
                }

                val amPm = if (hourOfDay >= 12) "오후" else "오전" // 오전/오후 정보

                val selectedTime = "$amPm $selectedHour:$minute"
                binding.btnScheduleTime.text = selectedTime
            }
        }

        binding.etScheduleLoca.setOnClickListener {
            binding.timePicker.visibility = GONE
            binding.calendarView.visibility = GONE
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

            // 나중에 서버 통신 결과로 일정 등록 성공/실패 토스트 메시지 띄우기
            Toast.makeText(this, "일정이 등록되었습니다", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ClubActivity::class.java)
            startActivity(intent)
            finish()
//            setScheduleList.add(ScheduleVO(
//                "모임명", // 나중에 수정필요
//                binding.etScheduleName.text.toString(),
//                binding.etScheduleIntro.text.toString(),
//                "${binding.btnScheduleDate.text.toString()} ${binding.btnScheduleTime.text.toString()}",
//                binding.etScheduleFee.text.toString().toInt(), // 도운이가 수정함 fee를 int로 바꿔서 수정함
//                binding.etScheduleLoca.text.toString(),
//                binding.tvAllMember.text.toString().toInt(),
//                0,"모집중")
//            )

        }









    }

}