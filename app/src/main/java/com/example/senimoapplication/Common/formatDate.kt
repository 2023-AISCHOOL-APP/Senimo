package com.example.senimoapplication.Common

import android.content.Context
import com.example.senimoapplication.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

fun formatDate(inputDate: String): String {
    // 입력 날짜의 형식을 정의합니다. (ISO 8601 포맷, UTC 시간대)
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC") // 입력 포맷의 시간대를 UTC로 설정합니다.

    // 출력할 날짜의 형식을 정의합니다. (한국 시간대, '년 월 일 (요일) 시간' 형식)
    val outputFormat = SimpleDateFormat("yyyy'년' MM'월' dd'일' (E) HH:mm", Locale.KOREA)
    outputFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul") // 출력 포맷의 시간대를 한국 시간대로 설정합니다.

    try {
        // 입력 문자열을 Date 객체로 파싱합니다.
        val date = inputFormat.parse(inputDate) ?: return ""

        // Date 객체를 새로운 포맷으로 변환합니다.
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        return "날짜 변환 오류" // 예외 발생 시 사용자에게 보여줄 메시지 반환
    }
}

fun myScheduleDate(inputDate: String): String {
    // 입력 스트링의 형식을 지정합니다.
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    // 출력 형식을 지정하고 요일을 한국어로 표시합니다.
    val outputFormat = SimpleDateFormat("MM'.' dd (E) HH:mm", Locale.KOREA)
    outputFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
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

fun photoUploadTime(inputDate: String): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM월 dd일 a hh:mm", Locale.getDefault())

        // 입력된 타임스탬프 문자열을 파싱
        val date = inputFormat.parse(inputDate)

        // 출력 형식에 맞게 포맷팅
        return outputFormat.format(date)
    } catch (e: Exception) {
        // 날짜 형식이 잘못된 경우 또는 파싱 오류 발생 시 처리
        return "Invalid Date"
    }
}

fun myChatListDate(inputDateString: String, context: Context): String {
    // 입력 스트링의 형식을 지정합니다.
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    try {
        // 입력 스트링을 파싱합니다.
        val date = inputFormat.parse(inputDateString)
        date?.let {
            val currentTime = System.currentTimeMillis()
            val diff = currentTime - date.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            return when {
                seconds < 60 -> context.getString(R.string.just_now)
                minutes < 60 -> context.getString(R.string.minutes_ago, minutes.toInt())
                hours < 24 -> context.getString(R.string.hours_ago, hours.toInt())
                days < 2 -> context.getString(R.string.days_ago, days.toInt())
                else -> SimpleDateFormat("MM'.' dd (E) HH:mm", Locale.KOREA).format(date)
            }
        } ?: run {
            return "" // 날짜 파싱 실패 시 빈 문자열 반환
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        return "" // 예외 발생 시 빈 문자열 반환
    }
}







fun main() {
    val inputDate = "2023-11-18 19:00"
    val formattedDate = formatDate(inputDate)
    println(formattedDate)
}

