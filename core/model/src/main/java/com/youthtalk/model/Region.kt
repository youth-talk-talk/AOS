package com.youthtalk.model

import kotlinx.serialization.Serializable

@Serializable
enum class Region(val region: String) {
    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    SEJONG("세종"),
    DAEJEON("대전"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    GANGWON("강원"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    DAEGU("대구"),
    ULSAN("울산"),
    BUSAN("부산"),
    GWANGJU("광주"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    JEJU("제주"),
    ALL("전국"),
}

fun String.toRegion(): Region = when (this) {
    "서울" -> Region.SEOUL
    "경기" -> Region.GYEONGGI
    "인천" -> Region.INCHEON
    "세종" -> Region.SEJONG
    "대전" -> Region.DAEJEON
    "충북" -> Region.CHUNGBUK
    "충남" -> Region.CHUNGNAM
    "강원" -> Region.GANGWON
    "경북" -> Region.GYEONGBUK
    "경남" -> Region.GYEONGNAM
    "대구" -> Region.DAEGU
    "울산" -> Region.ULSAN
    "부산" -> Region.BUSAN
    "광주" -> Region.GWANGJU
    "전북" -> Region.JEONBUK
    "전남" -> Region.JEONNAM
    "제주" -> Region.JEJU
    else -> Region.ALL
}

fun Region.toRegionName(): String = when (this) {
    Region.SEOUL -> "서울"
    Region.GYEONGGI -> "경기"
    Region.INCHEON -> "인천"
    Region.SEJONG -> "세종"
    Region.DAEJEON -> "대전"
    Region.CHUNGBUK -> "충북"
    Region.CHUNGNAM -> "충남"
    Region.GANGWON -> "강원"
    Region.GYEONGBUK -> "경북"
    Region.GYEONGNAM -> "경남"
    Region.DAEGU -> "대구"
    Region.ULSAN -> "울산"
    Region.BUSAN -> "부산"
    Region.GWANGJU -> "광주"
    Region.JEONBUK -> "전북"
    Region.JEONNAM -> "전남"
    Region.JEJU -> "제주"
    else -> "전국"
}
