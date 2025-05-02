package com.feature.policy.extentions

import java.time.DayOfWeek
import java.time.LocalDate

object DateUtils {
    fun getWeeks(): List<LocalDate> = List(7) { LocalDate.now().plusDays(it.toLong()) }
    fun weekToString(date: LocalDate): String {
        return if (date == LocalDate.now()) {
            "오늘"
        } else {
            when (date.dayOfWeek) {
                DayOfWeek.MONDAY -> "월"
                DayOfWeek.THURSDAY -> "화"
                DayOfWeek.WEDNESDAY -> "수"
                DayOfWeek.TUESDAY -> "목"
                DayOfWeek.FRIDAY -> "금"
                DayOfWeek.SATURDAY -> "토"
                DayOfWeek.SUNDAY -> "일"
            }
        }
    }
}
