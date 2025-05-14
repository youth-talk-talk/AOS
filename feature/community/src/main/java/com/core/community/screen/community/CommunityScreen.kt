package com.core.community.screen.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.community.component.FreePost
import com.core.community.component.ReviewPost
import com.core.community.component.SearchBarComponent
import com.core.community.model.community.CommunityUiEvent
import com.core.community.viewmodel.CommunityViewModel
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray70
import com.youthtalk.model.post.PostSubject
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityViewModel = hiltViewModel(),
    onClickCommunitySearch: (PostSubject) -> Unit,
    onClickPostDetail: (Long) -> Unit,
    onClickCommunityWrite: (PostSubject) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val communityType = PostSubject.entries.toList()
    val pagerState = rememberPagerState { communityType.size }
    val scope = rememberCoroutineScope()
    val lazyColumnStates = List(2) { rememberLazyListState() }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "커뮤니티",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            SearchBarComponent(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                hint = "궁금한 주제가 있나요?",
                onClick = { onClickCommunitySearch(communityType[pagerState.currentPage]) }
            )

            SecondaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = gray10,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier
                            .tabIndicatorOffset(pagerState.currentPage, matchContentSize = false)
                            .padding(horizontal = 16.dp),
                        color = gray100
                    )
                },
                divider = {
                    HorizontalDivider(
                        color = gray70
                    )
                }
            ) {
                communityType.forEachIndexed { index, community ->
                    val title = when (community) {
                        PostSubject.REVIEW -> "후기게시판"
                        PostSubject.FREE -> "자유게시판"
                    }
                    Tab(
                        selected = pagerState.currentPage == index,
                        selectedContentColor = gray10,
                        text = {
                            Text(
                                text = title,
                                style = if (pagerState.currentPage == index) {
                                    MaterialTheme.typography.displayLarge.copy(
                                        color = gray100
                                    )
                                } else {
                                    MaterialTheme.typography.displayMedium.copy(
                                        color = gray70
                                    )
                                }
                            )
                        },
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pagerState
            ) {
                when (communityType[it]) {
                    PostSubject.REVIEW -> ReviewPost(
                        category = state.category,
                        reviews = state.reviews.collectAsLazyPagingItems(),
                        popularReviews = state.popularReviews,
                        lazyListState = lazyColumnStates[it],
                        onClickPost = onClickPostDetail,
                        onClickCategory = { category -> viewModel.setEvent(CommunityUiEvent.ChangeCategory(category)) }
                    )

                    PostSubject.FREE -> FreePost(
                        lazyListState = lazyColumnStates[it],
                        popularFrees = state.popularFrees,
                        frees = state.frees.collectAsLazyPagingItems(),
                        onClickPost = onClickPostDetail
                    )
                }
            }
        }

        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = RoundedCornerShape(100.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = gray10,
            onClick = { onClickCommunityWrite(communityType[pagerState.currentPage]) },
            expanded = !lazyColumnStates[pagerState.currentPage].canScrollBackward,
            icon = {
                Icon(
                    Icons.Filled.Edit,
                    "Extended floating action button.",
                    tint = gray10
                )
            },
            text = {
                Text(
                    text = "글쓰기",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = gray10
                    )
                )
            }
        )
    }
}
