package com.feature.policy.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.feature.policy.extentions.DateUtils
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.chip.DayChip
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.item.TitleItem
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.policy.Policy
import java.time.LocalDate
import kotlin.math.min

@Composable
fun DDayPolicy(
    modifier: Modifier = Modifier,
    count: Int,
    deadlinePolicies: LazyPagingItems<Policy>,
    selectedDay: LocalDate,
    onClickDay: (LocalDate) -> Unit,
    onClickDeadlinePolicy: () -> Unit,
    onClickPolicyDetail: (Long) -> Unit,
    onClickScrap: (Long, Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .heightIn(min = 400.dp)
    ) {
        TitleItem(
            title = "곧 마감되니 서둘러 지원해 보세요!",
            onClick = onClickDeadlinePolicy
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateUtils.getWeeks().forEach {
                DayChip(
                    dayOfWeek = DateUtils.weekToString(it),
                    day = it.dayOfMonth,
                    isSelected = selectedDay == it,
                    onClick = { onClickDay(it) }
                )
            }
        }

        if (count != 0) {
            if (deadlinePolicies.loadState.refresh is LoadState.NotLoading) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 1000.dp)
                        .padding(bottom = 38.dp, start = 16.dp, end = 16.dp),
                    userScrollEnabled = false,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (deadlinePolicies.itemCount != 0) {
                        items(
                            count = min(count, 4),
                            key = deadlinePolicies.itemKey()
                        ) {
                            deadlinePolicies[it]?.let { policy ->
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
                                    onClickScrap = { id, scrap -> onClickScrap(id, scrap) }
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 65.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 65.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyScreen(
                    modifier = Modifier.fillMaxWidth(),
                    emptyTitle = "마감 예정인 정책이 없어요"
                )
            }
        }

        HorizontalDivider(
            thickness = 10.dp,
            color = gray30
        )
    }
}
