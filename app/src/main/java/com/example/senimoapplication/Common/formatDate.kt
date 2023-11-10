package com.example.senimoapplication.Common

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun formatDate(inputDate: String): String {
    // 입력 스트링의 형식을 지정합니다.
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // 출력 형식을 지정하고 요일을 한국어로 표시합니다.
    val outputFormat = SimpleDateFormat("yyyy'년' MM'월' dd'일' (E) HH:mm", Locale.KOREA)

    try {
        // 입력 스트링을 파싱합니다.
        val date = inputFormat.parse(inputDate)

        // 출력 형식으로 포맷팅합니다.
        val formattedDate = outputFormat.format(date)

        return formattedDate
    } catch (e: Exception) {
        e.printStackTrace()
        return "" // 파싱 또는 포맷팅 오류 발생 시 빈 문자열 반환
    }
}

fun myScheduleDate(inputDate: String): String {
    // 입력 스트링의 형식을 지정합니다.
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // 출력 형식을 지정하고 요일을 한국어로 표시합니다.
    val outputFormat = SimpleDateFormat("MM'.' dd (E) HH:mm", Locale.KOREA)

    try {
        // 입력 스트링을 파싱합니다.
        val date = inputFormat.parse(inputDate)

        // 출력 형식으로 포맷팅합니다.
        val formattedDate = outputFormat.format(date)

        return formattedDate
    } catch (e: Exception) {
        e.printStackTrace()
        return "" // 파싱 또는 포맷팅 오류 발생 시 빈 문자열 반환
    }
}


fun dDate(inputDate: String): String {
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val eventDate = dateFormat.parse(inputDate)
        val currentDate = Date()

        // 두 날짜 사이의 시간 차이 계산
        val diffInMillies = eventDate.time - currentDate.time
        val days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

        // D-day 문자열 생성
        val dDayString = if (days == 0L) {
            "D-day"
        } else {
            "D-${days + 1}"
        }

        return dDayString
    } catch (e: Exception) {
        // 날짜 형식이 잘못된 경우 또는 파싱 에러 발생 시 처리
        return "Invalid Date"
    }
}

fun main() {
    val inputDate = "2023-11-18 19:00"
    val formattedDate = formatDate(inputDate)
    println(formattedDate)
}

