package com.core.home

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.home.component.CategoryCard
import com.core.home.component.HomeAppBar
import com.core.home.component.SearchScreen
import com.core.home.model.HomeUiState
import com.core.navigation.Nav
import com.youth.app.feature.home.R
import com.youthtalk.component.PolicyCard
import com.youthtalk.component.PolicyCheckBox
import com.youthtalk.component.PopularCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navController: NavController, homeLazyListScrollState: LazyListState) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState !is HomeUiState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        HomeMain(
            uiState = uiState as HomeUiState.Success,
            homeLazyListScrollState = homeLazyListScrollState,
            onCheck = viewModel::changeCategoryCheck,
            onClickDetailPolicy = { policyId ->
                navController.navigate("${Nav.PolicyDetail.route}/$policyId")
            },
            onClickSpecPolicy = {
                navController.navigate(Nav.SpecPolicy.route)
            },
            onClickSearch = {
                navController.navigate(Nav.Search.route)
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeMain(
    uiState: HomeUiState.Success,
    homeLazyListScrollState: LazyListState,
    onCheck: (Category?) -> Unit,
    onClickDetailPolicy: (String) -> Unit,
    onClickSpecPolicy: () -> Unit,
    onClickSearch: () -> Unit,
) {
    val allPolicies = uiState.allPolicies.collectAsLazyPagingItems()
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
                    PopularPolicyScreen(
                        uiState.popularPolicies,
                        onClickDetailPolicy,
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
                ) { index ->
                    UpdatePolicyScreen(
                        modifier = Modifier.animateItemPlacement(),
                        allPolicies[index],
                        onClickDetailPolicy = onClickDetailPolicy,
                    )
                }
            }
        }
    }
}

@Composable
private fun PopularPolicyScreen(top5Policies: List<Policy>, onClickDetailPolicy: (String) -> Unit) {
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
                onClickDetailPolicy = onClickDetailPolicy,
            )
        }
    }
}

@Composable
private fun PopularTitle() {
    Box(modifier = Modifier.background(Color.White)) {
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
}

@Composable
private fun UpdatePolicyScreen(modifier: Modifier = Modifier, policy: Policy?, onClickDetailPolicy: (String) -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            .padding(horizontal = 17.dp),
    ) {
        policy?.let {
            PolicyCard(
                modifier = Modifier.padding(bottom = 12.dp),
                policy = policy,
                onClickDetailPolicy = onClickDetailPolicy,
            )
        }
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
private fun CategoryScreen(goSpecPolicyScreen: () -> Unit) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Category.entries.forEach { category ->
            CategoryCard(
                modifier = Modifier
                    .clickableSingle {
                        goSpecPolicyScreen()
                    },
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

@Preview
@Composable
private fun HomeScreenPreview() {
    YongProjectTheme {
        HomeScreen(
            navController = rememberNavController(),
            homeLazyListScrollState = rememberLazyListState(),
        )
    }
}
