package com.youthtalk.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import com.youthtalk.util.getTime
import java.time.LocalDateTime

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: Post,
    isVisiblePolicyTitle: Boolean = false,
    onClick: () -> Unit,
    onClickScrap: (Long, Boolean) -> Unit
) {
    val dateTime by remember {
        mutableStateOf(post.createdAt.getTime())
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = gray10)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        post.category?.let { keyword ->
            KeywordTag(text = keyword.categoryName)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
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
        }

        if (isVisiblePolicyTitle) {
            post.policyTitle?.let { title ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = gray50,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 11.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = gray90
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onClickScrap(post.postId, post.scrap)
                            },
                        painter = painterResource(if (post.scrap) R.drawable.bookmark_fill else R.drawable.bookmark_line),
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
                text = dateTime,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = gray80
                )
            )
        }
    }
}

@Preview
@Composable
private fun PostCardPreview() {
    YongProjectTheme {
        PostCard(
            post = Post(
                postId = 0,
                title = "",
                writerId = 0,
                policyId = null,
                policyTitle = null,
                comments = 0,
                contentPreview = "",
                scrapCount = 0,
                scrap = false,
                category = Category.JOB,
                createdAt = LocalDateTime.now(),
                postType = PostType.COMMUNITY_TAB_FREE
            ),
            onClick = {},
            onClickScrap = { _, _ -> }
        )
    }
}

@Preview
@Composable
private fun PostCardWithTagPreview() {
    YongProjectTheme {
        PostCard(
            post = Post(
                postId = 0,
                title = "",
                writerId = 0,
                policyId = null,
                policyTitle = null,
                comments = 0,
                contentPreview = "",
                scrapCount = 0,
                scrap = false,
                createdAt = LocalDateTime.now(),
                category = Category.JOB,
                postType = PostType.COMMUNITY_TAB_FREE
            ),
            onClick = {},
            onClickScrap = { _, _ -> }
        )
    }
}
