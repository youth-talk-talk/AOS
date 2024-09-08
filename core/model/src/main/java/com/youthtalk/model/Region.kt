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
    ALL("전국");
}

fun String.toRegion() : Region = when (this) {
    "서울특별시" -> Region.SEOUL
    "경기도" -> Region.GYEONGGI
    "인천광역시" -> Region.INCHEON
    "세종특별자치시" -> Region.SEJONG
    "대전광역시" -> Region.DAEJEON
    "충청북도" -> Region.CHUNGBUK
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

fun Region.toRegionName() : String = when (this) {
    Region.SEOUL -> "서울특별시"
    Region.GYEONGGI -> "경기도"
     Region.INCHEON -> "인천광역시"
     Region.SEJONG -> "세종특별자치시"
     Region.DAEJEON -> "대전광역시"
     Region.CHUNGBUK -> "충청북도"
     Region.CHUNGNAM -> "충청남도"
     Region.GANGWON -> "강원도"
     Region.GYEONGBUK -> "경상북도"
     Region.GYEONGNAM -> "경상남도"
     Region.DAEGU -> "대구광역시"
     Region.ULSAN -> "울산광역시"
     Region.BUSAN -> "부산광역시"
     Region.GWANGJU -> "광주광역시"
     Region.JEONBUK -> "전라북도"
     Region.JEONNAM -> "전라남도"
     Region.JEJU -> "제주특별자치도"
    else -> "전체"
}

