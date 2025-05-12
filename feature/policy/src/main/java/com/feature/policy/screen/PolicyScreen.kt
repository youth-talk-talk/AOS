package com.feature.policy.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.feature.policy.component.DDayPolicy
import com.feature.policy.component.RecentViewPolicy
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.item.TitleItem
import com.youthtalk.component.topbar.RegionTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.model.Category
import com.youthtalk.model.Region
import java.time.LocalDate
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PolicyScreen(
    modifier: Modifier = Modifier,
    onClickRecentViewPolicy: () -> Unit,
    onClickDeadlinePolicy: () -> Unit,
    onClickPolicyOverView: () -> Unit
) {
    var selectDay by remember {
        mutableStateOf(LocalDate.now())
    }
    val categories = Category.entries.toList()
    var pagerState = rememberPagerState { categories.size }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var isScrollingUp by remember { mutableStateOf(true) }
    var lastOffset by remember { mutableStateOf(0f) }

    LaunchedEffect(scrollState.value) {
        Timber.e("scrollState ${scrollState.value}, lastOffset $lastOffset")
        isScrollingUp = scrollState.value <= lastOffset
        lastOffset = scrollState.value.toFloat()
    }

    Column(
        modifier = modifier
    ) {
        AnimatedVisibility(visible = isScrollingUp) {
            RegionTopBar(
                region = Region.SEOUL,
                onClickRegion = {},
                onClickSearch = {}
            )
        }

        BoxWithConstraints {
            val screenHeight = this.maxHeight
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
            ) {
                RecentViewPolicy(
                    onClickRecentViewPolicy = onClickRecentViewPolicy
                )
                DDayPolicy(
                    selectedDay = selectDay,
                    onClickDay = { selectDay = it },
                    onClickDeadlinePolicy = onClickDeadlinePolicy
                )

                Column(
                    modifier = Modifier
                        .height(screenHeight)
                ) {
                    TitleItem(
                        modifier = Modifier.padding(top = 20.dp, bottom = 15.dp),
                        title = "모든 정책 한눈에 보기",
                        onClick = onClickPolicyOverView
                    )

                    PrimaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = gray10,
                        divider = {
                            HorizontalDivider(
                                color = gray70
                            )
                        }
                    ) {
                        categories.forEachIndexed { index, category ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                selectedContentColor = gray10,
                                text = {
                                    Text(
                                        text = category.categoryName,
                                        style = if (pagerState.currentPage == index) {
                                            MaterialTheme.typography.displayLarge.copy(
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            MaterialTheme.typography.displaySmall.copy(
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
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxHeight()
                            .nestedScroll(
                                remember {
                                    object : NestedScrollConnection {
                                        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                                            Timber.e("NestedScrollConnection onPostFling consumed : $consumed, available $available")
                                            return super.onPostFling(consumed, available)
                                        }

                                        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                                            Timber.e("NestedScrollConnection onPostScroll consumed : $consumed, available $available")
                                            return super.onPostScroll(consumed, available, source)
                                        }

                                        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                            if (available.y != 0f) {
                                                isScrollingUp = available.y > 0
                                            }
                                            Timber.e("NestedScrollConnection onPreScroll $available")
                                            return if (available.y > 0) {
                                                Offset.Zero
                                            } else {
                                                Offset(
                                                    x = 0f,
                                                    y = -scrollState.dispatchRawDelta(-available.y)
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                    ) { page: Int ->
                        ListLazyColumn(50)
                    }
                }
            }
        }
    }
}

@Composable
fun ListLazyColumn(items: Int) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { index ->
            PolicyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = gray10,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = gray40,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}
