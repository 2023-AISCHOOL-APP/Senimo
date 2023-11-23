package com.example.senimoapplication.Common

class BadgeManager {
    var badge = mutableListOf(false, false, false, false, false, false, false, false, false)

    // 시니모 뉴비 : 회원가입 시 호출
    fun updateForNewUser(userId: String) {
        badge[0] = true
        updateBadgeInServer(userId, "badge_code01" )
    }

    // 호기심탐험가 : 5개의 모임에 찜했을 경우(관심 모임) 호출
    fun updateForInterestedClubs(userId: String, interestedClubsCount: Int) {
        if (interestedClubsCount >= 5) {
            badge[4] = true
            updateBadgeInServer(userId, "badge_code02")
        }
    }

    // 새싹모임러 / 이구역모임왕 : 새로운 모임에 가입 했거나, 가입한 모임 개수 5개일 경우 호출
    fun updateForNewJoin(userId: String, joinedMeetingsCount: Int, isFirstJoin: Boolean) {
        if (isFirstJoin) {
            badge[2] = true
            updateBadgeInServer(userId, "badge_code03")
        }
        if (joinedMeetingsCount >= 5) {
            badge[3] = true
            updateBadgeInServer(userId, "badge_code04")
        }
    }

    // 용기있는뉴비 / 모임의기둥 / 프로참석러  : 오프라인 모임 처음 참석 또는 5회, 15회일 경우 호출
    fun updateForParticipation(userId: String, finishedScheduleCount: Int) {
        if (finishedScheduleCount >= 1) {
            badge[1] = true
            updateBadgeInServer(userId, "badge_code05")
        }
        if (finishedScheduleCount >= 5) {
            badge[5] = true
            updateBadgeInServer(userId, "badge_code06")
        }
        if (finishedScheduleCount >= 15) {
            badge[6] = true
            updateBadgeInServer(userId, "badge_code07")
        }
    }

    // 내가바로모임장 : 모임 생성 1회일 경우 호출
    fun updateForMeetingCreation(userId: String) {
        badge[7] = true
        updateBadgeInServer(userId, "badge_code08")
    }

    // 뱃지마스터 : 모든 뱃지가 true일 경우 호출
    fun updateForBadgeMaster(userId: String) {
        if (badge.take(8).all { it }) {
            badge[8] = true
            updateBadgeInServer(userId, "badge_code09")
        }
    }


    // 서버에 배지 업데이트 요청
    private fun updateBadgeInServer(userId: String, badgeCode: String) {
        // Retrofit 요청 로직 구현
        // 예: retrofitService.awardBadge(userId, badgeCode)
    }

    // 현재 뱃지 상태를 반환하는 함수
    fun getCurrentBadgeStatus(): List<Boolean> = badge
}



// 싱글톤 인스턴스 제공을 위한 코드
object BadgeManagerSingleton {
    val instance = BadgeManager()
}