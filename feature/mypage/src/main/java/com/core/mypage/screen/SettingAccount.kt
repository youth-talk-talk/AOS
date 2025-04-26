package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.mypage.R
import com.youthtalk.component.dropdown.RegionDropDown
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90

@Composable
fun SettingAccount(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        MiddleTitleTopBar(
            title = "내 계정",
            onBack = {},
            tails = {
                Text(
                    text = "저장",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80,
                    ),
                )
            },
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
                // TODO: 이미지로 바꾸기
                Box(
                    modifier = Modifier
                        .size(94.dp)
                        .clip(CircleShape)
                        .background(gray50)
                        .align(Alignment.Center),
                )

                Image(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "카메라",
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
                text = "닉네임",
                style = MaterialTheme.typography.displayMedium.copy(
                    color = gray90,
                ),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 46.dp)
                    .border(
                        width = 1.dp,
                        color = gray50,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "울적한 쿠키(닉네임)",
                    style = MaterialTheme.typography.displayMedium,
                )
            }

            Text(
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
                text = "연동된 계정",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = gray90,
                ),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 46.dp)
                    .border(
                        width = 1.dp,
                        color = gray50,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(R.drawable.kakao),
                    contentDescription = "카카오",
                )

                Text(
                    text = "abcd@kakao.com",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Text(
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
                text = "관심지역",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = gray90,
                ),
            )

            RegionDropDown(
                hint = stringResource(R.string.account_region_hint),
                onSelect = {},
            )

            HorizontalDivider(
                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp),
                thickness = 1.dp,
                color = gray40,
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "로그아웃",
                style = MaterialTheme.typography.displayMedium.copy(
                    color = gray70,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun SettingAccountPreview() {
    YongProjectTheme {
        SettingAccount()
    }
}
