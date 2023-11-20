package com.example.senimoapplication.Login.Activity_login

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.senimoapplication.R

class IntroActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_intro)

    val imgs = arrayOf(
      findViewById<ImageView>(R.id.introImg1),
      findViewById<ImageView>(R.id.introImg2),
      findViewById<ImageView>(R.id.introImg3)
    )

    val translations = arrayOf(
      resources.getDimensionPixelSize(R.dimen.translation_y).toFloat(),
      resources.getDimensionPixelSize(R.dimen.translation_y_2).toFloat(),
      resources.getDimensionPixelSize(R.dimen.translation_y).toFloat()
    )

    val animators = Array(3) {
      ObjectAnimator.ofFloat(imgs[it], "translationY", 0f, translations[it]).apply {
        duration = 10000
        interpolator = DecelerateInterpolator()
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
        start()
      }
    }

    val btnLogin = findViewById<Button>(R.id.btnLoginI)
    val btnSignUp = findViewById<Button>(R.id.btnSignUp)
    var backPressedTime:Long = 0

    val callback = object : OnBackPressedCallback(true){
      override fun handleOnBackPressed() {
        if(System.currentTimeMillis() - backPressedTime >= 2000){
          backPressedTime = System.currentTimeMillis()
          Toast.makeText(this@IntroActivity, "뒤로가기 버튼을 한번 더 누르면 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
        }else if (System.currentTimeMillis() - backPressedTime < 2000){
          finish()
        }
      }
    }
    this.onBackPressedDispatcher.addCallback(this, callback)

    btnLogin.setOnClickListener {
      val intent = Intent(this@IntroActivity, LoginActivity::class.java)
      startActivity(intent)
      finish()
    }

    btnSignUp.setOnClickListener {
      val intent = Intent(this@IntroActivity, SignUpActivity::class.java)

      startActivity(intent)
      finish()
    }
  }
}