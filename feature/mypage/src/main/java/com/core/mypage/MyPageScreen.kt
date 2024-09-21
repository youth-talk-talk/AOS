package com.core.mypage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.core.mypage.model.home.MyPageHomeUiState
import com.core.mypage.navigation.SettingNavigation
import com.core.mypage.viewmodel.MyPageHomeViewModel
import com.youth.app.feature.mypage.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import com.youthtalk.util.clickableSingle

@Composable
fun MyPageScreen(onClickDetailPolicy: (String) -> Unit, goLogin: () -> Unit, policyDetail: (String) -> Unit, postDetail: (Long) -> Unit) {
    val navHost = rememberNavController()
    val myPageHomeViewModel: MyPageHomeViewModel = hiltViewModel()
    NavHost(
        navController = navHost,
        startDestination = SettingNavigation.MyPageHome.route,
    ) {
        composable(SettingNavigation.MyPageHome.route) {
            MyPageHomeScreen(
                viewModel = myPageHomeViewModel,
                navHost = navHost,
                onClickDetailPolicy = onClickDetailPolicy,
            )
        }

        composable(SettingNavigation.AccountManage.route) {
            AccountManageScreen(
                viewModel = myPageHomeViewModel,
                navHost = navHost,
                goLogin = goLogin,
            )
        }

        composable(SettingNavigation.NicknameSetting.route) {
            NicknameSettingScreen(
                viewModel = myPageHomeViewModel,
                onBack = {
                    navHost.popBackStack()
                },
            )
        }

        composable(SettingNavigation.ScrapPolicy.route) {
            ScrapPolicyScreen(
                onBack = {
                    navHost.popBackStack()
                },
                policyDetail = policyDetail,
            )
        }

        composable(
            route = "${SettingNavigation.MyPagePost.route}/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                },
            ),
        ) {
            val type = it.arguments?.getString("type") ?: ""
            MyPagePostScreen(
                type = type,
                onBack = {
                    navHost.popBackStack()
                },
                postDetail = postDetail,
            )
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
            MyPageCommentScreen(
                isMine = isMine,
                onBack = { navHost.popBackStack() },
                policyDetail = policyDetail,
                postDetail = postDetail,
            )
        }

        composable(SettingNavigation.Announce.route) {
            AnnouncementScreen(
                onBack = {
                    navHost.popBackStack()
                },
                onClickAnnounceDetail = { navHost.navigate("${SettingNavigation.AnnounceDetail.route}/$it") },
            )
        }

        composable(
            "${SettingNavigation.AnnounceDetail.route}/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                },
            ),
        ) {
            val id = it.arguments?.getLong("id") ?: 0
            AnnouncementDetailScreen(
                id = id,
                onBack = {
                    navHost.popBackStack()
                },
            )
        }
    }
}

@Composable
private fun MyPageHomeScreen(navHost: NavHostController, viewModel: MyPageHomeViewModel = hiltViewModel(), onClickDetailPolicy: (String) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is MyPageHomeUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is MyPageHomeUiState.Success -> {
            MyPageHome(
                navHost = navHost,
                uiState = uiState as MyPageHomeUiState.Success,
                onClickDetailPolicy = onClickDetailPolicy,
            )
        }
    }
}

@Composable
private fun MyPageHome(navHost: NavHostController, uiState: MyPageHomeUiState.Success, onClickDetailPolicy: (String) -> Unit) {
    val user = uiState.user
    val deadLinePolicies = uiState.deadlinePolicies
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column {
            ProfileCard(
                username = user.nickname,
                onClick = {
                    navHost.navigate(SettingNavigation.AccountManage.route)
                },
            )
            DDayPolicy(
                policies = deadLinePolicies,
                onClickDetailPolicy = onClickDetailPolicy,
            )
            FavoritesScreen(
                scrapPolicy = {
                    navHost.navigate(SettingNavigation.ScrapPolicy.route)
                },
                scrapPost = {
                    navHost.navigate("${SettingNavigation.MyPagePost.route}/scrap")
                },
                writePost = {
                    navHost.navigate("${SettingNavigation.MyPagePost.route}/me")
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
                onClickQnA = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/s5skUrpg"))
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
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
    onClickQnA: () -> Unit,
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
                    } else {
                        onClickQnA()
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
        MyPageScreen(
            onClickDetailPolicy = {},
            goLogin = {},
            policyDetail = {},
            postDetail = {},
        )
    }
}
