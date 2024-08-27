package com.core.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.core.mypage.component.DDayPolicy
import com.core.mypage.component.EtcTabComponent
import com.core.mypage.component.FavoritesTabComponent
import com.core.mypage.component.ProfileCard
import com.core.navigation.SettingNavigation
import com.youth.app.feature.mypage.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import com.youthtalk.util.clickableSingle

@Composable
fun MyPageScreen() {
    val navHost = rememberNavController()
    val nickname = "놀고픈 청년"
    NavHost(
        navController = navHost,
        startDestination = SettingNavigation.MyPageHome.route,
    ) {
        composable(SettingNavigation.MyPageHome.route) {
            MyPageHomeScreen(navHost)
        }

        composable(SettingNavigation.AccountManage.route) {
            AccountManageScreen(
                navHost = navHost,
            )
        }

        composable(SettingNavigation.NicknameSetting.route) {
            NicknameSettingScreen(
                nickname = nickname,
            )
        }

        composable(SettingNavigation.ScrapPolicy.route) {
            ScrapPolicyScreen()
        }

        composable(
            route = "${SettingNavigation.MyPagePost.route}/{me}",
            arguments = listOf(
                navArgument("me") {
                    type = NavType.BoolType
                },
            ),
        ) {
            val isMine = it.arguments?.getBoolean("me") ?: false
            MyPagePostScreen(isMine)
        }

        composable(
            route = "${SettingNavigation.MyPageComment.route}/{me}",
            arguments = listOf(
                navArgument("me") {
                    type = NavType.BoolType
                },
            ),
        ) {
            val isMine = it.arguments?.getBoolean("me") ?: false
            MyPageCommentScreen(isMine)
        }

        composable(SettingNavigation.Announce.route) {
            AnnouncementScreen()
        }
    }
}

@Composable
private fun MyPageHomeScreen(navHost: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column {
            ProfileCard(
                onClick = {
                    navHost.navigate(SettingNavigation.AccountManage.route)
                },
            )
            DDayPolicy(
                policies = listOf(),
            )
            FavoritesScreen(
                scrapPolicy = {
                    navHost.navigate(SettingNavigation.ScrapPolicy.route)
                },
                scrapPost = {
                    navHost.navigate("${SettingNavigation.MyPagePost.route}/${false}")
                },
                writePost = {
                    navHost.navigate("${SettingNavigation.MyPagePost.route}/${true}")
                },
                writeComment = {
                    navHost.navigate("${SettingNavigation.MyPageComment.route}/${true}")
                },
                likeComment = {
                    navHost.navigate("${SettingNavigation.MyPageComment.route}/${false}")
                },
                announce = {
                    navHost.navigate(SettingNavigation.Announce.route)
                },
            )
        }
    }
}

@Composable
private fun FavoritesScreen(
    scrapPolicy: () -> Unit,
    scrapPost: () -> Unit,
    writePost: () -> Unit,
    writeComment: () -> Unit,
    likeComment: () -> Unit,
    announce: () -> Unit,
) {
    val favorites = stringArrayResource(id = R.array.favorites)
    val etc = stringArrayResource(id = R.array.etc)
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        text = "즐겨찾기",
        style = MaterialTheme.typography.headlineSmall.copy(
            color = MaterialTheme.colorScheme.onSecondary,
        ),
    )
    favorites.forEachIndexed { index, s ->
        FavoritesTabComponent(
            modifier = Modifier
                .padding(start = 16.dp, end = 18.dp)
                .clickableSingle {
                    when (index) {
                        0 -> scrapPolicy()
                        1 -> scrapPost()
                        2 -> writePost()
                        3 -> writeComment()
                        4 -> likeComment()
                    }
                },
            title = s,
        )
    }
    etc.forEach {
        EtcTabComponent(
            modifier = Modifier
                .padding(horizontal = 17.dp)
                .clickableSingle {
                    if (it == "공지사항") {
                        announce()
                    }
                },
            title = it,
        )
    }
}

@Preview
@Composable
private fun MyPagePreview() {
    val policies = listOf(
        Policy(
            policyId = "R2023081716945",
            category = Category.JOB,
            title = "국민 취업지원 제도",
            deadlineStatus = "",
            hostDep = "국토교통부",
            scrap = false,
        ),
        Policy(
            policyId = "R2023081716946",
            category = Category.JOB,
            title = "국민 취업지원 제도",
            deadlineStatus = "",
            hostDep = "국토교통부",
            scrap = false,
        ),
        Policy(
            policyId = "R2023081716947",
            category = Category.JOB,
            title = "국민 취업지원 제도",
            deadlineStatus = "",
            hostDep = "국토교통부",
            scrap = false,
        ),
        Policy(
            policyId = "R2023081716948",
            category = Category.JOB,
            title = "국민 취업지원 제도",
            deadlineStatus = "",
            hostDep = "국토교통부",
            scrap = false,
        ),
    )
    YongProjectTheme {
        MyPageScreen()
    }
}
