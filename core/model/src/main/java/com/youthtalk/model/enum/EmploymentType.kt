package com.youthtalk.model.enum

enum class EmploymentType(val employmentName: String) {
    UNRESTRICTED("전체 선택"),
    EMPLOYED("재직자"),
    SELF_EMPLOYED("자영업자"),
    UNEMPLOYED("미취업자"),
    FREELANCER("프리랜서"),
    DAILY_WORKER("일용근로자"),
    ENTREPRENEUR("예비창업자"),
    TEMPORARY_WORKER("단기근로자"),
    FARMER("영농종사자"),
    OTHER("기타")
}
