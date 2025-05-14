package com.youth.search.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.youth.app.feature.search.R
import com.youth.search.component.FilterChip
import com.youthtalk.component.button.FilterButton
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.dropdown.SortTypeDropDown
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.sheet.FilterBottomSheet
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.FilterType
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType
import com.youthtalk.util.SpecializedUtils
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicySearchResultScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    count: Int,
    searchFilter: SearchFilter,
    sortType: SortType,
    policies: LazyPagingItems<Policy>,
    applyFilter: (SearchFilter, SortType) -> Unit,
    onClickPolicyDetail: (Long) -> Unit
) {
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var startIndex by remember { mutableStateOf(0) }
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

                    val filterCount = when (filters[it]) {
                        FilterType.POLICY_TYPE -> searchFilter.category?.size ?: 0
                        FilterType.REGION -> searchFilter.region?.size ?: 0
                        FilterType.RECRUIT -> searchFilter.employment?.size ?: 0
                        FilterType.EDUCATION -> searchFilter.education?.size ?: 0
                        FilterType.SPECIALIZED -> {
                            val specialCount = (SpecializedUtils.getFilterList(searchFilter.specialization)?.size ?: 0)
                            val marriedCount = if (searchFilter.marriage != null) 1 else 0
                            specialCount + marriedCount
                        }

                        FilterType.AGE_EARN -> {
                            val earnCount = if (searchFilter.minEarn != null || searchFilter.maxEarn != null) 1 else 0
                            val ageCount = if (searchFilter.age != null) 1 else 0
                            earnCount + ageCount
                        }
                    }

                    FilterChip(
                        text = title,
                        count = filterCount,
                        onClick = {
                            startIndex = it
                            bottomSheet = true
                        }
                    )
                }
            }
            if (!searchFilter.isAllNull()) {
                Spacer(modifier.height(12.dp))
                PolicyFilterInfo(
                    searchFilter = searchFilter,
                    onDeleteSearchFilter = { applyFilter(it, sortType) }
                )
            }
        }

        item {
            HorizontalDivider(
                modifier = Modifier.padding(top = 14.dp, bottom = 10.dp),
                thickness = 1.dp,
                color = gray30
            )
        }

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

                SortTypeDropDown(
                    sortType = sortType,
                    onClickSort = { applyFilter(searchFilter, it) }
                )
            }
        }

        if (!isLoading) {
            if (policies.loadState.refresh is LoadState.NotLoading) {
                if (count != 0) {
                    items(
                        count = policies.itemCount,
                        key = policies.itemKey()
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
                                policy = policy,
                                onClick = { onClickPolicyDetail(policy.policyId) }
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
        } else {
            item {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (bottomSheet) {
        FilterBottomSheet(
            sheetState = state,
            searchFilter = searchFilter,
            startIndex = startIndex,
            onDismiss = { bottomSheet = false },
            onClick = { applyFilter(it, sortType) }
        )
    }
}

@Composable
fun PolicyFilterInfo(modifier: Modifier = Modifier, searchFilter: SearchFilter, onDeleteSearchFilter: (SearchFilter) -> Unit) {
    Row(
        modifier = modifier
            .height(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(modifier = Modifier.width(6.dp))
            searchFilter.category?.forEach { category ->
                FilterButton(text = category.categoryName) {
                    val filter = (searchFilter.category ?: listOf()) - category
                    onDeleteSearchFilter(searchFilter.copy(category = filter.ifEmpty { null }))
                }
            }

            searchFilter.region?.forEach { region ->
                FilterButton(text = region) {
                    val filter = (searchFilter.region ?: listOf()) - region
                    onDeleteSearchFilter(searchFilter.copy(region = filter.ifEmpty { null }))
                }
            }

            searchFilter.employment?.forEach { employment ->
                FilterButton(text = employment.employmentName) {
                    val filter = (searchFilter.employment ?: listOf()) - employment
                    onDeleteSearchFilter(searchFilter.copy(employment = filter.ifEmpty { null }))
                }
            }

            searchFilter.education?.forEach { education ->
                FilterButton(text = education.educationName) {
                    val filter = (searchFilter.education ?: listOf()) - education
                    onDeleteSearchFilter(searchFilter.copy(education = filter.ifEmpty { null }))
                }
            }

            SpecializedUtils.getFilterList(searchFilter.specialization)?.forEach { special ->
                FilterButton(text = special.specialName) {
                    val filter = (searchFilter.specialization ?: listOf()) - special
                    onDeleteSearchFilter(searchFilter.copy(specialization = filter.ifEmpty { null }))
                }
            }

            searchFilter.marriage?.let { marry ->
                FilterButton(text = marry.marriageName) {
                    onDeleteSearchFilter(searchFilter.copy(marriage = null))
                }
            }

            if (searchFilter.minEarn != null && searchFilter.maxEarn != null) {
                searchFilter.earnToString()?.let { value ->
                    FilterButton(text = value) {
                        onDeleteSearchFilter(searchFilter.copy(minEarn = null, maxEarn = null))
                    }
                }
            }

            searchFilter.age?.let { age ->
                FilterButton(text = "${age}세") {
                    onDeleteSearchFilter(searchFilter.copy(age = null))
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 10.dp),
            color = gray40
        )
        Image(
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDeleteSearchFilter(SearchFilter(keyword = searchFilter.keyword))
                },
            painter = painterResource(R.drawable.refresh),
            contentDescription = "새로고침",
            colorFilter = ColorFilter.tint(color = gray80)
        )
    }
}
