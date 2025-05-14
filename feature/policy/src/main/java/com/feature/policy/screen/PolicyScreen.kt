package com.feature.policy.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.feature.policy.component.DDayPolicy
import com.feature.policy.component.RecentViewPolicy
import com.feature.policy.model.policy.PolicyUiEvent
import com.feature.policy.viewmodel.PolicyViewModel
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.chip.RoundChip
import com.youthtalk.component.dropdown.SortTypeDropDown
import com.youthtalk.component.item.TitleItem
import com.youthtalk.component.sheet.RegionBottomSheet
import com.youthtalk.component.topbar.RegionTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import java.text.DecimalFormat

@Composable
fun PolicyScreen(
    modifier: Modifier = Modifier,
    viewModel: PolicyViewModel = hiltViewModel(),
    onClickRecentViewPolicy: () -> Unit,
    onClickDeadlinePolicy: () -> Unit,
    onClickPolicyOverView: (Category) -> Unit,
    onClickPolicyDetail: (Long) -> Unit,
    onClickPolicySearch: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val categories = Category.entries.toList()
    val allPolicies = state.categoryPolicies.collectAsLazyPagingItems()
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
    ) {
        RegionTopBar(
            region = state.user.region,
            onClickRegion = { bottomSheet = true },
            onClickSearch = onClickPolicySearch
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                RecentViewPolicy(
                    policies = state.recentlyPolicies,
                    onClickRecentViewPolicy = onClickRecentViewPolicy,
                    onClickPolicyDetail = onClickPolicyDetail
                )
            }
            item {
                DDayPolicy(
                    selectedDay = state.selectedDay,
                    count = state.deadlineCount,
                    deadlinePolicies = state.deadlinePolicies.collectAsLazyPagingItems(),
                    onClickDay = { viewModel.setEvent(PolicyUiEvent.SelectedDay(it)) },
                    onClickDeadlinePolicy = onClickDeadlinePolicy,
                    onClickPolicyDetail = onClickPolicyDetail
                )
            }
            item {
                TitleItem(
                    modifier = Modifier.padding(top = 20.dp, bottom = 15.dp),
                    title = "모든 정책 한눈에 보기",
                    onClick = { onClickPolicyOverView(Category.ALL) }
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
                ) {
                    items(
                        count = categories.size
                    ) {
                        RoundChip(
                            text = categories[it].categoryName,
                            isSelected = state.selectCategory == categories[it],
                            onClick = {
                                viewModel.setEvent(PolicyUiEvent.SelectCategory(category = categories[it], sortType = state.sortType))
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "총 ${DecimalFormat("#,###").format(state.allCount)}건",
                        style = MaterialTheme.typography.displayMedium
                    )

                    SortTypeDropDown(
                        sortType = state.sortType,
                        onClickSort = { viewModel.setEvent(PolicyUiEvent.SelectCategory(category = state.selectCategory, sortType = it)) }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            listLazyColumn(policies = allPolicies, onClickPolicyDetail = onClickPolicyDetail)
        }
    }

    if (bottomSheet) {
        RegionBottomSheet(
            region = state.user.region,
            onDismiss = { bottomSheet = false }
        ) {
            viewModel.setEvent(PolicyUiEvent.PostRegion(state.user, it ?: Region.ALL))
        }
    }
}

fun LazyListScope.listLazyColumn(policies: LazyPagingItems<Policy>, onClickPolicyDetail: (Long) -> Unit) {
    if (policies.itemCount != 0 && policies.loadState.refresh is LoadState.NotLoading) {
        items(
            count = policies.itemCount,
            key = policies.itemKey()
        ) { index ->
            policies[index]?.let { policy ->
                PolicyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 14.dp)
                        .background(
                            color = gray10,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = gray40,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    policy = policy,
                    onClick = { onClickPolicyDetail(policy.policyId) }
                )
            }
        }
    }

    if (policies.loadState.refresh is LoadState.Loading || policies.itemCount == 0) {
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
}
