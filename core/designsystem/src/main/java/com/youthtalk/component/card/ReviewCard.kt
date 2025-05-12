package com.youthtalk.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.policy.Review

@Composable
fun ReviewCard(modifier: Modifier = Modifier, review: Review, onClick: (Long) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = gray10)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(review.postId)
            },
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = review.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = review.contentPreview,
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = gray80
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.comment),
                        contentDescription = stringResource(R.string.comment),
                        tint = gray80
                    )

                    Text(
                        text = "${review.commentCount}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = gray80
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.bookmark_line),
                        contentDescription = stringResource(R.string.bookmark),
                        tint = gray80
                    )

                    Text(
                        text = "${review.scrapCount}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = gray80
                        )
                    )
                }
            }

            Text(
                text = "Date",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = gray80
                )
            )
        }
    }
}
