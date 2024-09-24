package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.youthtalk.component.PolicyCard
import com.youthtalk.component.filter.FilterComponent
import com.youthtalk.component.filter.PolicyFilterBottomSheet
import com.youthtalk.model.EmploymentCode
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import com.youthtalk.util.clickableSingle

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchPolicies(
    policies: LazyPagingItems<Policy>,
    count: Int,
    map: Map<String, Boolean>,
    filterInfo: FilterInfo,
    onClickDetailPolicy: (String) -> Unit,
    onClickScrap: (String, Boolean) -> Unit,
    postFilterInfo: (FilterInfo) -> Unit,
    getFilterInfo: () -> Unit,
    onClickFilterApply: () -> Unit,
) {
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        item {
            FilterComponent(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 17.dp)
                    .padding(top = 15.dp)
                    .clickableSingle {
                        showBottomSheet = true
                        getFilterInfo()
                    },
            )
        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(12.dp),
            )
            SpecPolicyInfo(
                count = count,
                title = "정책",
            )
        }

        items(
            count = policies.itemCount,
            key = { index -> policies.peek(index)?.policyId ?: "" },
        ) { index ->
            policies[index]?.let { policy ->
                PolicyCard(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp),
                    policy = policy.copy(scrap = map.getOrDefault(policy.policyId, policy.scrap)),
                    onClickDetailPolicy = onClickDetailPolicy,
                    onClickScrap = onClickScrap,
                )
            }
        }
    }

    PolicyFilterBottomSheet(
        showBottomSheet = showBottomSheet,
        sheetState = sheetState,
        onDismiss = { showBottomSheet = false },
        filterInfo = filterInfo,
        onClickEmploy = {
            val newEmployList =
                if (filterInfo.employmentCodeList == null) {
                    listOf(it)
                } else {
                    filterInfo.employmentCodeList?.let { employList ->
                        if (it == EmploymentCode.ALL) {
                            null
                        } else if (employList.contains(it)) {
                            (employList - it).ifEmpty { null }
                        } else {
                            if ((employList + it).size == EmploymentCode.entries.size - 1) null else (employList + it)
                        }
                    }
                }
            postFilterInfo(filterInfo.copy(employmentCodeList = newEmployList))
        },
        onClickFinished = { postFilterInfo(filterInfo.copy(isFinished = it)) },
        onClickReset = { postFilterInfo(FilterInfo(null, null, null)) },
        onChangeAge = { postFilterInfo(filterInfo.copy(age = it.toInt())) },
        onClickApply = onClickFilterApply,
    )
}
