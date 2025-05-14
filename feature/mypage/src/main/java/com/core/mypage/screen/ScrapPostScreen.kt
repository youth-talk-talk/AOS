package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.navigation.model.ScrapPostType
import com.youth.app.feature.mypage.R
import com.youthtalk.component.card.PostCard
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import java.time.LocalDateTime

@Composable
fun ScrapPostScreen(modifier: Modifier = Modifier, type: ScrapPostType) {
    var size by remember {
        mutableStateOf(15)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        NoBackMiddleTitleTopBar(
            title = when (type) {
                ScrapPostType.MY -> "작성한 글"
                ScrapPostType.SCRAP -> "스크랩한 게시글"
            },
            tails = {
                Image(
                    painter = painterResource(R.drawable.close),
                    contentDescription = stringResource(R.string.close)
                )
            }
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 14.dp),
                    text = when (type) {
                        ScrapPostType.MY -> "작성글 10"
                        ScrapPostType.SCRAP -> "게시글 10"
                    },
                    style = MaterialTheme.typography.labelMedium
                )
            }

            items(size) {
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
                    onClick = {}
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 20.dp),
                    color = gray40,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Preview
@Composable
private fun ScrapPostScreenPreview() {
    YongProjectTheme {
        ScrapPostScreen(
            type = ScrapPostType.SCRAP
        )
    }
}
