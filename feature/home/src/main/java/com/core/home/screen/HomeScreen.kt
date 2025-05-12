package com.core.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.core.home.viewmodel.HomeViewModel
import com.youth.app.core.designsystem.R as Design
import com.youth.app.feature.policy.R
import com.youthtalk.component.card.BestCard
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.card.ReviewCard
import com.youthtalk.component.chip.RoundChip
import com.youthtalk.component.empty.EmptyScreen
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
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.policy.PoliciesWithReview
import com.youthtalk.model.policy.Policy
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onClickPolicySearch: () -> Unit,
    onClickPopularPolicy: () -> Unit,
    onClickNewPolicy: () -> Unit,
    onClickPolicyDetail: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = gray10)
        ) {
            RegionTopBar(
                region = uiState.user.region,
                onClickRegion = {},
                onClickSearch = onClickPolicySearch
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 30.dp)
                    ) {
                        items(
                            items = Category.entries.toList()
                        ) {
                            CategoryItem(
                                category = it,
                                onClick = {}
                            )
                        }
                    }
                }
                popularPolicy(
                    popularPolices = uiState.homeData.popularPolicies,
                    onClickPopularPolicy = onClickPopularPolicy,
                    onClickPolicyDetail = onClickPolicyDetail
                )
                newPolicy(
                    newPolicies = uiState.homeData.newPolicies,
                    onClickNewPolicy = onClickNewPolicy
                )
                realTimePolicy(
                    policiesWithReviews = uiState.homeData.policiesWithReviews
                )
                item {
                    TitleItem(
                        modifier = Modifier.padding(top = 40.dp, bottom = 14.dp),
                        title = "청년톡톡 Best",
                        onClick = {}
                    )
                }

                items(
                    count = uiState.homeData.bestPosts.size
                ) {
                    val bestPost = uiState.homeData.bestPosts[it]
                    BestCard(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 8.dp),
                        post = bestPost
                    )
                }
            }
        }
    }
}

fun LazyListScope.popularPolicy(
    modifier: Modifier = Modifier,
    popularPolices: List<Policy>,
    onClickPopularPolicy: () -> Unit,
    onClickPolicyDetail: () -> Unit
) {
    item {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TitleItem(
                title = "우리 지역 인기 정책",
                onClick = onClickPopularPolicy
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(
                    count = if (popularPolices.size > 10) 10 else popularPolices.size
                ) {
                    PolicyCard(
                        modifier = Modifier
                            .width(340.dp)
                            .shadow(
                                offsetX = 4.dp,
                                offsetY = 4.dp,
                                blurRadius = 10.dp
                            )
                            .background(
                                color = gray10,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        onClick = onClickPolicyDetail,
                        policy = popularPolices[it],
                        isVisibleScrap = true
                    )
                }
            }
        }
    }
}

fun LazyListScope.newPolicy(modifier: Modifier = Modifier, newPolicies: NewPolicies, onClickNewPolicy: () -> Unit) {
    item {
        val scope = rememberCoroutineScope()
        val categories = Category.entries.toList()
        val pagerState = rememberPagerState { categories.size }
        val lazyState = rememberLazyListState()
        LaunchedEffect(pagerState.currentPage) {
            lazyState.animateScrollToItem(pagerState.currentPage)
        }

        Column(
            modifier = modifier
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleItem(
                title = "따끈따끈한 새로운 정책",
                onClick = onClickNewPolicy
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp)
                    .padding(horizontal = 16.dp),
                text = "${LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))} 기준",
                style = MaterialTheme.typography.displaySmall.copy(
                    color = gray80
                )
            )

            LazyRow(
                state = lazyState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
            ) {
                items(
                    count = categories.size
                ) {
                    RoundChip(
                        text = categories[it].categoryName,
                        isSelected = categories[pagerState.currentPage] == categories[it],
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                                lazyState.animateScrollToItem(it)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val items = when (categories[it]) {
                        Category.ALL -> newPolicies.all
                        Category.DWELLING -> newPolicies.dwelling
                        Category.EDUCATION -> newPolicies.education
                        Category.JOB -> newPolicies.job
                        Category.LIFE -> newPolicies.life
                        Category.PARTICIPATION -> newPolicies.participation
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(525.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (items.isEmpty()) {
                            Spacer(modifier = Modifier.weight(1f))
                            EmptyScreen(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(5f)
                                    .background(color = gray10)
                                    .padding(top = 65.dp),
                                emptyTitle = "새로운 정책이 없습니다."
                            )
                        } else {
                            items.take(4).forEach {
                                PolicyCard(
                                    modifier = Modifier
                                        .width(340.dp)
                                        .shadow(
                                            offsetX = 4.dp,
                                            offsetY = 4.dp,
                                            blurRadius = 10.dp
                                        )
                                        .background(
                                            color = gray10,
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    policy = it
                                )
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(categories.size) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = if (pagerState.currentPage == it) gray90 else gray40,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

fun LazyListScope.realTimePolicy(modifier: Modifier = Modifier, policiesWithReviews: List<PoliciesWithReview>) {
    item {
        var index by remember {
            mutableStateOf(0)
        }

        val policy = policiesWithReviews[index]
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TitleItem(
                title = "실시간 정책 톡톡!",
                isVisibleArrow = false,
                onClick = {}
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .shadow(
                        offsetX = 4.dp,
                        offsetY = 4.dp,
                        blurRadius = 9.dp
                    )
                    .background(
                        color = gray10,
                        shape = RoundedCornerShape(9.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(
                                width = 1.dp,
                                color = gray40,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (policy.departmentImgUrl.isNullOrEmpty()) {
                            Image(
                                modifier = Modifier.size(34.dp),
                                painter = painterResource(Design.drawable.default_policy),
                                contentDescription = "정책 아이콘"
                            )
                        } else {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(policy.departmentImgUrl)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                    }

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 2.dp),
                        text = policy.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Image(
                        painter = painterResource(R.drawable.fill_around_button),
                        contentDescription = "화면 이동 버튼"
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp, bottom = 30.dp)
                ) {
                    policy.reviews.forEachIndexed { index, review ->
                        ReviewCard(
                            review = review,
                            onClick = {}
                        )
                        if (index != policy.reviews.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 20.dp),
                                thickness = 1.dp,
                                color = gray40
                            )
                        }
                    }
                }

                if (policiesWithReviews.size > 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = gray20,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                index = (index + 1) % policiesWithReviews.size
                            }
                            .padding(horizontal = 30.dp, vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            Image(
                                painter = painterResource(R.drawable.refresh),
                                contentDescription = "새로고침"
                            )

                            Text(
                                modifier = Modifier.padding(start = 10.dp, end = 4.dp),
                                text = "새로운 정책 더보기",
                                style = MaterialTheme.typography.displayMedium
                            )

                            Text(
                                text = "${index + 1}/${policiesWithReviews.size}",
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
