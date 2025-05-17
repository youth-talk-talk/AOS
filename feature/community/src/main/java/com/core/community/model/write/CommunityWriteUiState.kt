package com.core.community.model.write

import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.core.base.model.UiState
import com.core.community.model.Contents
import com.youthtalk.model.Image
import com.youthtalk.model.policy.SearchPolicy
import com.youthtalk.model.post.PostSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CommunityWriteUiState(
    val title: String,
    val postType: PostSubject,
    val policyId: Long?,
    val policyName: String?,
    val searchPolicy: String,
    val contentList: List<Contents>,
    val images: List<Image>,
    val uploadLoading: Boolean,
    val focusIndex: Pair<Int, TextFieldValue?>,
    val searchPolicies: Flow<PagingData<SearchPolicy>>
) : UiState {
    companion object {
        val initState = CommunityWriteUiState(
            title = "",
            postType = PostSubject.REVIEW,
            policyId = null,
            policyName = null,
            contentList = listOf(Contents.Text(TextFieldValue(""))),
            images = listOf(),
            uploadLoading = false,
            focusIndex = Pair(0, TextFieldValue("")),
            searchPolicy = "",
            searchPolicies = emptyFlow()
        )
    }

    val isValid: Boolean
        get() = if (postType == PostSubject.REVIEW) {
            policyName != null && contentList != listOf(Contents.Text(TextFieldValue(""))) &&
                title.isNotEmpty()
        } else {
            contentList != listOf(Contents.Text(TextFieldValue(""))) &&
                title.isNotEmpty()
        }
}
