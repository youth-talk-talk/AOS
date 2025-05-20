package com.youthtalk.model

import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import com.youthtalk.model.typeenum.toRegionName

/**
 *     "departmentImgUrl": "https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/CenterDep/31.+%E1%84%89%E1%85%A1%E1%86%AB%E1%84%85%E1%85%B5%E1%86%B7%E1%84%8E%E1%85%A5%E1%86%BC.png",
 *     "isScrap": false,
 *     "recruitmentType": "모집 마감",
 *     "title": "산림산업 창업지원_청년 산림창업 마중물 지원 ",
 *     "region": "전국",
 *     "category": "일자리",
 *     "hostDep": "산림청",
 *     "applyTerm": "2025.03.28 ~ 2025.04.11",
 *     "introduction": "청년법인 도약 및 사업성공을 위한 청년 산림창업 마중물 지원하여 열악한 청년 창업 여건을 개선하여 산촌 정착 저변 확대 ",
 *     "age": "만 19세 ~ 만 39세",
 *     "supportDetail": "1. 전문가 멘토링(희망분야) 및 운영사무국이 제공하는 창업성장 관련 교육\n2. 시제품 개발, 홍보, 마케팅 등에 소요되는 사업화 예산",
 *     "addition": "창업 7년 이하의 개인 또는 법인사업자",
 *     "applLimit": "○ 과거 부정수급으로 적발되어 약정 해지된 단체(법인, 기업 등)\n○ 신청서, 사업화 계획 등 신청에 관한 서류를 허위로 기재한 경우\n○ 금융기관의 계좌개설이 불가능하거나, 본인 명의의 금융자산에 대한 압류가 진행 중인자(시효 소멸자는 제외) \n○ 국세 또는 지방세를 체납 중인 자 \n○ 동일 사업화 아이디어로 정부부처, 지방자치단체, 공공기관에서 시행하는 창업지원사업에 참여하여 현금성 창업 지원금을 받은 자로, 타 사업과 지원기간(협약기간)이 중복되는 경우\n○ 한국임업진흥원 및 정부지원사업에 참여제한으로 제재중인 개인 및 법인사업자 ",
 *     "applStep": "○ 신청 접수 : 3.28.(금) ~ 4.11.(금)\n○ 서류ㆍ발표 평가 : 4월 중\n○ 결과발표 : 5.2.(금)\n○ 사업화지원 : 5~9월",
 *     "submitDoc": "□ 자격조건\n 가. 업력 : 창업 7년 이하의 개인 또는 법인사업자\n   * 창업일 기준 : (개인) 사업자등록증 개업연월일, (법인) 법인등기부등본 회사성립연월일\n   * 신청자격 대상기업 : '18. 3. 29. ~ '25. 3. 28. 기간 중 사업을 개시한 자\n 나. 산림분야 : 산림자원을 활용한 재화생산ㆍ서비스공급 사업을 영위하는 자\n 다. 청년 : 대한민국 국적의 19세 이상  39세 이하 인자 \n   * 신청자격 출생기간 : 1985. 3. 29. ~ 2006. 3. 28. 출생자",
 *     "evaluation": "□ 선정 및 지원 절차\n ○ 자격검토 및 서류평가\n  - 평가방법 : 접수 마감일까지 제출된 신청서, 증빙서류에 대한 심사 \n  - 평가항목 : 신청자격 검토, 사업계획 적정성, 실현가능성, 성장전략 등 평가\n    * 가점사항 : (1점) 벤처기업인증 기업, 산림형 예비사회적기업, 사회적기업\n                      (0.5점) '24년도 청년 산림창업가 시너지캠프 참가자, 청년 산림창업활성화 프로그램 수료자\n ○ 발표 평가\n  - 평가방법 : 신청자(대표자)의 창업사업화 모델, 사업화 추진계획 등에 대한 발표평가(발표 및 질의 응답)\n   - 평가항목 : 기술성, 실현 가능성, 성장 가능성, 사업화 추진계획 등\n   * 발표자는 신청자(대표인) 본인으로 하며, 평가 불참 시 선정 대상에서 제외\n  ** 평가결과 기준점수(70점) 미달 시 선정하지 않을 수 있음\n ○ 결과발표 : 개별통지 및 홈페이지 게시\n   - 발표평가 고득점자 순으로 지원대상자 최종 선정 및 사업화 자금(정부지원금) 확정 후 결과 공지\n   - 최종 선정 후 선정 인원 20% 내외 예비합격자 선정\n",
 *     "applUrl": "www.kofpi.or.kr",
 *     "refUrl1": null,
 *     "refUrl2": null,
 *     "etc": null,
 *     "subRegion": null,
 *     "earnEtc": null,
 *     "specialization": null,
 *     "major": "인문계열, 사회계열, 상경계열, 이학계열, 공학계열, 예체능계열, 농산업계열, 기타",
 *     "education": "고졸 미만, 고교 재학, 고졸 예정, 고교 졸업, 대학 재학, 대졸 예정, 대학 졸업, 석·박사, 기타",
 *     "marriage": null,
 *     "employment": null
 *
 * */

data class PolicyDetail(
    val departmentImgUrl: String?,
    val recruitmentType: String?,
    val region: Region?,
    val subRegion: String?,
    val category: Category?,
    val title: String?,
    val introduction: String?,
    val supportDetail: String?,
    val applyTerm: String?,
    val age: String?,
    val education: String?,
    val major: String?,
    val employment: String?,
    val specialization: String?,
    val applLimit: String?,
    val addition: String?,
    val applStep: String?,
    val evaluation: String?,
    val applUrl: String?,
    val submitDoc: String?,
    val etc: String?,
    val hostDep: String?,
    val refUrl1: String?,
    val refUrl2: String?,
    val isScrap: Boolean,
    val earnEtc: String?,
    val marriage: String?
) {
    val applyQualifications: Boolean = !age.isNullOrEmpty() || region != null || !applLimit.isNullOrEmpty() ||
        !subRegion.isNullOrEmpty() || !etc.isNullOrEmpty() || !earnEtc.isNullOrEmpty() || !specialization.isNullOrEmpty() ||
        !major.isNullOrEmpty() || !education.isNullOrEmpty() || !marriage.isNullOrEmpty() || !employment.isNullOrEmpty()

    fun getApplyQualifications(): List<String> {
        val regionInfo = if (region == null && subRegion == null) {
            null
        } else {
            region?.toRegionName() + subRegion?.let { " $it" }
        }
        val list = mutableListOf(
            age, regionInfo, earnEtc, major, education, employment, specialization, marriage, etc
        )

        applLimit?.let {
            list.add("참여 제한 대상 참고")
            list.add(it)
        }

        return list.filterNotNull()
    }

    val applyMethod: Boolean = !applStep.isNullOrEmpty() || !applUrl.isNullOrEmpty() || !refUrl1.isNullOrEmpty() || !refUrl2.isNullOrEmpty() ||
        !evaluation.isNullOrEmpty() || !submitDoc.isNullOrEmpty()

    fun getApplyMethod(): List<String> {
        return listOfNotNull(applStep, submitDoc, evaluation, applUrl, refUrl1, refUrl2)
    }
}
