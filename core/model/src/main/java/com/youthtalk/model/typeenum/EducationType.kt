package com.youthtalk.model.typeenum

enum class EducationType(val educationName: String) {
    UNRESTRICTED("전체 선택"),
    HIGHSCHOOL_BELOW("고졸 미만"),
    HIGHSCHOOL_STUDENT("고교 재학"),
    HIGHSCHOOL_GRADUATED_EXPECTED("고졸 예정"),
    HIGHSCHOOL_GRADUATED("고교 졸업"),
    UNIVERSITY_STUDENT("대학 재학"),
    UNIVERSITY_GRADUATED_EXPECTED("대졸 예정"),
    UNIVERSITY_GRADUATED("대학 졸업"),
    MASTER_DOCTOR("석박사")
}
