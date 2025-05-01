package com.core.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.policy.R
import com.youthtalk.component.card.BestCard
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.card.PostCard
import com.youthtalk.component.chip.RoundChip
import com.youthtalk.component.item.CategoryItem
import com.youthtalk.component.item.TitleItem
import com.youthtalk.component.topbar.RegionTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray20
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.extentions.shadow
import com.youthtalk.model.Category
import com.youthtalk.model.Region

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onClickPolicySearch: () -> Unit, onClickPopularPolicy: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = gray10),
    ) {
        RegionTopBar(
            region = Region.SEOUL,
            onClickRegion = {},
            onClickSearch = onClickPolicySearch,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 30.dp),
                ) {
                    items(
                        items = Category.entries.toList(),
                    ) {
                        CategoryItem(
                            category = it,
                            onClick = {},
                        )
                    }
                }
            }
            popularPolicy(
                onClickPopularPolicy = onClickPopularPolicy,
            )
            newPolicy()
            realTimePolicy()
            item {
                TitleItem(
                    modifier = Modifier.padding(top = 40.dp, bottom = 14.dp),
                    title = "청년톡톡 Best",
                    onClick = {},
                )
            }

            items(
                count = 10,
            ) {
                BestCard(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp),
                    communityCategory = "자유게시판",
                    communityTitle = "면접 정장 비싸서 걱정했는데 공짜로 해결함!",
                    communitySubTitle = "면접 정장 비싸서 걱정했는데 공짜로 해결함!.....",
                )
            }
        }
    }
}

fun LazyListScope.popularPolicy(modifier: Modifier = Modifier, onClickPopularPolicy: () -> Unit) {
    item {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            TitleItem(
                title = "우리 지역 인기 정책",
                onClick = onClickPopularPolicy,
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(
                    count = 5,
                ) {
                    PolicyCard(
                        modifier = Modifier
                            .width(340.dp)
                            .shadow(
                                offsetX = 4.dp,
                                offsetY = 4.dp,
                                blurRadius = 10.dp,
                            )
                            .background(
                                color = gray10,
                                shape = RoundedCornerShape(10.dp),
                            ),
                    )
                }
            }
        }
    }
}

fun LazyListScope.newPolicy(modifier: Modifier = Modifier) {
    item {
        Column(
            modifier = modifier
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val categories = Category.entries.toList()
            val pagerState = rememberPagerState { categories.size }
            TitleItem(
                title = "따끈따끈한 새로운 정책",
                onClick = {},
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp)
                    .padding(horizontal = 16.dp),
                text = "2024.01.01 기준",
                style = MaterialTheme.typography.displaySmall.copy(
                    color = gray80,
                ),
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            ) {
                items(
                    items = categories,
                ) {
                    RoundChip(
                        text = it.categoryName,
                        isSelected = categories[pagerState.currentPage] == it,
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    repeat(4) {
                        PolicyCard(
                            modifier = Modifier
                                .width(340.dp)
                                .shadow(
                                    offsetX = 4.dp,
                                    offsetY = 4.dp,
                                    blurRadius = 10.dp,
                                )
                                .background(
                                    color = gray10,
                                    shape = RoundedCornerShape(10.dp),
                                ),
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(categories.size) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = if (pagerState.currentPage == it) gray90 else gray40,
                                shape = CircleShape,
                            ),
                    )
                }
            }
        }
    }
}

fun LazyListScope.realTimePolicy(modifier: Modifier = Modifier) {
    item {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            TitleItem(
                title = "실시간 정책 톡톡!",
                isVisibleArrow = false,
                onClick = {},
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .shadow(
                        offsetX = 4.dp,
                        offsetY = 4.dp,
                        blurRadius = 9.dp,
                    )
                    .background(
                        color = gray10,
                        shape = RoundedCornerShape(9.dp),
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(
                                width = 1.dp,
                                color = gray40,
                                shape = RoundedCornerShape(6.dp),
                            )
                            .padding(horizontal = 9.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            modifier = Modifier.size(34.dp),
                            painter = painterResource(R.drawable.image_mock),
                            contentDescription = "정책 아이콘",
                        )
                    }

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 2.dp),
                        text = "서울시 청년안심주택(공공지원민간임대) 임대보증금 지원",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Image(
                        painter = painterResource(R.drawable.fill_around_button),
                        contentDescription = "화면 이동 버튼",
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp, bottom = 30.dp),
                ) {
                    repeat(3) {
                        PostCard(
                            communityTitle = "영화 보는 거 좋아하는 사람? 꿀팁 알려드림!",
                            communitySubTitle = "영화 보는 거 좋아하는 사람? 꿀팁 알려드림! 영화 보는 거 좋아하는 사람...",
                        )
                        if (it != 2) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 20.dp),
                                thickness = 1.dp,
                                color = gray40,
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = gray20,
                            shape = RoundedCornerShape(6.dp),
                        )
                        .padding(horizontal = 30.dp, vertical = 13.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row {
                        Image(
                            painter = painterResource(R.drawable.refresh),
                            contentDescription = "새로고침",
                        )

                        Text(
                            modifier = Modifier.padding(start = 10.dp, end = 4.dp),
                            text = "새로운 정책 더보기",
                            style = MaterialTheme.typography.displayMedium,
                        )

                        Text(
                            text = "1/5",
                            style = MaterialTheme.typography.displayMedium,
                        )
                    }
                }
            }
        }
    }
}
