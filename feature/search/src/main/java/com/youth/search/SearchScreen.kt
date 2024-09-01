package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youth.app.feature.search.R
import com.youth.search.model.SearchState
import com.youthtalk.component.PolicyCard
import com.youthtalk.component.SearchChip
import com.youthtalk.component.SortDropDownComponent
import com.youthtalk.component.filter.FilterComponent
import com.youthtalk.component.filter.PolicyFilterBottomSheet
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import com.youthtalk.util.clickableSingle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(type: String) {
    var text by remember {
        mutableStateOf("")
    }
    val navController = rememberNavController()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        SearchBar(
            text = text,
            onChangeText = { text = it },
            searchDone = {
                navController.navigate(SearchState.SEARCH.name) {
                    popUpTo(SearchState.SEARCH.name) {
                        inclusive = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            },
        )
        NavHost(
            navController = navController,
            startDestination = SearchState.NONE.name,
        ) {
            composable(
                route = SearchState.NONE.name,
            ) {
                ResentlySearchScreen(
                    searchClick = {
                        navController.navigate(SearchState.SEARCH.name) {
                            popUpTo(SearchState.SEARCH.name) {
                                inclusive = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                )
            }

            composable(
                route = SearchState.SEARCH.name,
            ) {
                SearchResultScreen(
                    showFilterBottomSheet = { showBottomSheet = true },
                )
            }
        }
    }

    PolicyFilterBottomSheet(
        showBottomSheet = showBottomSheet,
        sheetState = sheetState,
        onDismiss = { showBottomSheet = false },
        filterInfo = null,
        onClickEmploy = {},
        onClickFinished = {},
        onClickReset = {},
        onChangeAge = {},
        onClickApply = {},
    )
}

@Composable
fun SearchResultScreen(showFilterBottomSheet: () -> Unit, isPolicySearch: Boolean = true) {
    val dummyPolicies = getPolicies()
    LazyColumn(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),

    ) {
        if (isPolicySearch) {
            item {
                FilterComponent(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 17.dp)
                        .padding(top = 15.dp)
                        .clickableSingle {
                            showFilterBottomSheet()
                        },
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(12.dp),
            )
            SpecPolicyInfo()
        }

        items(
            count = dummyPolicies.size,
            key = { index -> dummyPolicies[index].policyId },
        ) { index ->
            PolicyCard(
                modifier = Modifier
                    .padding(horizontal = 17.dp)
                    .padding(bottom = 12.dp),
                policy = dummyPolicies[index],
                onClickDetailPolicy = { },
                onClickScrap = {},
            )
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

@Composable
private fun SpecPolicyInfo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
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
            text = "총 1234건의 정책이 있어요",
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
        SortDropDownComponent(
            modifier = Modifier,
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ResentlySearchScreen(searchClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(start = 17.dp),
            text = "최근 검색",
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            repeat(8) { index ->
                SearchChip(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickableSingle { searchClick() },
                    shape = RoundedCornerShape(43.dp),
                    text = "최근 검색${(Math.random() * 10000000).toInt()}",
                )
            }
        }
    }
}

@Composable
private fun SearchBar(text: String, onChangeText: (String) -> Unit, searchDone: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.left_icon),
                contentDescription = "뒤로가기",
                tint = Color.Black,
            )
            BasicTextField(
                value = text,
                onValueChange = onChangeText,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = gray50,
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = { searchDone() },
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text,
                ),
            ) { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = RoundedCornerShape(50.dp),
                        )
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 6.dp),
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "검색 아이콘",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.close_icon),
                        contentDescription = "초기화",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    YongProjectTheme {
        SearchScreen(
            type = "main",
        )
    }
}
