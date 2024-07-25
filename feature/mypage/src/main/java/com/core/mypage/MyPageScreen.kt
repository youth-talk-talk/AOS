package com.core.mypage

import androidx.compose.foundation.background
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
import com.core.mypage.component.DDayPolicy
import com.core.mypage.component.EtcTabComponent
import com.core.mypage.component.FavoritesTabComponent
import com.core.mypage.component.ProfileCard
import com.youth.app.feature.mypage.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy

@Composable
fun MyPageScreen(polices: List<Policy>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column {
            ProfileCard()
            DDayPolicy(
                policies = polices,
            )
            FavoritesScreen()
        }
    }
}

@Composable
private fun FavoritesScreen() {
    val favorites = stringArrayResource(id = R.array.favorites)
    val etc = stringArrayResource(id = R.array.etc)
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        text = "즐겨찾기",
        style = MaterialTheme.typography.headlineSmall.copy(
            color = MaterialTheme.colorScheme.onSecondary,
        ),
    )
    favorites.forEach {
        FavoritesTabComponent(
            modifier = Modifier.padding(start = 16.dp, end = 18.dp),
            title = it,
        )
    }
    etc.forEach {
        EtcTabComponent(
            modifier = Modifier.padding(horizontal = 17.dp),
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
            policies,
        )
    }
}
