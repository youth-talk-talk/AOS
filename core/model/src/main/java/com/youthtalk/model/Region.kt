package com.youthtalk.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.Serializable

@Serializable
enum class Region(val region: String) {
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHEON("인천"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    JEJU("제주"),
    SEJONG("세종"),
    ALL("전국");
}

fun String.toRegion() : Region = when (this) {
    "서울특별시" -> Region.SEOUL
    "경기도" -> Region.GYEONGGI
    "인천광역시" -> Region.INCHEON
    "세종특별자치시" -> Region.SEJONG
    "대전광역시" -> Region.DAEJEON
    "충척북도" -> Region.CHUNGBUK
    "충청남도" -> Region.CHUNGNAM
    "강원도" -> Region.GANGWON
    "경상북도" -> Region.GYEONGBUK
    "경상남도" -> Region.GYEONGNAM
    "대구광역시" -> Region.DAEGU
    "울산광역시" -> Region.ULSAN
    "부산광역시" -> Region.BUSAN
    "광주광역시" -> Region.GWANGJU
    "전라북도" -> Region.JEONBUK
    "전라남도" -> Region.JEONNAM
    "제주특별자치도" -> Region.JEJU
    else -> Region.ALL
}

