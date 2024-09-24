package com.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.LazyPagingItems
import com.core.home.component.HomeAppBar
import com.core.home.component.SearchScreen
import com.core.home.model.HomeUiState
import com.youthtalk.model.Category
import com.youthtalk.model.Policy

@Composable
fun HomeMain(
    uiState: HomeUiState.Success,
    allPolicies: LazyPagingItems<Policy>,
    homeLazyListScrollState: LazyListState,
    onCheck: (Category?) -> Unit,
    onClickDetailPolicy: (String) -> Unit,
    onClickSpecPolicy: (Category) -> Unit,
    onClickSearch: () -> Unit,
    onClickScrap: (String, Boolean) -> Unit,
) {
    Surface {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            HomeAppBar()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                state = homeLazyListScrollState,
            ) {
                item {
                    SearchScreen(
                        onClick = onClickSearch,
                    )
                }
                item {
                    CategoryScreen(
                        goSpecPolicyScreen = onClickSpecPolicy,
                    )
                }
                item { PopularTitle() }
                item {
                    val populars = uiState.popularPolicies.map { it.copy(scrap = uiState.scrap[it.policyId] ?: it.scrap) }
                    PopularPolicyScreen(
                        populars,
                        onClickDetailPolicy,
                        onClickScrap = onClickScrap,
                    )
                }
                item {
                    UpdateTitle(
                        categoryFilters = uiState.categoryList,
                        onCheck = onCheck,
                    )
                }
                items(
                    count = allPolicies.itemCount,
                    key = { index -> allPolicies.peek(index)!!.policyId },
                ) { index ->
                    allPolicies[index]?.let { policy ->
                        val checkPolicyScrap = policy.copy(scrap = uiState.scrap.getOrDefault(policy.policyId, policy.scrap))
                        UpdatePolicyScreen(
                            policy = checkPolicyScrap,
                            onClickDetailPolicy = onClickDetailPolicy,
                            onClickScrap = onClickScrap,
                        )
                    }
                }
            }
        }
    }
}
