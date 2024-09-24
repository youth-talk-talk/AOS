package com.core.community.screen.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.community.R
import com.youthtalk.component.PostCard
import com.youthtalk.model.Post
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PopularPosts(
    popularReviewPosts: ImmutableList<Post>,
    map: Map<Long, Boolean>,
    onClickItem: (Long) -> Unit,
    postPostScrap: (Long, Boolean) -> Unit,
) {
    Text(
        text = stringResource(id = R.string.popular_post),
        style = MaterialTheme.typography.headlineSmall.copy(
            color = MaterialTheme.colorScheme.onPrimary,
        ),
    )

    LazyRow(
        modifier = Modifier
            .aspectRatio(2.8f),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = popularReviewPosts.size,
        ) { index ->
            val reviewPost = popularReviewPosts[index]
            val post = reviewPost.copy(
                scrap = map.getOrDefault(reviewPost.postId, reviewPost.scrap),
                scraps = if (reviewPost.scrap == map.getOrDefault(reviewPost.postId, reviewPost.scrap)) {
                    reviewPost.scraps
                } else {
                    if (map.getOrDefault(reviewPost.postId, reviewPost.scrap)) reviewPost.scraps + 1 else reviewPost.scraps - 1
                },
            )
            PostCard(
                modifier = Modifier
                    .aspectRatio(2.5f)
                    .clickableSingle {
                        onClickItem(post.postId)
                    },
                policyTitle = post.policyTitle,
                title = post.title,
                comments = post.comments,
                scraps = post.scraps,
                scrap = post.scrap,
                isSingleLine = true,
                onClickScrap = { postPostScrap(post.postId, it) },
            )
        }
    }
}
