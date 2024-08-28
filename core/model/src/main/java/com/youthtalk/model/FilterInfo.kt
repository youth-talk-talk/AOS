package com.youthtalk.model

data class FilterInfo(
    val age: Int?,
    val employmentCodeList: List<EmploymentCode>?,
    val isFinished: Boolean?,
)
