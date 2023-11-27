//package com.example.senimoapplication.Common
//
//class BadgeManager {
//    private var badges = mutableMapOf(
//        "badge_code01" to false, // 시니모 뉴비
//        "badge_code02" to false, // 호기심탐험가
//        "badge_code03" to false, // 새싹모임러
//        "badge_code04" to false, // 이구역모임왕
//        "badge_code05" to false, // 용기있는뉴비
//        "badge_code06" to false, // 모임의기둥
//        "badge_code07" to false, // 프로참석러
//        "badge_code08" to false, // 내가바로모임장
//        "badge_code09" to false  // 뱃지마스터
//    )
//
//    // 뱃지 상태 업데이트 및 서버 요청
//    private fun tryUpdateBadge(userId: String, badgeCode: String) {
//        if (!badges[badgeCode]!!) {
//            badges[badgeCode] = true
//            updateBadgeInServer(userId, badgeCode)
//        }
//    }
//
//    // 서버에 배지 업데이트 요청
//    private fun updateBadgeInServer(userId: String, badgeCode: String) {
//        // Retrofit 요청 로직 구현
//        // 예: retrofitService.awardBadge(userId, badgeCode)
//    }
//
//    // 각 상황에 맞는 뱃지 업데이트 함수들
//    fun updateForNewUser(userId: String) {
//        tryUpdateBadge(userId, "badge_code01")
//    }
//
//    fun updateForInterestedClubs(userId: String, interestedClubsCount: Int) {
//        if (interestedClubsCount >= 5) {
//            tryUpdateBadge(userId, "badge_code02")
//        }
//    }
//
//    fun updateForNewJoin(userId: String, joinedMeetingsCount: Int, isFirstJoin: Boolean) {
//        if (isFirstJoin) {
//            tryUpdateBadge(userId, "badge_code03")
//        }
//        if (joinedMeetingsCount >= 5) {
//            tryUpdateBadge(userId, "badge_code04")
//        }
//    }
//
//    fun updateForParticipation(userId: String, finishedScheduleCount: Int) {
//        if (finishedScheduleCount >= 1) {
//            tryUpdateBadge(userId, "badge_code05")
//        }
//        if (finishedScheduleCount >= 5) {
//            tryUpdateBadge(userId, "badge_code06")
//        }
//        if (finishedScheduleCount >= 15) {
//            tryUpdateBadge(userId, "badge_code07")
//        }
//    }
//
//    fun updateForMeetingCreation(userId: String) {
//        tryUpdateBadge(userId, "badge_code08")
//    }
//
//    fun updateForBadgeMaster(userId: String) {
//        if (badges.values.take(8).all { it }) {
//            tryUpdateBadge(userId, "badge_code09")
//        }
//    }
//
//    fun getCurrentBadgeStatus(): Map<String, Boolean> = badges
//}
//
//// 싱글톤 인스턴스 제공을 위한 코드
//object BadgeManagerSingleton {
//    val instance = BadgeManager()
//}
