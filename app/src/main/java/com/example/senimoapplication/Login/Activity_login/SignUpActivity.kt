package com.example.senimoapplication.Login.Activity_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.senimoapplication.R

class SignUpActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up)

    val btnToEnterMyInfo = findViewById<Button>(R.id.btnToEnterMyInfo)

    btnToEnterMyInfo.setOnClickListener {
      val intent = Intent(this@SignUpActivity, EnterMyInfoActivity::class.java)
      startActivity(intent)
      overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
    }
  }
}
