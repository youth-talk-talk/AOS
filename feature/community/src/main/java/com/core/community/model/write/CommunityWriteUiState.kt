package com.core.community.model.write

import androidx.compose.ui.text.input.TextFieldValue
import com.core.base.model.UiState
import com.core.community.model.Contents
import com.youthtalk.model.Image
import com.youthtalk.model.post.PostSubject

data class CommunityWriteUiState(
    val title: String,
    val postType: PostSubject,
    val policyId: Long?,
    val policyName: String?,
    val contentList: List<Contents>,
    val images: List<Image>,
    val uploadLoading: Boolean,
    val focusIndex: Pair<Int, TextFieldValue?>
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
            focusIndex = Pair(0, TextFieldValue(""))
        )
    }
}
