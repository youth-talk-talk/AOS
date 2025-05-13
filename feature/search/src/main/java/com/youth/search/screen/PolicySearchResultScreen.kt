package com.youth.search.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.youth.app.feature.search.R
import com.youth.search.component.FilterChip
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.sheet.FilterBottomSheet
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.FilterType
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.search.SearchFilter
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicySearchResultScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    count: Int,
    searchFilter: SearchFilter,
    policies: LazyPagingItems<Policy>,
    applyFilter: (SearchFilter) -> Unit
) {
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    count = FilterType.entries.toList().size
                ) {
                    val filters = FilterType.entries.toList()

                    val title = when (filters[it]) {
                        FilterType.POLICY_TYPE -> "정책분야"
                        FilterType.REGION -> "지역"
                        FilterType.RECRUIT -> "취업상태"
                        FilterType.EDUCATION -> "학력"
                        FilterType.SPECIALIZED -> "특화 분야"
                        FilterType.AGE_EARN -> "연령 및 소득"
                    }

                    FilterChip(
                        text = title,
                        onClick = {
                            bottomSheet = true
                        }
                    )
                }
            }
        }

        item {
            HorizontalDivider(
                modifier = Modifier.padding(top = 14.dp, bottom = 10.dp),
                thickness = 1.dp,
                color = gray30
            )
        }

        if (!isLoading) {
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
                        text = "총 ${DecimalFormat("#,###").format(count)}건",
                        style = MaterialTheme.typography.displayMedium
                    )

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "최신순",
                            style = MaterialTheme.typography.displayMedium
                        )

                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.arrowdown),
                            contentDescription = "아래 화살표"
                        )
                    }
                }
            }

            items(
                count = policies.itemCount,
                key = { policies[it]?.policyId ?: 0 }
            ) { index ->
                policies[index]?.let { policy ->
                    PolicyCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                            .background(
                                color = gray10,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = gray40,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        policy = policy
                    )
                }
            }
        } else {
            item {
                Column(
                    modifier = Modifier.fillParentMaxHeight(0.8f)
                ) {
                    Spacer(modifier = Modifier.weight(2f))
                    EmptyScreen(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f),
                        emptyTitle = "일치하는 결과가 없습니다."
                    )
                }
            }
        }
    }

    if (bottomSheet) {
        FilterBottomSheet(
            sheetState = state,
            searchFilter = searchFilter,
            onDismiss = { bottomSheet = false },
            onClick = applyFilter
        )
    }
}
