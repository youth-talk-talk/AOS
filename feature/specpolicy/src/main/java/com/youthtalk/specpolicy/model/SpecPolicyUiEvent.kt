package com.youthtalk.specpolicy.model

import com.youthtalk.model.Category
import com.youthtalk.model.EmploymentCode

sealed interface SpecPolicyUiEvent {
    data class GetData(val category: Category) : SpecPolicyUiEvent
    data class ChangeEmployCode(val employmentCode: EmploymentCode) : SpecPolicyUiEvent
    data class ChangeFinished(val isFinished: Boolean) : SpecPolicyUiEvent
    data class ClickScrap(val id: String) : SpecPolicyUiEvent
    data object GetFilterInfo : SpecPolicyUiEvent
    data object FilterReset : SpecPolicyUiEvent
    data object FilterApply : SpecPolicyUiEvent
    data class ChangeAge(val age: String) : SpecPolicyUiEvent
}
