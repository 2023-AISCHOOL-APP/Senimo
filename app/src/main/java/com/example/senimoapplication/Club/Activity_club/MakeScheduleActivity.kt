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
import com.example.senimoapplication.Common.DivideDateTime
import com.example.senimoapplication.databinding.ActivityMakeScheduleBinding
import com.example.senimoapplication.server.Server
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class MakeScheduleActivity : ComponentActivity() {

  private var imageUri: Uri? = null // selectedImageUri를 클래스 수준에 선언
  private var imageName: String? = null // 선택된 이미지의 이름을 저장
  var members: Int = 10
  lateinit var binding: ActivityMakeScheduleBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMakeScheduleBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    //Intent 관리 (From 일정 수정 버튼)
    val clickedSchedule = intent.getParcelableExtra<ScheduleVO>("clickedSchedule")
    val title = intent.getStringExtra("title")
    val intentClubCode = intent.getStringExtra("club_code")
    Log.d("clickedSchedule","받아온 값 확인 $clickedSchedule, $title")

    binding.timePicker.visibility = GONE
    binding.calendarView.visibility = GONE

    binding.icBack.setOnClickListener {
      val intent = Intent(this@MakeScheduleActivity, ClubActivity::class.java)
      intent.putExtra("clubCodeFromMakeSchedule", clickedSchedule?.clubCode)
      startActivity(intent)
      finish()
    }


    // Intent값이 있는 경우 : 일정 수정하기
    if (title !=null){
      binding.tvScheduleTitleM.text = title
    }
    if (clickedSchedule != null){
      binding.etScheduleName.text = Editable.Factory.getInstance().newEditable(clickedSchedule.scheTitle)
      binding.etScheduleIntro.text = Editable.Factory.getInstance().newEditable(clickedSchedule.scheContent)
      binding.etScheduleLoca.text = Editable.Factory.getInstance().newEditable(clickedSchedule.scheLoca)
      binding.etScheduleFee.text = Editable.Factory.getInstance().newEditable(clickedSchedule.scheFee.toString())
      members = clickedSchedule.maxNum
      binding.tvAllMember.text = members.toString()
      Glide.with(this).load(clickedSchedule.scheImg).into(binding.imgButton)
      binding.tvLetterCnt.text = binding.etScheduleName.text.length.toString()
      binding.tvLetterCnt2.text = binding.etScheduleIntro.text.length.toString()

      // 선택된 날짜 캘린더 뷰에 표시

      val defaultDateString = DivideDateTime(clickedSchedule.scheDate).first
      binding.btnScheduleDate.text = defaultDateString
      val date = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).parse(defaultDateString)
      val calendar = Calendar.getInstance()
      calendar.time = date
      binding.calendarView.date = calendar.timeInMillis
      binding.btnScheduleDate.setOnClickListener {
        binding.calendarView.visibility = VISIBLE
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
          val selectedDate = "$year-${month + 1}-$dayOfMonth"
          binding.btnScheduleDate.text = "$selectedDate"
        }
      }

      // 선택된 시간 타임피커에 표시
      val defaultTimeString = DivideDateTime(clickedSchedule.scheDate).second
      binding.btnScheduleTime.text = defaultTimeString
      val timeFormat = SimpleDateFormat("a HH:mm", Locale.KOREAN)
      val selectedTime = timeFormat.parse(defaultTimeString)
      calendar.time = selectedTime
      binding.timePicker.currentHour = calendar.get(Calendar.HOUR_OF_DAY)
      binding.timePicker.currentMinute = calendar.get(Calendar.MINUTE)

      binding.btnScheduleTime.setOnClickListener {
        binding.timePicker.visibility = VISIBLE
        binding.calendarView.visibility = GONE
        binding.timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
          val selectedHour = if (hourOfDay > 12) {
            hourOfDay - 12 // 오후 시간을 12시간 형식으로 변환
          } else if (hourOfDay == 0) {
            12 // 자정 시간을 12시간 형식으로 변환
          } else {
            hourOfDay // 오전 시간은 그대로 유지
          }
          val amPm = if (hourOfDay >= 12) "오후" else "오전" // 오전/오후 정보
          val selectedTime = String.format("%s %02d:%02d", amPm, selectedHour, minute)
          binding.btnScheduleTime.text = selectedTime
        }
      }

      // 일정 수정하기 버튼
      binding.btnSetSchedule.setOnClickListener {
        val scheCode = clickedSchedule.scheCode
        val clubCode = clickedSchedule.clubCode
        val scheTitle = binding.etScheduleName.text.toString()
        val scheContent = binding.etScheduleIntro.text.toString()
        val scheDate = getUserSelectedDateTime()
        val scheLocation = binding.etScheduleLoca.text.toString()
        val maxNum = binding.tvAllMember.text.toString().toInt()
        val joinedMembers = clickedSchedule.joinedMembers
        val scheFee = binding.etScheduleFee.text.toString().toInt()
        val scheImg = imageName.toString()
        val clubName = clickedSchedule.clubName

        Log.d("datecheck", "${scheDate}")
        // 함수 새로 만들어야함
        updateSche(scheCode,clubCode, scheTitle, scheContent, scheDate, scheLocation, scheFee, maxNum, joinedMembers, scheImg, clubName)
      }
    } else {

      // 일정 등록 (기본 화면)
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

      // 일정 제목 - 입력 글자 수 제한
      var isTitleLimitExceeded = false
      var isIntroLimitExceeded = false
      binding.etScheduleName.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
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
        }

        override fun afterTextChanged(editable: Editable?) {
          val currentLength = editable?.length ?: 0
          binding.tvLetterCnt.text = currentLength.toString()
        }
      })

      // 일정 소개 - 입력 글자 수 제한
      binding.etScheduleIntro.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
          val currentLength = charSequence?.length ?: 0
          if (currentLength >= 300) {
            Toast.makeText(this@MakeScheduleActivity, "300자 이내로 입력해주세요", Toast.LENGTH_SHORT).show()
            binding.etScheduleIntro.text.delete(start, start + count)
            isIntroLimitExceeded = true
          } else {
            isIntroLimitExceeded = false
          }
        }

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
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
          val selectedTime = String.format("%s %02d:%02d", amPm, selectedHour, minute)
          binding.btnScheduleTime.text = selectedTime
        }
      }

      // 일정 등록하기 버튼
      binding.btnSetSchedule.setOnClickListener {
        val scheTitle = binding.etScheduleName.text.toString()
        val scheContent = binding.etScheduleIntro.text.toString()
        val scheDate = getUserSelectedDateTime()
        val scheLocation = binding.etScheduleLoca.text.toString()
        val maxNum = binding.tvAllMember.text.toString().toInt()
        val scheFee = binding.etScheduleFee.text.toString().toInt()
        val scheImg = imageName.toString()

        Log.d("datecheck", "${scheDate}")
        makeSche(intentClubCode?:"", scheTitle, scheContent, scheDate, scheLocation, maxNum, scheFee, scheImg)
      }
    }

    // 장소 클릭하면 캘린더, 타임 피커 숨기기
    binding.etScheduleLoca.setOnClickListener {
      binding.timePicker.visibility = GONE
      binding.calendarView.visibility = GONE
    }

    // 버튼 누르면 인원 수 변경 시키기 (일정 참가자수 상한선 : 50명)
    val incrementAmount = 5 // 증가 또는 감소할 인원 수
    binding.imgPlus.setOnClickListener {
      if (members + incrementAmount <= 50) {
        members += incrementAmount
        binding.tvAllMember.text = members.toString()
      }
    }
    binding.imgMinus.setOnClickListener {
      if (members - incrementAmount >= 0) {
        members -= incrementAmount
        binding.tvAllMember.text = members.toString()
      }
    }

  }



  private fun getUserSelectedDateTime(): String {
    val selectedDate = binding.btnScheduleDate.text.toString() // 사용자가 선택한 날짜
    val selectedTime = binding.btnScheduleTime.text.toString() // 사용자가 선택한 시간
    val dateTimeString = "$selectedDate $selectedTime"

    // 선택된 날짜와 시간을 원하는 형식으로 변환
    val inputFormat = SimpleDateFormat("yyyy-MM-dd a hh:mm", Locale.KOREAN)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    try {
      val date = inputFormat.parse(dateTimeString)
      return outputFormat.format(date)
    } catch (e: ParseException) {
      e.printStackTrace()
    }

    return "" // 변환 실패 시 빈 문자열 반환
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
  fun makeSche(clubCode: String, scheTitle: String, scheContent: String, scheDate: String, scheLocation: String, maxNum: Int, scheFee: Int, scheImg: String?) {
    val service = Server(this).service
    val scheduleVO = ScheduleVO("", clubCode, scheTitle, scheContent, scheDate, scheLocation, scheFee, maxNum, 0, scheImg?:"")
    val call = service.createSchedule(scheduleVO)

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
            val intent = Intent(this@MakeScheduleActivity, ClubActivity::class.java)
            intent.putExtra("clickedMeeting",scheduleVO)
            startActivity(intent)
            Log.d("makeschedule", scheDate)

            finish()
          } else {
            // 서버 응답은 성공했지만 'rows'가 'success'가 아닌 경우
            Log.d("makeschedule", "not success")
          }
        }
      }

      override fun onFailure(call: Call<MakeScheResVo>, t: Throwable) {
        // 네트워크 요청 실패 시
        Log.e("makeschedule", "makeSche 네트워크 요청 실패", t)
      }
    })
  }

  // 일정 업데이트 함수
  fun updateSche(scheCode:String,clubCode: String, scheTitle: String, scheContent: String, scheDate: String, scheLocation: String, scheFee: Int, maxNum: Int, joinedMembers: Int, scheImg: String?, clubName: String?) {
    val service = Server(this).service
    val scheduleVO = ScheduleVO(scheCode,clubCode, scheTitle, scheContent,scheDate, scheLocation, scheFee,maxNum,joinedMembers,scheImg, clubName)
    val call = service.updateSchedule(scheduleVO)

    call.enqueue(object : Callback<MakeScheResVo> {
      override fun onResponse(call: Call<MakeScheResVo>, response: Response<MakeScheResVo>) {
        if (response.isSuccessful) {
          val makeScheRes = response.body()
          if (makeScheRes != null && makeScheRes.rows == "success") {
            Toast.makeText(this@MakeScheduleActivity, "일정이 등록되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MakeScheduleActivity, ClubActivity::class.java)
            intent.putExtra("clickedMeeting", scheduleVO)
            startActivity(intent)
            finish()
          } else {
            Toast.makeText(this@MakeScheduleActivity, "서버 오류: 일정 등록 실패", Toast.LENGTH_SHORT).show()
            Log.d("makeschedule", "Server responded but not success: ${response.body()?.rows}")
          }
        } else {
          Toast.makeText(this@MakeScheduleActivity, "응답 실패: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
          Log.d("makeschedule", "Response unsuccessful")
        }
      }

      override fun onFailure(call: Call<MakeScheResVo>, t: Throwable) {
        Toast.makeText(this@MakeScheduleActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
        Log.e("makeschedule", "Network request failed", t)
      }
    })
  }

}