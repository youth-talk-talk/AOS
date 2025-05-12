package com.youthtalk.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray80
import com.youthtalk.extentions.shadow
import com.youthtalk.model.community.Post
import com.youthtalk.util.getTime
import java.time.LocalDateTime

@Composable
fun BestCard(modifier: Modifier = Modifier, post: Post, onClickPostDetail: (Long) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                offsetX = 4.dp,
                offsetY = 4.dp,
                blurRadius = 12.dp
            )
            .background(
                color = gray10,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClickPostDetail(post.postId)
            }
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = post.policyTitle?.let { "후기게시판" } ?: "자유게시판",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray80
                    )
                )

                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = post.contentPreview,
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = gray80
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
                            text = "${post.comments}",
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
                            painter = painterResource(
                                if (post.scrap) R.drawable.bookmark_fill else R.drawable.bookmark_line
                            ),
                            contentDescription = stringResource(R.string.bookmark),
                            tint = if (post.scrap) MaterialTheme.colorScheme.primary else gray80
                        )

                        Text(
                            text = "${post.scrapCount}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = gray80
                            )
                        )
                    }
                }

                Text(
                    text = post.createdAt.getTime(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80
                    )
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
            post = Post(
                postId = 0,
                title = "",
                writerId = 0,
                policyId = 0,
                policyTitle = "",
                comments = 0,
                contentPreview = "",
                scrapCount = 0,
                scrap = false,
                createdAt = LocalDateTime.now()
            ),
            onClickPostDetail = {}
        )
    }
}
