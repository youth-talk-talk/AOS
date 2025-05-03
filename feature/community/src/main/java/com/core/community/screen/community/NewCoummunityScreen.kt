package com.core.community.screen.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.core.community.component.FreePost
import com.core.community.component.ReviewPost
import com.core.community.component.SearchBarComponent
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray70
import com.youthtalk.model.Category
import com.youthtalk.model.CommunityType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCommunityScreen(modifier: Modifier = Modifier, onClickCommunitySearch: (CommunityType) -> Unit) {
    val communityType = CommunityType.entries.toList()
    val pagerState = rememberPagerState { communityType.size }
    val scope = rememberCoroutineScope()
    val categories = Category.entries.toList()
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            Text(
                text = "커뮤니티",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        SearchBarComponent(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            hint = "궁금한 주제가 있나요?",
            onClick = { onClickCommunitySearch(communityType[pagerState.currentPage]) },
        )

        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = gray10,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(pagerState.currentPage, matchContentSize = false)
                        .padding(horizontal = 16.dp),
                    color = gray100,
                )
            },
            divider = {
                HorizontalDivider(
                    color = gray70,
                )
            },
        ) {
            communityType.forEachIndexed { index, community ->
                val title = when (community) {
                    CommunityType.REVIEW -> "후기게시판"
                    CommunityType.FREE -> "자유게시판"
                }
                Tab(
                    selected = pagerState.currentPage == index,
                    selectedContentColor = gray10,
                    text = {
                        Text(
                            text = title,
                            style = if (pagerState.currentPage == index) {
                                MaterialTheme.typography.displayLarge.copy(
                                    color = gray100,
                                )
                            } else {
                                MaterialTheme.typography.displayMedium.copy(
                                    color = gray70,
                                )
                            },
                        )
                    },
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
        ) {
            when (communityType[it]) {
                CommunityType.REVIEW -> ReviewPost(
                    categories = categories,
                )

                CommunityType.FREE -> FreePost()
            }
        }
    }
}
