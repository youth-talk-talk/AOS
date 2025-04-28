package com.youthtalk.component.card

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.component.tag.KeywordTag
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90

@Composable
fun PostCard(modifier: Modifier = Modifier, keyword: String = "", communityTitle: String, communitySubTitle: String, policyTitle: String = "") {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (keyword.isNotEmpty()) {
            KeywordTag(text = keyword)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = communityTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = communitySubTitle,
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = gray80,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (policyTitle.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = gray50,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 11.dp),
                ) {
                    Text(
                        text = policyTitle,
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = gray90,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.comment),
                            contentDescription = stringResource(R.string.comment),
                            tint = gray80,
                        )

                        Text(
                            text = "숫자",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = gray80,
                            ),
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.bookmark_line),
                            contentDescription = stringResource(R.string.bookmark),
                            tint = gray80,
                        )

                        Text(
                            text = "숫자",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = gray80,
                            ),
                        )
                    }
                }

                Text(
                    text = "Date",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PostCardPreview() {
    YongProjectTheme {
        PostCard(
            communityTitle = "영화 보는 거 좋아하는 사람? 꿀팁 알려드림!",
            communitySubTitle = "영화 보는 거 좋아하는 사람? 꿀팁 알려드림!",
        )
    }
}
