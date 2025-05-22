package com.youthtalk.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 1분 미만-방금 전
 * 1분~59분-n분 전
 * 1시간~23시간-n시간 전
 * 1일~6일-n일 전
 * 7일 이상날짜 표기 > xx.xx.xx(년.월.일)
 *
 * */
fun LocalDateTime.getTime(): String {
    val now = LocalDateTime.now()
    val duration = Duration.between(this, now)
    val seconds = 60L
    val hours = seconds * 60L
    val days = hours * 24L
    val weeks = days * 7
    return when {
        duration.seconds < seconds -> "방금 전"
        duration.seconds < hours -> "${duration.toMinutes()}분 전"
        duration.seconds < days -> "${duration.toHours()}시간 전"
        duration.seconds < weeks -> "${duration.toHours() / 24}일 전"
        else -> format(DateTimeFormatter.ofPattern("yy.MM.dd"))
    }
}
