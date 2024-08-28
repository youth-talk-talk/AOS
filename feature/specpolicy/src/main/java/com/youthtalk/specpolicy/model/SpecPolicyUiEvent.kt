package com.youthtalk.specpolicy.model

import com.youthtalk.model.Category

sealed interface SpecPolicyUiEvent {
    data class GetData(val category: Category) : SpecPolicyUiEvent
}
