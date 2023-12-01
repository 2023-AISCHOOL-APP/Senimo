package com.example.senimoapplication.Common

// CustomDialog.kt
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.senimoapplication.Club.VO.DeleteMemberVO
import com.example.senimoapplication.Club.VO.UpdateMemberVO
import com.example.senimoapplication.Club.adapter.PostAdapter.Companion.deletePostData
import com.example.senimoapplication.Club.fragment.HomeFragment
import com.example.senimoapplication.Club.fragment.MemberManager
import com.example.senimoapplication.Login.Activity_login.IntroActivity
import com.example.senimoapplication.MainPage.Activity_main.MainActivity
import com.example.senimoapplication.R
import com.example.senimoapplication.server.Server
import com.example.senimoapplication.server.Token.PreferenceManager
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// 일정 관리 다이얼로그
fun showActivityDialogBox(
    activity: Activity,
    message: String?,
    okay: String?,
    successMessage: String?,
    scheCode: String?,
    onDeleteSchedule: () -> Unit
) {
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
        onDeleteSchedule.invoke() // 일정 삭제 함수 호출
        Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun showPostDialogBox(activity: Activity, message: String?, okay: String?, successMessage : String?, postCode: String, listener: PostUpdateListener) {
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
        deletePostData(activity, postCode, listener)
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

// 회원 삭제 다이얼로그
fun showDeleteDialogBox(context: Context, message: String?, okay: String?, successMessage: String?, user: String, clubCode: String, fragment: HomeFragment) {

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
        val server = Server(context)
        val memberManager = MemberManager(server)
        val callback = object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body()?.getAsBoolean() == true) {
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    fragment.fetchMemberList(fragment.view) // HomeFragment의 함수 호출
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "멤버 삭제에 실패했습니다. \n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
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
fun showUpdateDialogBox(context: Context, message: String?, okay: String?, successMessage: String?, user: String, clubCode: String, clubRole: Int, fragment: HomeFragment) {
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
        val server = Server(context)
        val memberManager = MemberManager(server)
        val callback = object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    fragment.fetchMemberList(fragment.view) // HomeFragment의 함수 호출
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "회원 역할 변경에 실패했습니다. \n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
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

// 모임장 위임 다이얼로그
fun showLeaderUpdateDialogBox(context: Context, message: String?, okay: String?, successMessage: String?, user: String, clubCode: String, clubRole: Int, fragment: HomeFragment) {
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
        val server = Server(context)
        val memberManager = MemberManager(server)
        val callback = object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    fragment.fetchMemberList(fragment.view) // HomeFragment의 함수 호출
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "모임장 위임에 실패했습니다. \n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류가 발생했습니다. \n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        val updateMemberVO = UpdateMemberVO(clubCode, user, clubRole)
        memberManager.updateLeader(updateMemberVO, callback)
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

// 모임탈퇴 다이얼로그
fun showQuitDialogBox(context: Context, message: String?, okay: String?, successMessage : String?, fragment: HomeFragment, clubCode: String ) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_custom_dialog)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
    val btnOkay: Button = dialog.findViewById(R.id.btnOkay)
    val btnCancel: Button = dialog.findViewById(R.id.btnCancel)
    val UserData = PreferenceManager.getUser(context)
    val userId = UserData?.user_id

    tvMessage.text = message
    btnOkay.text = okay
    btnOkay.setOnClickListener {
        fragment.quitClub(clubCode,userId)
        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

// 로그아웃 다이얼로그
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
        PreferenceManager.clearToken(activity)
        // 로그아웃 후 처리 (예: 로그인 화면으로 이동)
        val intent = Intent(activity, IntroActivity::class.java)
        activity.startActivity(intent)
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

// 접근 권한 안내 다이얼로그
fun showAlertDialogBox(context: Context, message: String?, okay: String?) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_custom_dialog_one)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
    val btnOkay: Button = dialog.findViewById(R.id.btnOkay)

    tvMessage.text = message
    btnOkay.text = okay
    btnOkay.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

// 회원탈퇴 다이얼로그
fun showDropOutDialogBox(activity: Activity, message: String?, okay: String?, successMessage : String?, onUserDropOut: () -> Unit) {
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
        onUserDropOut.invoke() // 회원 탈퇴 함수 호출
        Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
        PreferenceManager.clearToken(activity)
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

// 뱃지 획득여부 다이얼로그
fun showBadgeDialogBox(activity: Activity, badgeImg: String?, badgeName: String?, badgeGetInfo: String?, text1 : String?, text2 : String?) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_custom_dialog_badge)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val cvMColor : CardView = dialog.findViewById(R.id.cvMColor)
    val imgMClosebtn : ImageView = dialog.findViewById(R.id.imgMClosebtn)
    val imgMBadge : ImageView = dialog.findViewById(R.id.imgMBadge)
    val tvMBadgeName : TextView = dialog.findViewById(R.id.tvMBadgeName)
    val btnMGetInfo : Button = dialog.findViewById(R.id.btnMGetInfo)
    val tvMText1 : TextView = dialog.findViewById(R.id.tvMText1)
    val tvMText2 : TextView = dialog.findViewById(R.id.tvMText2)

    // 배지 이미지 설정
    val resourceId = activity.resources.getIdentifier(badgeImg, "drawable", activity.packageName)
    imgMBadge.setImageResource(resourceId)

    // 배지 상태에 따라 UI 업데이트
    if (badgeImg != null) {
        if (badgeImg.endsWith("_off")) {
            val darkGrayColor = ContextCompat.getColor(activity, R.color.bg_darkgray80)
            cvMColor.setCardBackgroundColor(darkGrayColor)
            btnMGetInfo.text = "획득 방법"
            btnMGetInfo.backgroundTintList = ColorStateList.valueOf(darkGrayColor)

            // 닫기 버튼 이미지 변경
            val closeIconNavy = activity.resources.getIdentifier("ic_close_dialog_navy", "drawable", activity.packageName)
            imgMClosebtn.setImageResource(closeIconNavy)

        } else if (badgeImg.endsWith("_on")) {
            val pointColor = ContextCompat.getColor(activity, R.color.point)
            cvMColor.setCardBackgroundColor(pointColor)
            btnMGetInfo.text = "보유 중"
            btnMGetInfo.backgroundTintList = ColorStateList.valueOf(pointColor)

            // 닫기 버튼 이미지 변경
            val cloaseIconPoint = activity.resources.getIdentifier("ic_close_dialog_red", "drawable", activity.packageName)
            imgMClosebtn.setImageResource(cloaseIconPoint)
        }
    }

    // 텍스트 설정
    tvMBadgeName.text = badgeName
    btnMGetInfo.text = badgeGetInfo
    tvMText1.text = text1
    tvMText2.text = text2

    // 닫기 버튼 이벤트 설정
    imgMClosebtn.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()

}

