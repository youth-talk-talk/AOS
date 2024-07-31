package com.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.home.component.CategoryCard
import com.core.home.component.HomeAppBar
import com.core.home.component.SearchScreen
import com.core.home.model.HomeUiState
import com.youth.app.feature.home.R
import com.youthtalk.component.PolicyCard
import com.youthtalk.component.PolicyCheckBox
import com.youthtalk.component.PopularCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import kotlinx.collections.immutable.ImmutableList

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val top5Policies = getTop5Policies()
    val policies = policies()

    val uiState by viewModel.uiState.collectAsState()
    val success = (uiState as? HomeUiState.Success) ?: HomeUiState.Success()
    Surface {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            HomeAppBar()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                item { SearchScreen() }
                item { CategoryScreen() }
                item { PopularTitle() }
                item { PopularPolicyScreen(top5Policies) }
                item {
                    UpdateTitle(
                        categoryFilters = success.categoryList,
                        onCheck = viewModel::setCategoryCheck,
                    )
                }
                items(
                    count = policies.size,
                ) { index ->
                    UpdatePolicyScreen(policies[index])
                }
            }
        }
    }
}

@Composable
private fun PopularPolicyScreen(top5Policies: List<Policy>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2.2f)
            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            .padding(horizontal = 17.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = top5Policies,
            key = { item: Policy -> item.policyId },
        ) { policy ->
            PopularCard(
                modifier = Modifier
                    .aspectRatio(14 / 15f),
                policy = policy,
            )
        }
    }
}

@Composable
private fun PopularTitle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            )
            .padding(
                horizontal = 17.dp,
                vertical = 12.dp,
            ),
    ) {
        Text(
            text = "인기 정책",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
private fun UpdatePolicyScreen(policy: Policy) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            .padding(horizontal = 17.dp),
    ) {
        PolicyCard(
            modifier = Modifier.padding(bottom = 12.dp),
            policy = policy,
        )
    }
}

@Composable
private fun UpdateTitle(categoryFilters: ImmutableList<Category>, onCheck: (Category?) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            .padding(
                horizontal = 17.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "최근 업데이트",
            style = MaterialTheme.typography.headlineSmall,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            stringArrayResource(id = R.array.categories).forEach {
                PolicyCheckBox(
                    isCheck = categoryFilters
                        .any { category: Category -> category.categoryName == it },
                    title = it,
                    textStyle = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    onCheck(Category.entries.find { category: Category -> category.categoryName == it })
                }
            }
        }
    }
}

@Composable
private fun CategoryScreen() {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Category.entries.forEach { category ->
            CategoryCard(
                category = category.categoryName,
                painter = when (category) {
                    Category.JOB -> painterResource(id = R.drawable.recruit)
                    Category.EDUCATION -> painterResource(id = R.drawable.teacher)
                    Category.LIFE -> painterResource(id = R.drawable.book)
                    Category.PARTICIPATION -> painterResource(id = R.drawable.participation)
                },
            )
        }
    }
}

private fun getTop5Policies(): List<Policy> = listOf(
    Policy(
        policyId = "R2023081716945",
        category = Category.JOB,
        title = "국민 취업지원 제도1",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
    Policy(
        policyId = "R2023081716946",
        category = Category.JOB,
        title = "국민 취업지원 제도2",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
    Policy(
        policyId = "R2023081716947",
        category = Category.JOB,
        title = "국민 취업지원 제도3",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
    Policy(
        policyId = "R2023081716948",
        category = Category.JOB,
        title = "국민 취업지원 제도4",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
    Policy(
        policyId = "R2023081716949",
        category = Category.JOB,
        title = "국민 취업지원 제도5",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
)

private fun policies(): List<Policy> = listOf(
    Policy(
        policyId = "R2023081716945",
        category = Category.JOB,
        title = "국민 취업지원 제도1",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
    Policy(
        policyId = "R2023081716946",
        category = Category.JOB,
        title = "국민 취업지원 제도2",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
    Policy(
        policyId = "R2023081716947",
        category = Category.JOB,
        title = "국민 취업지원 제도3",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
    Policy(
        policyId = "R2023081716948",
        category = Category.JOB,
        title = "국민 취업지원 제도4",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
    Policy(
        policyId = "R2023081716949",
        category = Category.JOB,
        title = "국민 취업지원 제도5",
        deadlineStatus = "",
        hostDep = "국토교통부",
        scrap = false,
    ),
)

@Preview
@Composable
private fun HomeScreenPreview() {
    YongProjectTheme {
        HomeScreen()
    }
}
