package com.core.community.screen.write

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.youth.app.feature.community.R
import com.youthtalk.component.button.Round6Button
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.policy.SearchPolicy
import kotlinx.coroutines.delay

@Composable
fun PolicySearchScreen(
    modifier: Modifier = Modifier,
    searchPolicies: LazyPagingItems<SearchPolicy>,
    searchPolicy: String,
    onSearchPolicyChange: (String) -> Unit,
    onSearchPolicy: (String) -> Unit,
    onBack: () -> Unit,
    onClickSearchPolicy: (SearchPolicy) -> Unit
) {
    var searchIndex by remember {
        mutableStateOf<SearchPolicy?>(null)
    }

    LaunchedEffect(searchPolicy) {
        delay(300)
        if (searchPolicy.isNotEmpty()) {
            searchIndex = null
            onSearchPolicy(searchPolicy)
        }
    }

    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = gray10)
    ) {
        NoBackMiddleTitleTopBar(
            title = "정책 검색",
            tails = {
                IconButton(onBack) {
                    Image(
                        painter = painterResource(R.drawable.close),
                        contentDescription = "닫기",
                        colorFilter = ColorFilter.tint(color = gray100)
                    )
                }
            }
        )

        BasicTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 10.dp),
            value = searchPolicy,
            onValueChange = onSearchPolicyChange,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.titleSmall
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = gray30,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.search),
                    contentDescription = "검색",
                    colorFilter = ColorFilter.tint(color = gray70)
                )

                Box(modifier = Modifier.weight(1f)) {
                    if (searchPolicy.isEmpty()) {
                        Text(
                            text = "정책명을 검색해 주세요",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = gray70
                            )
                        )
                    }
                    innerTextField()
                }

                if (searchPolicy.isNotEmpty()) {
                    Image(
                        modifier = Modifier
                            .padding(start = 4.dp),
                        painter = painterResource(R.drawable.closecircle),
                        contentDescription = "초기화"
                    )
                }
            }
        }

        if (searchPolicy.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "후기를 작성할 정책을 검색헤 보세요.",
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = gray80
                    )
                )
            }
        } else {
            if (searchPolicies.itemCount != 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (searchPolicies.loadState.refresh is LoadState.NotLoading) {
                        items(
                            count = searchPolicies.itemCount
                        ) {
                            searchPolicies[it]?.let { policy ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = if (policy == searchIndex) MaterialTheme.colorScheme.onPrimary else gray10
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            searchIndex = policy
                                        }
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.policy),
                                        contentDescription = "정책아이콘"
                                    )

                                    Text(
                                        text = policy.policyTitle,
                                        style = MaterialTheme.typography.displaySmall
                                    )
                                }

                                if (it != searchPolicies.itemCount - 1) {
                                    HorizontalDivider(
                                        color = gray40
                                    )
                                }
                            }
                        }
                    }

                    if (searchPolicies.loadState.refresh is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillParentMaxHeight(0.8f),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }

            if (searchPolicies.itemCount == 0 && searchPolicies.loadState.refresh is LoadState.NotLoading) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Spacer(modifier = Modifier.weight(2f))
                    EmptyScreen(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f),
                        emptyTitle = "검색 결과가 존재하지 않습니다."
                    )
                }
            }

            if (searchPolicies.itemCount != 0 && searchPolicies.loadState.refresh is LoadState.NotLoading) {
                Round6Button(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 26.dp),
                    text = "추가하기",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = gray10
                ) {
                    searchIndex?.let { policy ->
                        onClickSearchPolicy(policy)
                    }
                }
            }
        }
    }
}
