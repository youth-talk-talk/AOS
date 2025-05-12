package com.core.home.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.core.navigation.navigator.HomeTabNavigation
import com.youth.app.core.navigation.R
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray80

@Composable
fun BottomNavigation(modifier: Modifier = Modifier, route: HomeTabNavigation, onClickNavigation: (HomeTabNavigation) -> Unit) {
    val bottomNavigation =
        listOf(
            HomeTabNavigation.Home,
            HomeTabNavigation.Policy,
            HomeTabNavigation.Community,
            HomeTabNavigation.Setting
        )

    val scope = rememberCoroutineScope()

    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = gray40
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            bottomNavigation.forEach { currentRoute ->
                val color = if (currentRoute == route) MaterialTheme.colorScheme.primary else gray80
                val pair = when (currentRoute) {
                    HomeTabNavigation.Community -> Pair(R.drawable.community, "커뮤니티")
                    HomeTabNavigation.Home -> Pair(R.drawable.home, "홈")
                    HomeTabNavigation.Policy -> Pair(R.drawable.policy, "청년정책")
                    HomeTabNavigation.Setting -> Pair(R.drawable.person, "마이페이지")
                }
                BottomIcon(
                    title = pair.second,
                    icon = pair.first,
                    color = color,
                    scrollTop = {
//                    if (currentRoute == MainNav.Home.route) {
//                        scope.launch {
//                            homeLazyListScrollState.scrollToItem(0)
//                        }
//                    }
                    },
                    onClickTab = {
                        onClickNavigation(currentRoute)
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.BottomIcon(title: String, @DrawableRes icon: Int, color: Color, scrollTop: () -> Unit, onClickTab: () -> Unit) {
    Box(
        modifier =
        Modifier
            .weight(1f)
            .align(Alignment.CenterVertically)
            .padding(vertical = 7.dp)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                onClickTab()
//                    if (navHostController.currentDestination?.route != mainNav.route) {
//                        navHostController.navigate(mainNav.route) {
//                            popUpTo(navHostController.graph.id) {
//                                saveState = true
//                            }
//                            launchSingleTop = mainNav.route != MainNav.MyPage.route
//                            restoreState = mainNav.route != MainNav.MyPage.route
//                        }
//                    } else {
//                        scrollTop()
//                    }
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier =
                Modifier
                    .size(24.dp)
            ) {
                Image(
                    modifier =
                    Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    colorFilter = ColorFilter.tint(color = color)
                )
            }
            Text(
                text = title,
                style =
                MaterialTheme.typography.headlineMedium
                    .copy(color = color)
            )
        }
    }
}
