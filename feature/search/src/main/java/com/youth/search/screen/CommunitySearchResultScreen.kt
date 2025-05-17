package com.youth.search.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.youthtalk.component.card.PostCard
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import java.text.DecimalFormat

@Composable
fun CommunitySearchResultScreen(modifier: Modifier = Modifier, communityType: PostSubject, posts: LazyPagingItems<Post>, totalCount: Int) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (totalCount != 0) {
            if (posts.loadState.refresh is LoadState.NotLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "총 ${DecimalFormat("#,###").format(totalCount)}건",
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                }

                items(
                    count = posts.itemCount
                ) {
                    posts[it]?.let { post ->
                        PostCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            post = post,
                            onClick = {}
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                            color = gray40
                        )
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        } else {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val title = when (communityType) {
                        PostSubject.REVIEW -> "후기게시글"
                        PostSubject.POST -> "자유게시글"
                    }
                    Spacer(modifier.weight(2f))
                    EmptyScreen(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f),
                        emptyTitle = "검색한 ${title}이 존재하지 않습니다."
                    )
                }
            }
        }
    }
}
