package com.core.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.community.component.SearchBarComponent
import com.youth.app.feature.community.R
import com.youthtalk.component.PolicyCheckBox
import com.youthtalk.component.PostCard
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun CommunityScreen() {
    val tabNames = stringArrayResource(id = R.array.tabs)
    var tabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Surface {
        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                item {
                    CommunityTab(tabIndex, tabNames.toList()) { newIndex ->
                        tabIndex = newIndex
                    }
                }

                item {
                    SearchBarComponent(
                        modifier = Modifier
                            .padding(horizontal = 17.dp)
                            .padding(top = 24.dp),
                    )
                }

                when (tabIndex) {
                    0 -> reviewPost()
                    1 -> freeBoard()
                }
            }
            WriteButton()
        }
    }
}

@Composable
private fun BoxScope.WriteButton() {
    Row(
        modifier = Modifier.Companion
            .align(Alignment.BottomCenter)
            .padding(bottom = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50.dp),
            )
            .padding(
                horizontal = 17.dp,
                vertical = 13.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.write_icon),
            contentDescription = "글쓰기",
            tint = Color.Black,
        )
        Text(
            text = "글쓰기",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
private fun CommunityTab(tabIndex: Int, tabNames: List<String>, onTabClick: (Int) -> Unit) {
    TabRow(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 17.dp),
        selectedTabIndex = tabIndex,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabIndex])
                    .padding(vertical = 1.dp)
                    .offset(y = 0.5.dp)
                    .height(3.dp)
                    .background(Color.Black, shape = RoundedCornerShape(3.dp)),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        divider = {
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.onTertiary, shape = RoundedCornerShape(2.dp)),
                color = Color.Transparent,
            )
        },
    ) {
        tabNames.forEachIndexed { index, s ->
            Tab(
                selectedContentColor = MaterialTheme.colorScheme.background,
                selected = index == tabIndex,
                onClick = { onTabClick(index) },
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = s,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = if (index == tabIndex) Color.Black else MaterialTheme.colorScheme.onTertiary,
                    ),
                )
            }
        }
    }
}

private fun LazyListScope.reviewPost() {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 23.dp, end = 24.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            stringArrayResource(id = R.array.policies).forEach {
                PolicyCheckBox(
                    spaceBy = Arrangement.spacedBy(7.dp),
                    isCheck = true,
                    title = it,
                    textStyle = MaterialTheme.typography.displayLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                    ),
                    onCheckChange = {},
                )
            }
        }
    }

    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                )
                .padding(
                    top = 15.dp,
                    start = 17.dp,
                    end = 17.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.popular_post),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
            )

            LazyRow(
                modifier = Modifier
                    .aspectRatio(3.14f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    count = 5,
                ) {
                    PostCard(
                        modifier = Modifier.aspectRatio(3f),
                    )
                }
            }
        }
    }

    item {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                .padding(
                    top = 12.dp,
                    start = 17.dp,
                    end = 17.dp,
                ),
            text = stringResource(id = R.string.recent_post),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }

    items(
        count = 5,
    ) { index ->
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                .padding(horizontal = 17.dp)
                .padding(top = 12.dp, bottom = if (index == 4) 12.dp else 0.dp),

        ) {
            PostCard(
                modifier = Modifier.padding(),
            )
        }
    }
}

fun LazyListScope.freeBoard() {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                )
                .padding(
                    top = 15.dp,
                    start = 17.dp,
                    end = 17.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.popular_post),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
            )

            LazyRow(
                modifier = Modifier
                    .aspectRatio(3.14f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    count = 5,
                ) {
                    PostCard(
                        modifier = Modifier.aspectRatio(3f),
                    )
                }
            }
        }
    }

    item {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                .padding(
                    top = 12.dp,
                    start = 17.dp,
                    end = 17.dp,
                ),
            text = stringResource(id = R.string.recent_post),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }

    items(
        count = 5,
    ) { index ->
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                .padding(horizontal = 17.dp)
                .padding(top = 12.dp, bottom = if (index == 4) 12.dp else 0.dp),

        ) {
            PostCard(
                modifier = Modifier.padding(),
            )
        }
    }
}

@Preview
@Composable
private fun CommunityScreenPreview() {
    YongProjectTheme {
        CommunityScreen()
    }
}
