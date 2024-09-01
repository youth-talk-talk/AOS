package com.youthtalk.model

import kotlinx.serialization.Serializable

@Serializable
enum class EmploymentCode(
    val employName: String
) {
    ALL("전체선택"),
    EMPLOYED("재직자"),
    SELF_EMPLOYED("자영업자"),
    UNEMPLOYED("미취업자"),
    FREELANCER("프리랜서"),
    DAILY_WORKER("일용근로자"),
    ENTREPRENEUR("예비창업자"),
    TEMPORARY_WORKER("단기근로자"),
    FARMER("영농종사자"),
    NO_RESTRICTION("제한없음"),
    OTHER("기타")
}
