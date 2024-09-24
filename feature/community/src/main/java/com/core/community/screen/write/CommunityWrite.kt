package com.core.community.screen.write

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.community.model.ContentInfo
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.SearchPolicy
import com.youthtalk.model.WriteInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.immutableListOf

@Composable
fun CommunityWrite(
    type: String,
    title: String,
    contents: ImmutableList<WriteInfo>,
    policies: LazyPagingItems<SearchPolicy>,
    searchPolicy: SearchPolicy?,
    requestFocus: SharedFlow<Int>,
    onClickPicture: () -> Unit,
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
    onPolicyDialogClick: (SearchPolicy?) -> Unit,
    onTitleTextChange: (String) -> Unit,
    onChangeTextValue: (ContentInfo, String) -> Unit,
    onDeleteText: () -> Unit,
    onDeleteImage: (Int) -> Unit,
    createPost: (String) -> Unit,
) {
    val appbarTitle = if (type == "review") "후기글쓰기" else "자유글쓰기"
    var textFieldState by remember { mutableStateOf(TextFieldValue(text = title)) }
    var policyDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
            ),
    ) {
        MiddleTitleTopBar(title = appbarTitle, onBack = onBack)
        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = rememberScrollState(),
                )
                .imePadding(),
        ) {
            TitleEditText(
                textFieldState,
                onTitleTextChange = {
                    textFieldState = it
                    if (it.text != title) {
                        onTitleTextChange(it.text)
                    }
                },
            )
            if (type == "review") {
                PolicySearch(
                    searchPolicy = searchPolicy,
                    onClick = { policyDialog = true },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            ContentEditText(
                modifier = Modifier
                    .height(600.dp)
                    .fillMaxWidth(),
                contents = contents,
                type = type,
                requestFocus = requestFocus,
                onClickPicture = onClickPicture,
                onChangeTextValue = onChangeTextValue,
                onDeleteText = onDeleteText,
                onDeleteImage = onDeleteImage,
            )
            RoundButton(
                modifier = Modifier
                    .padding(horizontal = 17.dp)
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(),
                text = "등록하기",
                enabled = checkEnabled(title, contents, type, searchPolicy),
                color = if (checkEnabled(
                        title,
                        contents,
                        type,
                        searchPolicy,
                    )
                ) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ) {
                createPost(type)
            }
        }
    }

    if (policyDialog) {
        PolicyDialog(
            policies = policies,
            onDismiss = { policyDialog = false },
            onSearch = onSearch,
            onClick = onPolicyDialogClick,
        )
    }
}

private fun checkEnabled(title: String, contents: ImmutableList<WriteInfo>, type: String, searchPolicy: SearchPolicy?): Boolean {
    return (
        title.isNotEmpty() && contents.any {
            it.uri != null || !it.content.isNullOrEmpty()
        } && ((type == "review" && searchPolicy != null) || type == "post")
        )
}

@Preview
@Composable
private fun CommunityWritePreview() {
    val policies = flow<PagingData<SearchPolicy>> { emit(PagingData.from(listOf())) }.collectAsLazyPagingItems()
    YongProjectTheme {
        CommunityWrite(
            type = "",
            title = "",
            contents = immutableListOf<WriteInfo>().toPersistentList(),
            policies = policies,
            searchPolicy = SearchPolicy(title = "partiendo", policyId = "elitr"),
            requestFocus = MutableSharedFlow<Int>(),
            onClickPicture = { },
            onBack = { },
            onSearch = {},
            onPolicyDialogClick = {},
            onTitleTextChange = {},
            onChangeTextValue = { _, _ -> },
            onDeleteText = { /*TODO*/ },
            onDeleteImage = {},
            createPost = {},
        )
    }
}
