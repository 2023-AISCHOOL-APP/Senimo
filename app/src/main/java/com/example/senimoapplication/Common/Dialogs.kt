package com.example.senimoapplication.Common

// CustomDialog.kt
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.Login.Activity_login.IntroActivity
import com.example.senimoapplication.Login.Activity_login.LoginActivity
import com.example.senimoapplication.R

// 게시물 관리 다이얼로그
fun showBoardDialogBox(activity: Activity, message: String?, okay: String?, successMessage : String?) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_custom_dialog)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
    val btnOkay: Button = dialog.findViewById(R.id.btnOkay)
    val btnCancel: Button = dialog.findViewById(R.id.btnCancel)

    tvMessage.text = message
    btnOkay.text = okay
    btnOkay.setOnClickListener {

        Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
        // DB 데이터 삭제
        val intent = Intent(activity, ClubActivity::class.java)
        activity.startActivity(intent)
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

// 회원 관리 다이얼로그
fun showMemberDialogBox(context: Context, message: String?, okay: String?, successMessage : String?) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_custom_dialog)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
    val btnOkay: Button = dialog.findViewById(R.id.btnOkay)
    val btnCancel: Button = dialog.findViewById(R.id.btnCancel)

    tvMessage.text = message
    btnOkay.text = okay
    btnOkay.setOnClickListener {
        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
        // 원하는 동작 추가
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}


// 환경설정 관리 다이얼로그
fun showSettingDialogBox(activity: Activity, message: String?, okay: String?, successMessage : String?) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_custom_dialog)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
    val btnOkay: Button = dialog.findViewById(R.id.btnOkay)
    val btnCancel: Button = dialog.findViewById(R.id.btnCancel)

    tvMessage.text = message
    btnOkay.text = okay
    btnOkay.setOnClickListener {

        Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
        // DB 데이터 삭제
        val intent = Intent(activity, IntroActivity::class.java)
        activity.startActivity(intent)
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}