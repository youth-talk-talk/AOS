package com.youthtalk.model.typeenum

import kotlinx.serialization.Serializable

@Serializable
enum class SpecializedType(val specialName: String) {
    UNRESTRICTED("전체 선택"),
    SMALL_BUSINESS("중소기업"),
    FARMER("농업인"),
    SOLDIER("군인"),
    WOMEN("여성"),
    WELFARE_RECIPIENT("기초생활수급자"),
    DISABLED("장애인"),
    SINGLE_PARENT("한부모가정"),
    REGIONAL_TALENT("지역인재"),
    OTHER("기타")
}
