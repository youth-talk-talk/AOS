package com.core.community.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youth.app.feature.community.R
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.CommentTextField
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Comment
import com.youthtalk.model.Post

@Composable
fun CommunityDetailScreen(postId: Long, type: String) {
    val post = Post(
        postId = 0,
        title = "",
        content = "",
        writerId = null,
        scraps = 0,
        scrap = false,
        comments = 0,
        policyId = null,
        policyTitle = "일자리",
    )

    val comments = getComments()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CommunityDetailAppBar(
            policyTitle = post.policyTitle,
        )
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 17.dp, vertical = 15.dp),
        ) {
            item {
                CommunityTitle(
                    policyTitle = "[미래두배 청년통장",
                )
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(Color.Gray),
                ) {
                    Text(text = "텍스트 쪽 추후 구현")
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "댓글",
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = "7",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
            }

            items(
                count = comments.size,
            ) { index ->
                val comment = comments[index]
                CommentScreen(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .shadow(
                            elevation = 3.dp,
                        ),
                    nickname = comment.nickname,
                    content = comment.content,
                    isLike = comment.isLikeByMember,
                    isMine = false,
                )
            }
        }

        CommentTextField(
            modifier = Modifier.imePadding(),
        )
    }
}

private fun getComments() = listOf(
    Comment(
        commentId = 142,
        nickname = "집가고싶다어",
        content = "댓글내용수정1",
        isLikeByMember = false,
    ),
    Comment(
        commentId = 144,
        nickname = "집가고싶다어",
        content = "댓글내용수정3",
        isLikeByMember = false,
    ),
    Comment(
        commentId = 382,
        nickname = "집가고싶다어",
        content = "댓글내용수정2",
        isLikeByMember = false,
    ),
)

@Composable
fun CommunityTitle(modifier: Modifier = Modifier, policyTitle: String?) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = "씩씩한 청년",
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )

        Text(
            text = "사용자 설정 타이틀 사용자 설정 타이틀",
            style = MaterialTheme.typography.bodyMedium,
        )

        policyTitle?.let { policyTitle ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "정책명",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
                )
                Text(
                    text = policyTitle,
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onTertiary,
                    ),
                )
            }
        }
    }
}

@Composable
fun CommunityDetailAppBar(modifier: Modifier = Modifier, policyTitle: String?) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .padding(
                horizontal = 17.dp,
                vertical = 13.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.left_icon),
                contentDescription = "뒤로가기",
                tint = Color.Black,
            )
            policyTitle?.let { policyTitle ->
                Text(
                    text = policyTitle,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.share_icon),
                contentDescription = "공유",
                tint = Color.Black,
            )

            Icon(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "북마크",
                tint = Color.Black,
            )

            Icon(
                painter = painterResource(id = R.drawable.more_icon),
                contentDescription = "더보기",
                tint = Color.Black,
            )
        }
    }
}

@Preview
@Composable
private fun CommunityDetailScreenPreview() {
    YongProjectTheme {
        CommunityDetailScreen(
            postId = 0,
            type = "REVIEW",
        )
    }
}
