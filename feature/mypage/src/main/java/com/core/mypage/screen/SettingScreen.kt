package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.mypage.component.ProfileCard
import com.core.mypage.model.CommunityModel
import com.core.mypage.model.ManageModel
import com.core.mypage.model.SettingModel
import com.youth.app.feature.mypage.R
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray90

@Composable
fun SettingScreen(modifier: Modifier = Modifier, onClickProfileCard: () -> Unit, onClickEtc: () -> Unit, onClickTerms: () -> Unit) {
    SettingMain(
        onClickProfileCard = onClickProfileCard,
        onClickEtc = onClickEtc,
        onClickTerms = onClickTerms,
    )
}

@Composable
fun SettingMain(modifier: Modifier = Modifier, onClickProfileCard: () -> Unit, onClickEtc: () -> Unit, onClickTerms: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        MiddleTitleTopBar(
            title = stringResource(R.string.appbar),
            onBack = {},
        )
        ProfileCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            username = "울적한 쿠키",
            email = "abcd@kakao.com",
            onClick = onClickProfileCard,
        )

        ScrapPolicyAndNotification()

        HorizontalDivider(
            thickness = 10.dp,
            color = gray30,
        )

        SettingList<CommunityModel>(
            modifier = Modifier
                .padding(top = 30.dp, start = 15.dp, end = 17.dp, bottom = 20.dp),
            title = stringResource(R.string.community),
            contents = CommunityModel.getList(),
            onClick = {
                when (it) {
                    is CommunityModel.Comment -> {}
                    is CommunityModel.Like -> {}
                    is CommunityModel.Scrap -> {}
                    is CommunityModel.Write -> {}
                }
            },
        )

        HorizontalDivider(
            thickness = 10.dp,
            color = gray30,
        )

        SettingList<ManageModel>(
            modifier = Modifier
                .padding(top = 20.dp, start = 15.dp, end = 17.dp, bottom = 8.dp),
            title = stringResource(R.string.manage),
            contents = ManageModel.getList(),
            onClick = {
                when (it) {
                    is ManageModel.Policy -> onClickTerms()
                    is ManageModel.Inquire -> {}
                    is ManageModel.Etc -> onClickEtc()
                }
            },
        )
    }
}

@Composable
private fun <T : SettingModel> SettingList(modifier: Modifier = Modifier, title: String, contents: List<T>, onClick: (T) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            contents.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) {
                            onClick(it)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = it.getSettingName(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = gray90,
                        ),
                    )

                    Image(
                        painter = painterResource(R.drawable.arrowright),
                        contentDescription = stringResource(R.string.next),
                    )
                }
            }
        }
    }
}

@Composable
fun ScrapPolicyAndNotification(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.bookmark_line),
                contentDescription = stringResource(R.string.scrap_title),
            )
            Text(
                text = stringResource(R.string.scrap_title),
                style = MaterialTheme.typography.displayMedium,
            )
        }

        VerticalDivider(
            modifier = Modifier.height(52.dp),
            thickness = 1.dp,
            color = gray40,
        )

        Column(
            modifier = modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.notification),
                contentDescription = stringResource(R.string.notification),
            )
            Text(
                text = stringResource(R.string.notification),
                style = MaterialTheme.typography.displayMedium,
            )
        }
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    YongProjectTheme {
        SettingScreen(
            onClickProfileCard = {},
            onClickEtc = {},
            onClickTerms = {},
        )
    }
}
