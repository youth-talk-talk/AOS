package com.youthtalk.component.card

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.extentions.shadow

@Composable
fun BestCard(modifier: Modifier = Modifier, communityCategory: String, communityTitle: String, communitySubTitle: String, policyTitle: String = "") {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                offsetX = 4.dp,
                offsetY = 4.dp,
                blurRadius = 12.dp,
            )
            .background(
                color = gray10,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = communityCategory,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray80,
                    ),
                )

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
private fun BestCardPreview() {
    YongProjectTheme {
        BestCard(
            communityCategory = "자유게시판",
            communityTitle = "면접 정장 비싸서 걱정했는데 공짜로 해결함!",
            communitySubTitle = "면접 정장 비싸서 걱정했는데 공짜로 해결함!.....",
        )
    }
}
