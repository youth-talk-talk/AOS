package com.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.home.component.DropDownComponent
import com.youth.app.feature.home.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scrollState = rememberLazyListState()
    val topAppbarState =
        TopAppBarDefaults
            .enterAlwaysScrollBehavior(rememberTopAppBarState())

    val categoryList = getCategories()

    Surface {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .nestedScroll(topAppbarState.nestedScrollConnection),
        ) {
            HomeScreenAppBar(topAppbarState = topAppbarState)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
            ) {
                item {
                    DropDownComponent(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        dropDownClick = {},
                        dropDownList = listOf("1", "2", "3"),
                    )
                }

                item {
                    Row(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        categoryList.forEach { category ->
                            HomeScreenCategory(category = category)
                        }
                    }
                }

                items(count = 100) {
                    val r = if (it == 0) 20.dp else 0.dp
                    CardView(r = r, count = it)
                }
            }
        }
    }
}

@Composable
private fun CardView(r: Dp, count: Int) {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape =
                RoundedCornerShape(
                    topEnd = r,
                    topStart = r,
                ),
            )
            .padding(top = 16.dp, start = 8.dp, end = 8.dp),
    ) {
        Text(text = "HomeScreen $count")
    }
}

@Composable
private fun HomeScreenCategory(category: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = category.icon),
            contentDescription = category.description,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(text = category.description)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenAppBar(topAppbarState: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.top_name),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "검색", tint = Color.Black)
            }
        },
        scrollBehavior = topAppbarState,
        colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Composable
private fun getCategories(): List<Category> {
    val categoryList =
        listOf(
            Category(
                icon = R.drawable.recruit,
                description = "취업",
            ),
            Category(
                icon = R.drawable.room,
                description = "공간",
            ),
            Category(
                icon = R.drawable.teacher,
                description = "교육",
            ),
            Category(
                icon = R.drawable.book,
                description = "문화",
            ),
            Category(
                icon = R.drawable.participation,
                description = "취업",
            ),
        )
    return categoryList
}

@Preview
@Composable
private fun HomeScreenPreview() {
    YongProjectTheme {
        HomeScreen()
    }
}
