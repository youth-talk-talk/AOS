package com.feature.policy.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.feature.policy.model.overview.OverViewUiEvent
import com.feature.policy.viewmodel.PolicyOverviewViewModel
import com.youth.app.feature.policy.R
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.dropdown.SortTypeDropDown
import com.youthtalk.component.item.CategoryItem
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.Category

@Composable
fun PolicyOverviewScreen(
    modifier: Modifier = Modifier,
    viewModel: PolicyOverviewViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onClickPolicyDetail: (Long) -> Unit,
    onClickPolicySearch: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val policies = state.policies.collectAsLazyPagingItems()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "정책 모아보기",
            onBack = onBack,
            tails = {
                IconButton(onClick = onClickPolicySearch) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(com.youth.app.core.designsystem.R.drawable.search),
                        contentDescription = "검색"
                    )
                }
            }
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(
                items = Category.entries.toList()
            ) {
                CategoryItem(
                    category = it,
                    isSelected = it == state.category,
                    onClick = { if (it != state.category) viewModel.setEvent(OverViewUiEvent.ChangeCategory(category = it)) }
                )
            }
        }

        HorizontalDivider(
            color = gray30
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "총 ${java.text.DecimalFormat("#,###").format(state.count)}건",
                        style = MaterialTheme.typography.displayMedium
                    )

                    SortTypeDropDown(
                        sortType = state.sortType,
                        onClickSort = { sortType ->
                            if (state.sortType != sortType) {
                                viewModel.setEvent(
                                    OverViewUiEvent.SelectedSortType(
                                        category = state.category,
                                        sortType = sortType
                                    )
                                )
                            }
                        }
                    )
                }
            }

            if (policies.itemCount != 0 && policies.loadState.refresh is LoadState.NotLoading) {
                items(
                    count = policies.itemCount,
                    key = policies.itemKey()
                ) {
                    policies[it]?.let { policy ->
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
                                ),
                            policy = policy,
                            onClick = { onClickPolicyDetail(policy.policyId) }
                        )
                    }
                }
            }

            if (policies.itemCount == 0 || policies.loadState.refresh is LoadState.Loading) {
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
    }
}
