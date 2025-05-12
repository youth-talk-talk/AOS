package com.core.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.card.PostCard
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.extentions.shadow

@Composable
fun FreePost(modifier: Modifier = Modifier, lazyListState: LazyListState, onClickPost: () -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = "\uD83D\uDD25 인기 자유 게시물",
                    style = MaterialTheme.typography.titleLarge
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(
                        count = 5
                    ) {
                        PopularPostCard(
                            modifier = Modifier
                                .width(253.dp)
                                .shadow(
                                    offsetX = 4.dp,
                                    offsetY = 4.dp,
                                    blurRadius = 12.dp
                                )
                                .background(
                                    color = gray10,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            communityTitle = "면접 정장 비싸서 걱정했는데 공짜로 해결함!",
                            content = "면접 정장 비싸서 걱정했는데 공짜로 해결함!.....",
                            onClick = onClickPost
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                thickness = 10.dp,
                color = gray30
            )
        }

        items(
            count = 10
        ) {
            PostCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                communityTitle = "영화 보는 거 좋아하는 사람? 꿀팁 알려드림!",
                communitySubTitle = "영화 보는 거 좋아하는 사람? 꿀팁 알려드림! 영화 보는 거 좋아하는 사람...",
                onClick = onClickPost
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                color = gray40
            )
        }
    }
}
