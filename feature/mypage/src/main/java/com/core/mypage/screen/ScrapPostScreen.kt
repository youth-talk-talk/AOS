package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.core.mypage.model.scrappost.ScrapPostUiEvent
import com.core.mypage.viewmodel.ScrapPostViewModel
import com.core.navigation.model.ScrapPostType
import com.youth.app.feature.mypage.R
import com.youthtalk.component.card.PostCard
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray40
import com.youthtalk.extentions.rememberLazyListState

@Composable
fun ScrapPostScreen(
    modifier: Modifier = Modifier,
    viewModel: ScrapPostViewModel = hiltViewModel(),
    onClickPostDetail: (Long) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val posts = state.posts.collectAsLazyPagingItems()
    val lazyListState = posts.rememberLazyListState()
    var isRefresh by rememberSaveable {
        mutableStateOf(false)
    }
    LifecycleResumeEffect(Unit) {
        if (isRefresh) {
            viewModel.setEvent(ScrapPostUiEvent.RefreshCount)
            isRefresh = false
        }

        onPauseOrDispose {
            isRefresh = true
        }
    }

    if (!state.isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            NoBackMiddleTitleTopBar(
                title = when (state.type) {
                    ScrapPostType.MY -> "작성한 글"
                    ScrapPostType.SCRAP -> "스크랩한 게시글"
                },
                tails = {
                    Image(
                        modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onBack() },
                        painter = painterResource(R.drawable.close),
                        contentDescription = stringResource(R.string.close)
                    )
                }
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                state = lazyListState
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 14.dp),
                        text = when (state.type) {
                            ScrapPostType.MY -> "작성글 ${state.count}"
                            ScrapPostType.SCRAP -> "게시글 ${state.count}"
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                if (state.count != 0) {
                    if (posts.loadState.refresh is LoadState.NotLoading) {
                        items(
                            count = posts.itemCount,
                            key = posts.itemKey()
                        ) {
                            posts[it]?.let { post ->
                                PostCard(
                                    post = post,
                                    isVisiblePolicyTitle = true,
                                    onClick = { onClickPostDetail(post.postId) },
                                    onClickScrap = { postId, scrap -> viewModel.setEvent(ScrapPostUiEvent.PostScrapPost(postId, scrap)) }
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 20.dp),
                                color = gray40,
                                thickness = 1.dp
                            )
                        }
                    }

                    if (posts.loadState.refresh is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                } else {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize()
                        ) {
                            Spacer(modifier = Modifier.weight(2f))
                            EmptyScreen(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(5f),
                                emptyTitle = "게시글이 없습니다."
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
private fun ScrapPostScreenPreview() {
    YongProjectTheme {
        ScrapPostScreen(
            onClickPostDetail = {},
            onBack = {}
        )
    }
}
