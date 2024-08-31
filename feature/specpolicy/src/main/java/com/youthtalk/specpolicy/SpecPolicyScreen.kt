package com.youthtalk.specpolicy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.navigation.Nav
import com.youth.app.feature.specpolicy.R
import com.youthtalk.component.PolicyCard
import com.youthtalk.component.filter.FilterComponent
import com.youthtalk.component.filter.PolicyFilterBottomSheet
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import com.youthtalk.specpolicy.component.SpecPolicyTopBar
import com.youthtalk.specpolicy.model.SpecPolicyUiEvent
import com.youthtalk.specpolicy.model.SpecPolicyUiState
import com.youthtalk.util.clickableSingle

@Composable
fun SpecPolicyScreen(
    category: Category,
    viewModel: SpecPolicyViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = category.categoryName) {
        if (uiState is SpecPolicyUiState.Loading) {
            viewModel.uiEvent(SpecPolicyUiEvent.GetData(category))
        }
    }
    when (uiState) {
        is SpecPolicyUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is SpecPolicyUiState.Success -> {
            SpecPolicyScreen(
                category,
                navController,
                uiState as SpecPolicyUiState.Success,
                onClickPolicyDetail = {
                    navController.navigate("${Nav.PolicyDetail.route}/$it") {
                        restoreState = true
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpecPolicyScreen(
    category: Category,
    navController: NavHostController,
    uiState: SpecPolicyUiState.Success,
    onClickPolicyDetail: (String) -> Unit,
) {
    val policies = uiState.polices.collectAsLazyPagingItems()
    val policyCount = uiState.policyCount
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        SpecPolicyTopBar(
            title = category.categoryName,
            onBack = {
                navController.popBackStack()
            },
            onClickSearch = {
                navController.navigate("${Nav.Search.route}/main")
            },
        )

        LazyColumn {
            item {
                FilterComponent(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp)
                        .clickableSingle {
                            showBottomSheet = true
                        },
                )
            }

            item {
                SpecPolicyInfo(
                    policyCount = policyCount,
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
                        policy = policy,
                        onClickDetailPolicy = { onClickPolicyDetail(it) },
                    )
                }
            }
        }
    }

    PolicyFilterBottomSheet(
        filterInfo = uiState.filterInfo,
        sheetState = sheetState,
        showBottomSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false },
    )
}

@Composable
private fun SpecPolicyInfo(policyCount: Int) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            )
            .padding(horizontal = 17.dp)
            .padding(top = 15.dp, bottom = 17.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "총 ${policyCount}건의 정책이 있어요",
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        Icon(
            modifier = Modifier.padding(end = 30.dp),
            painter = painterResource(R.drawable.smile_icon),
            contentDescription = "스파일 아이콘",
            tint = MaterialTheme.colorScheme.onSecondary,
        )
        Spacer(modifier = Modifier.weight(1f))
//        SortDropDownComponent(
//            modifier = Modifier,
//        )
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
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424308",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424309",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424310",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424311",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424312",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424313",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
        Policy(
            policyId = "R2024062424314",
            category = Category.LIFE,
            title = "든든전세주택 입주자 모집 시작",
            deadlineStatus = "D-168",
            hostDep = "국토교통부 주택정책관 주택기금과\n주거복지정책관 주거복지지원과",
            scrap = false,
        ),
    )
}

@Preview
@Composable
private fun SpecPolicyScreenPreview() {
    YongProjectTheme {
        SpecPolicyScreen(
            category = Category.JOB,
        )
    }
}
