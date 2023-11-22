package com.example.senimoapplication.Common

// CustomDialog.kt
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.Club.VO.DeleteMemberVO
import com.example.senimoapplication.Club.VO.UpdateMemberVO
import com.example.senimoapplication.Club.fragment.MemberManager
import com.example.senimoapplication.Login.Activity_login.IntroActivity
import com.example.senimoapplication.Login.Activity_login.LoginActivity
import com.example.senimoapplication.R
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 게시물 관리 다이얼로그
fun showActivityDialogBox(activity: Activity, message: String?, okay: String?, successMessage : String?) {
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

// 회원 삭제 다이얼로그
fun showDeleteDialogBox(context: Context, message: String?, okay: String?, successMessage : String?, user:String, clubCode:String) {
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
        val memberManager = MemberManager()
        val callback = object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body()?.getAsBoolean() == true) {
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "멤버 삭제에 실패했습니다. \n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Delete member failure handling (e.g., network error)
                Toast.makeText(context, "네트워크 오류가 발생했습니다. \n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        val deleteMemberVO = DeleteMemberVO(clubCode, user)
        memberManager.deleteMember(deleteMemberVO, callback)
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

// 회원 역할 업데이트 다이얼로그
fun showUpdateDialogBox(context: Context, message: String?, okay: String?, successMessage: String?, clubCode: String,user: String,clubRole: Int) {
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
        // 서버 통신 요청
        val memberManager = MemberManager()
        val callback = object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "회원 전환에 실패했습니다. \n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // 역할 변경 실패 시 처리 (예: 네트워크 오류)
                Toast.makeText(context, "네트워크 오류가 발생했습니다. \n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        val updateMemberVO = UpdateMemberVO(clubCode, user, clubRole)
        memberManager.updateMember(updateMemberVO, callback)
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

// 기본 다이얼로그
fun showFragmentDialogBox(context: Context, message: String?, okay: String?, successMessage : String?) {
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