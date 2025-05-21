package com.youthtalk.model.policy

import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import com.youthtalk.model.typeenum.toRegionName

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
