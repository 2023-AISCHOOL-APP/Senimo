package com.example.senimoapplication.Club.Activity_club


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.VO.MakeScheResVo
import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.databinding.ActivityMakeScheduleBinding
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class MakeScheduleActivity : ComponentActivity() {

  private var imageUri: Uri? = null // selectedImageUri를 클래스 수준에 선언
  private var imageName: String? = null // 선택된 이미지의 이름을 저장

  lateinit var binding: ActivityMakeScheduleBinding
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
        // 이미지를 선택한 후에 URI를 변수에 저장
        imageUri = uri
        imageName = getFileName(uri) // 파일이름 추출
        Glide.with(this).load(uri).into(binding.imgButton)

        Log.d("PhotoPicker", "Selected URI: $uri")
        binding.imgButton.setImageURI(uri)
        binding.imgButton.visibility = ImageView.VISIBLE
        binding.imagebtnLogo.visibility = INVISIBLE // 이미지 선택 시 아이콘 사라지기
      } else {
        Log.d("PhotoPicker", "No media selected")
      }
    }

    binding.imgButton.setOnClickListener {
      pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    val setScheduleList: ArrayList<ScheduleVO> = ArrayList()

    var isTitleLimitExceeded = false
    var isIntroLimitExceeded = false

    // 일정 제목 - 입력 글자 수 제한
    binding.etScheduleName.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        charSequence: CharSequence?,
        start: Int,
        count: Int,
        after: Int
      ) {
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
      override fun beforeTextChanged(
        charSequence: CharSequence?,
        start: Int,
        count: Int,
        after: Int
      ) {
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
    binding.btnScheduleDate.setOnClickListener {
      binding.calendarView.visibility = VISIBLE
      binding.timePicker.visibility = View.GONE
      binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
        val selectedDate = "$year-${month + 1}-$dayOfMonth"
        binding.btnScheduleDate.text = "$selectedDate"
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
    var members: Int = 0
    binding.imgPlus.setOnClickListener {
      if (members <50) {
        members += 10
        if (members > 50) {
          members = 50
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

    // timePicker의 시간을 24시간제로 바꿈
    binding.timePicker.setIs24HourView(true)
    binding.btnSetSchedule.setOnClickListener {

      val clubCode = intent.getStringExtra("club_code").toString()
      val scheTitle = binding.etScheduleName.text.toString()
      val scheContent = binding.etScheduleIntro.text.toString()
      // getCombinedDateTime() 함수를 사용하여 날짜와 시간을 결합하여 scheDate 변수에 할당
      val scheDate = getCombinedDateTime()
      val scheLocation = binding.etScheduleLoca.text.toString()
      val maxNum = binding.tvAllMember.text.toString().toInt()
      val scheFee = binding.etScheduleFee.text.toString().toInt()
      val scheImg = imageName.toString()

      Log.d("scheDate", scheDate)
      // makeSche 함수를 호출하여 일정을 생성하고 필요한 매개변수들을 전달
      makeSche(clubCode, scheTitle, scheContent, scheDate, scheLocation, maxNum, scheFee, scheImg)
    }
  }

  // calendarView, timePicker에서 선택된 날짜와 시간을 결합하여 하나의 문자열로 반환하는 함수
  private fun getCombinedDateTime(): String {
    // Calendar 객체를 사용하여 현재 시간을 가져옴
    val selectedDate = Calendar.getInstance().apply {
      // CalendarView에서 선택된 날짜의 milliseconds를 가져와 selectedDate에 설정
      timeInMillis = binding.calendarView.date
    }

    // Calendar 객체를 사용하여 선택된 시간을 가져옴
    val selectedTime = Calendar.getInstance().apply {
      set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
      set(Calendar.MINUTE, binding.timePicker.minute)
    }

    // UTC 시간대로 형식을 지정하는 SimpleDateFormat 생성
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    // 선택된 날짜와 시간을 결합하여 하나의 Calendar 객체로 생성
    val combinedDateTime = Calendar.getInstance().apply {
      set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
      set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
      set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))
      set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY))
      set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE))
      set(Calendar.SECOND, 0)
    }

    // 결합된 날짜와 시간을 UTC 시간대 형식으로 변환하여 문자열로 반환
    return sdf.format(combinedDateTime.time)
  }

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

  // 서버에 새로운 일정을 생성하는 함수
  fun makeSche(
    clubCode: String,
    scheTitle: String,
    scheContent: String,
    scheDate: String,
    scheLocation: String,
    maxNum: Int,
    scheFee: Int,
    scheImg: String?
  ) {
    val service = Server(this).service
    val call = service.createSchedule(
      clubCode,
      scheTitle,
      scheContent,
      scheDate,
      scheLocation,
      maxNum,
      scheFee,
      scheImg
    )
    call.enqueue(object : Callback<MakeScheResVo> {
      override fun onResponse(call: Call<MakeScheResVo>, response: Response<MakeScheResVo>) {
        Log.d("makeSche", response.toString())
        if (response.isSuccessful) {
          val makeScheRes = response.body()
          if (makeScheRes != null && makeScheRes.rows == "success") {
            Log.d("clubCode", clubCode)
            // 서버 응답이 성공이면
            // 성공적으로 처리되었을 때의 동작을 수행
            Toast.makeText(this@MakeScheduleActivity, "일정이 등록되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MakeScheduleActivity, ScheduleActivity::class.java)
            startActivity(intent)
            finish()
          } else {
            // 서버 응답은 성공했지만 'rows'가 'success'가 아닌 경우
            Log.d("makeSche", "not success")
          }
        }
      }

      override fun onFailure(call: Call<MakeScheResVo>, t: Throwable) {
        // 네트워크 요청 실패 시
        Log.e("MakeScheduleActivity", "makeSche 네트워크 요청 실패", t)
      }
    })
  }
}