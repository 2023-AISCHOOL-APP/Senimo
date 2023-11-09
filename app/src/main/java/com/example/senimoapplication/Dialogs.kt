package com.example.senimoapplication

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment


// 모임 탈퇴 확인
class ClubMemberDropOutDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("정말 모임을 탈퇴하시겠어요?") // 다이얼로그에 표시할 메시지
            .setPositiveButton("탈퇴하기") { dialog, which ->
                // 확인 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .setNegativeButton("취소") { dialog, which ->
                // 취소 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .create()
    }
}

// 회원 강퇴 확인
class ClubMemberDeleteDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("모임에서 추방하시겠어요?") // 다이얼로그에 표시할 메시지
            .setPositiveButton("추방하기") { dialog, which ->
                // 확인 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .setNegativeButton("취소") { dialog, which ->
                // 취소 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .create()
    }
}

// 일반회원 -> 운영진으로 전환 확인
class ClubMemberLevelSwitchDialog1 : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("운영진으로 전환하시겠어요?") // 다이얼로그에 표시할 메시지
            .setPositiveButton("확인") { dialog, which ->
                // 확인 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .setNegativeButton("취소") { dialog, which ->
                // 취소 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .create()
    }
}

// 운영진 -> 일반회원으로 전환 확인
class ClubMemberLevelSwitchDialog2 : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("일반회원으로 전환하시겠어요?") // 다이얼로그에 표시할 메시지
            .setPositiveButton("전환하기") { dialog, which ->
                // 확인 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .setNegativeButton("취소") { dialog, which ->
                // 취소 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .create()
    }
}

// 모임장 위임 전환 확인
class ClubMemberLevelSwitchDialog3 : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("모임장을 위임하시겠어요?") // 다이얼로그에 표시할 메시지
            .setPositiveButton("위임하기") { dialog, which ->
                // 확인 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .setNegativeButton("취소") { dialog, which ->
                // 취소 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .create()
    }
}


// 모임 일정 삭제 확인
class ScheduleDeleteDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("모임 일정을 삭제하시겠어요?") // 다이얼로그에 표시할 메시지
            .setPositiveButton("삭제하기") { dialog, which ->
                // 확인 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .setNegativeButton("취소") { dialog, which ->
                // 취소 버튼을 눌렀을 때 수행할 동작
                // 여기에 코드를 추가하세요.
            }
            .create()
    }
}