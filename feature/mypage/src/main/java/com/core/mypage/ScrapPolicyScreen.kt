package com.core.mypage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.mypage.model.scrappolicy.ScrapPolicyUiEvent
import com.core.mypage.model.scrappolicy.ScrapPolicyUiState
import com.core.mypage.viewmodel.ScrapPolicyViewModel
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.component.PolicyCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.flowOf

@Composable
fun ScrapPolicyScreen(onBack: () -> Unit, viewModel: ScrapPolicyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is ScrapPolicyUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is ScrapPolicyUiState.Success -> {
            val state = (uiState as ScrapPolicyUiState.Success)
            ScrapScreen(
                policies = state.policies.collectAsLazyPagingItems(),
                deleteScrap = state.deleteScrapMap,
                onClickScrap = { id, scrap -> viewModel.uiEvent(ScrapPolicyUiEvent.DeleteScrap(id, scrap)) },
                onBack,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScrapScreen(
    policies: LazyPagingItems<Policy>,
    deleteScrap: Map<String, Boolean>,
    onClickScrap: (String, Boolean) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        MiddleTitleTopBar(
            title = "스크랩한 정책",
            onBack = onBack,
        )
        HorizontalDivider()

        LazyColumn(
            contentPadding = PaddingValues(start = 17.dp, end = 17.dp, top = 12.dp),
        ) {
            items(
                count = policies.itemCount,
                key = { index -> policies.peek(index)?.policyId ?: "" },
            ) { index ->
                policies[index]?.let { policy ->
                    if (!deleteScrap.containsKey(policy.policyId)) {
                        PolicyCard(
                            modifier = Modifier
                                .animateItemPlacement(),
                            policy = policy,
                            onClickDetailPolicy = {},
                            onClickScrap = onClickScrap,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

fun getPolicies(): List<Policy> {
    return listOf(
        Policy(
            policyId = "R2024062424307",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = true,
        ),
        Policy(
            policyId = "R2024062424308",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = true,
        ),
        Policy(
            policyId = "R2024062424309",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = true,
        ),
        Policy(
            policyId = "R2024062424310",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = true,
        ),
        Policy(
            policyId = "R2024062424311",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = true,
        ),
        Policy(
            policyId = "R2024062424312",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = true,
        ),
        Policy(
            policyId = "R2024062424313",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = true,
        ),
        Policy(
            policyId = "R2024062424314",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = true,
        ),
    )
}

@Preview
@Composable
private fun ScrapPolicyScreenPreview() {
    YongProjectTheme {
        ScrapScreen(
            policies = flowOf(PagingData.from(getPolicies())).collectAsLazyPagingItems(),
            deleteScrap = mapOf(),
            onClickScrap = { _, _ -> },
            onBack = {},
        )
    }
}
