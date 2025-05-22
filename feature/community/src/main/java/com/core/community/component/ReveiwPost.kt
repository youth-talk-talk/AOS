package com.core.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.paging.compose.itemKey
import com.youthtalk.component.card.PostCard
import com.youthtalk.component.chip.RoundChip
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.extentions.shadow
import com.youthtalk.model.post.Post
import com.youthtalk.model.typeenum.Category

@Composable
fun ReviewPost(
    modifier: Modifier = Modifier,
    popularReviews: List<Post>,
    reviews: LazyPagingItems<Post>,
    lazyListState: LazyListState,
    category: Category,
    onClickPost: (Long) -> Unit,
    onClickCategory: (Category) -> Unit,
    onClickPostScrap: (Long, Boolean) -> Unit
) {
    val categories = Category.entries.toList()
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
                    text = "\uD83D\uDD25 인기 후기 게시물",
                    style = MaterialTheme.typography.titleLarge
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(
                        count = popularReviews.size
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
                            post = popularReviews[it],
                            onClick = { onClickPost(popularReviews[it].postId) },
                            onClickPostScrap = onClickPostScrap
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

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    count = categories.size
                ) {
                    RoundChip(
                        text = categories[it].categoryName,
                        isSelected = categories[it] == category,
                        onClick = { onClickCategory(categories[it]) }
                    )
                }
            }
        }

        if (reviews.loadState.refresh is LoadState.NotLoading) {
            items(
                count = reviews.itemCount,
                key = reviews.itemKey { it.postId }
            ) {
                reviews[it]?.let { post ->
                    PostCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        post = post,
                        onClick = { onClickPost(post.postId) },
                        onClickScrap = onClickPostScrap
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                        color = gray40
                    )
                }
            }
        }

        if (reviews.loadState.refresh is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
