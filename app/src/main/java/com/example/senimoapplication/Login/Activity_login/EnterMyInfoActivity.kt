package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Login.adapter.DongAdapter
import com.example.senimoapplication.Login.adapter.GuAdapter
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityEnterMyInfoBinding

class EnterMyInfoActivity : AppCompatActivity() {

  lateinit var DongAdapter: DongAdapter
  lateinit var binding: ActivityEnterMyInfoBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_enter_my_info)

    binding = ActivityEnterMyInfoBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val guList = arrayListOf<String>("광주 전체","광산구","남구","동구","북구","서구")

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

    val GuAdapter = GuAdapter(R.layout.gu_list, guList, applicationContext)
    DongAdapter = DongAdapter(R.layout.dong_list, gwangjuDistricts, applicationContext)

    binding.rvGu.adapter = GuAdapter
    binding.rvGu.layoutManager = LinearLayoutManager(this@EnterMyInfoActivity)

    binding.rvDong.adapter = DongAdapter

    val girdLayoutManager = GridLayoutManager(applicationContext, 2)
    binding.rvDong.layoutManager = girdLayoutManager

    GuAdapter.itemClickListener = object : GuAdapter.OnItemClickListener {
      override fun onItemClick(position: Int) {
        val item = guList[position]

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
        binding.rvDong.smoothScrollToPosition(0)
      }
    }

    val callback = object : OnBackPressedCallback(true){
      override fun handleOnBackPressed() {
        val intent = Intent(this@EnterMyInfoActivity, SignUpActivity::class.java)
        startActivity(intent)
        finish()
      }
    }
    this.onBackPressedDispatcher.addCallback(this, callback)

    binding.btnEnterInfo.setOnClickListener {
      val intent = Intent(this@EnterMyInfoActivity, MainActivity::class.java)
      startActivity(intent)
      finishAffinity()
    }

    binding.imgBackToSignUp.setOnClickListener {
      val intent = Intent(this@EnterMyInfoActivity, SignUpActivity::class.java)
      startActivity(intent)
      finish()
    }

    // 회원 소개글 글자 수 제한
//    var isUserIntroLimitExceeded = false
//
//    binding.etmlUserIntroS.addTextChangedListener(object  : TextWatcher{
//      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        val currentLength = s?.length ?: 0
//        if (currentLength >= 300) {
//          Toast.makeText(this@EnterMyInfoActivity, "300자 이내로 입력해주세요.", Toast.LENGTH_SHORT).show()
//          binding.etmlUserIntroS.text.delete(start, start+count)
//          isUserIntroLimitExceeded = true
//        }else{
//          isUserIntroLimitExceeded = false
//        }
//      }
//
//      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//      }
//
//      override fun afterTextChanged(s: Editable?) {
//        val currentLength = s?.length ?:0
//        binding.tvMLetterCnt3.text = currentLength.toString()
//      }
//    })


    // 회원 소개글 글자 수 제한
    binding.etmlUserIntroS.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // 입력 전 필요한 로직 (필요한 경우)
      }
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // 입력 중 필요한 로직 (필요한 경우)
      }
      override fun afterTextChanged(s: Editable?) {
        val currentLength = s?.length ?: 0
        binding.tvMLetterCnt3.text = "$currentLength"
        if (currentLength > 300) {
          binding.tvMLetterCnt3.setTextColor(ContextCompat.getColor(this@EnterMyInfoActivity, R.color.main))
          binding.tvMUserIntroWarning.visibility = View.VISIBLE // 경고 메시지 표시
        } else {
          binding.tvMLetterCnt3.setTextColor(ContextCompat.getColor(this@EnterMyInfoActivity, R.color.txt_gray70))
          binding.tvMUserIntroWarning.visibility = View.GONE // 경고 메시지 숨김
        }
      }
    })

  }
}