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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.feature.policy.extentions.DateUtils
import com.feature.policy.model.deadline.DeadlineUiEvent
import com.feature.policy.viewmodel.DeadlineViewmodel
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.chip.DayChip
import com.youthtalk.component.dropdown.SortTypeDropDown
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40

@Composable
fun DeadlinePolicyScreen(
    viewModel: DeadlineViewmodel = hiltViewModel(),
    onBack: () -> Unit,
    onClickPolicyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val policies = state.policies.collectAsLazyPagingItems()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "마감 임박 정책",
            onBack = onBack
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateUtils.getWeeks().forEach {
                DayChip(
                    dayOfWeek = DateUtils.weekToString(it),
                    day = it.dayOfMonth,
                    isSelected = state.selectedDay == it,
                    onClick = { if (state.selectedDay != it) viewModel.setEvent(DeadlineUiEvent.SelectedDay(it)) }
                )
            }
        }

        HorizontalDivider(color = gray30)

        if (state.count != 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                        DeadlineUiEvent.SelectedSortType(
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
                                onClick = { onClickPolicyDetail(policy.policyId) },
                                onClickScrap = { id, scrap -> }
                            )
                        }
                    }
                }

                if (policies.itemCount == 0 && policies.loadState.refresh is LoadState.NotLoading) {
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

        if (state.count == 0) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5f),
                emptyTitle = "최근 본 정책이 없어요"
            )
        }
    }
}
