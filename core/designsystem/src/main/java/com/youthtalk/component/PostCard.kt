package com.youthtalk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun PostCard(modifier: Modifier = Modifier, policyTitle: String?, title: String, scraps: Int, comments: Int, scrap: Boolean) {
    val bookmark = if (scrap) painterResource(id = R.drawable.bookmark_check) else painterResource(id = R.drawable.bookmark)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 11.dp, vertical = 13.dp),
    ) {
        policyTitle?.let {
            Text(
                text = policyTitle,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                ),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomIcon(
                icon = {
                    Icon(
                        painter = bookmark,
                        contentDescription = "북마크",
                        tint = if (scrap) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                    )
                },
                count = "$scraps",
            )

            Spacer(modifier = Modifier.padding(start = 6.dp))

            BottomIcon(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.message_icon),
                        contentDescription = "메세지",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                count = "$comments",
            )
        }
    }
}

@Composable
private fun BottomIcon(icon: @Composable () -> Unit, count: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()

        Text(
            text = count,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }
}

@Preview
@Composable
private fun PostCardScrapTruePreview() {
    YongProjectTheme {
        PostCard(
            modifier = Modifier.aspectRatio(3.41f),
            policyTitle = "[대전] 미래두배 청년통장",
            title = "사용자 설정 타이틀 사용자 설정 타이틀 사용자 설정 타이틀",
            scraps = 123,
            comments = 123,
            scrap = true,
        )
    }
}

@Preview
@Composable
private fun PostCardScrapFalsePreview() {
    YongProjectTheme {
        PostCard(
            modifier = Modifier.aspectRatio(3.41f),
            policyTitle = "[대전] 미래두배 청년통장",
            title = "사용자 설정 타이틀 사용자 설정 타이틀 사용자 설정 타이틀",
            scraps = 123,
            comments = 123,
            scrap = false,
        )
    }
}
