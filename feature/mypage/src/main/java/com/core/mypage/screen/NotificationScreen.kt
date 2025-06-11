package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.mypage.R
import com.youthtalk.component.notification.NotificationComponent
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.NotificationType
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val list = listOf("청년 정책", "커뮤니티")
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { list.size }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        NoBackMiddleTitleTopBar(
            title = "알림",
            tails = {
                IconButton(
                    onBack
                ) {
                    Image(
                        painter = painterResource(R.drawable.close),
                        contentDescription = stringResource(R.string.close)
                    )
                }
            }
        )

        TabRow(
            containerColor = gray10,
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                            .padding(horizontal = 16.dp),
                        color = gray100
                    )
                }
            },
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = gray40
                )
            }
        ) {
            list.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    selected = pagerState.currentPage == index,
                    selectedContentColor = gray10,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState
        ) {
            NotificationList(communityType = it)
        }
    }
}

@Composable
fun NotificationList(modifier: Modifier = Modifier, communityType: Int) {
    val list = when (communityType) {
        0 -> listOf(NotificationType.SCRAP, NotificationType.SCRAP, NotificationType.BOOK_MARK)
        else -> listOf(NotificationType.COMMUNITY, NotificationType.COMMUNITY, NotificationType.COMMUNITY)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = list.size
        ) {
            NotificationComponent(
                notificationTitle = "스크랩 한 정책이 오늘 마감돼요!",
                notificationSubTitle = "사상구 면접 A to Z 운영’ 정책이 오늘 마감돼요! 지금 확인해 볼까요?",
                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                notificationType = list[it]
            )
        }

        item {
            Text(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                text = "지난 알림",
                style = MaterialTheme.typography.displayLarge
            )
        }

        items(
            count = list.size
        ) {
            NotificationComponent(
                notificationTitle = "스크랩 한 정책이 오늘 마감돼요!",
                notificationSubTitle = "사상구 면접 A to Z 운영’ 정책이 오늘 마감돼요! 지금 확인해 볼까요?",
                notificationType = list[it]
            )
        }
    }
}
