package com.youthtalk.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.component.tag.KeywordTag
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray90

@Composable
fun PolicyCard(modifier: Modifier = Modifier, isBookMark: Boolean = false, isVisibleScrap: Boolean = false, onClick: () -> Unit = {}) {
    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            KeywordTag(
                text = "상시",
                textColor = MaterialTheme.colorScheme.errorContainer,
                backgroundColor = MaterialTheme.colorScheme.onError,
            )

            KeywordTag(
                text = "지역",
            )

            KeywordTag(
                text = "정책분야",
            )

            Spacer(Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(
                        if (isBookMark) R.drawable.bookmark_fill else R.drawable.bookmark_line,
                    ),
                    contentDescription = "북마크",
                )
                Text(
                    text = "162",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray90,
                    ),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // TODO: 추후 이미지 변경
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(color = gray30)
                    .border(
                        width = 1.dp,
                        color = gray40,
                        shape = CircleShape,
                    ),
            )

            Text(
                text = "대학(원)생 학자금 대출이자 지원(2024년 2학기)",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        if (isVisibleScrap) {
            Text(
                text = stringResource(R.string.policy_scrap_title, 162),
            )
        }
    }
}

@Preview
@Composable
private fun PolicyCardPreview() {
    YongProjectTheme {
        PolicyCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = gray10,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = gray40,
                    shape = RoundedCornerShape(12.dp),
                ),
        )
    }
}

@Preview
@Composable
private fun PolicyCardBookmarkTruePreview() {
    YongProjectTheme {
        PolicyCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = gray10,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = gray40,
                    shape = RoundedCornerShape(12.dp),
                ),
            isBookMark = true,
        )
    }
}
