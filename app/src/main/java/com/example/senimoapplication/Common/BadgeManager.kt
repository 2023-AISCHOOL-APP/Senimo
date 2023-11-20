package com.example.senimoapplication.Common

class BadgeManager {
    var badge = mutableListOf(false, false, false, false, false, false, false, false, false)

    // 시니모 뉴비 : 회원가입 시 호출
    fun updateForNewUser() {
        badge[0] = true
    }

    // 호기심탐험가 : 5개의 모임에 찜했을 경우(관심 모임) 호출
    fun updateForInterestedClubs(interestedClubsCount: Int) {
        badge[4] = interestedClubsCount >= 5
    }

    // 새싹모임러 / 이구역모임왕 : 새로운 모임에 가입 했거나, 가입한 모임 개수 5개일 경우 호출
    fun updateForNewJoin(joinedMeetingsCount : Int, isFirstJoin : Boolean) {
        if(isFirstJoin) badge[2] = true
        badge[3] = joinedMeetingsCount >= 5
    }

    // 용기있는뉴비 / 모임의기둥 / 프로참석러  : 오프라인 모임 처음 참석 또는 5회, 15회일 경우 호출
    fun updateForParticipation(finishedScheduleCount: Int) {
        badge[1] = finishedScheduleCount >= 1
        badge[5] = finishedScheduleCount >= 5
        badge[6] = finishedScheduleCount >= 15
    }

    // 내가바로모임장 : 모임 생성 1회일 경우 호출
    fun updateForMeetingCreation() {
        badge[7] = true
    }

    // 뱃지마스터 : 모든 뱃지가 true일 경우 호출
    fun updateForBadgeMaster() {
        badge[8] = badge.take(8).all { it }
    }

    // 현재 뱃지 상태를 반환하는 함수
    fun getCurrentBadgeStatus(): List<Boolean> = badge
}

// 싱글톤 인스턴스 제공을 위한 코드
object BadgeManagerSingleton {
    val instance = BadgeManager()
}