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
import com.youthtalk.model.PostType
import com.youthtalk.model.ReviewPost
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PopularPosts(popularReviewPosts: ImmutableList<ReviewPost>, onClickItem: (Long) -> Unit, postPostScrap: (Long, Boolean, PostType) -> Unit) {
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
            PostCard(
                modifier = Modifier
                    .aspectRatio(2.5f)
                    .clickableSingle {
                        onClickItem(reviewPost.postId)
                    },
                policyTitle = reviewPost.policyTitle,
                title = reviewPost.title,
                comments = reviewPost.comments,
                scraps = reviewPost.scraps,
                scrap = reviewPost.scrap,
                isSingleLine = true,
                onClickScrap = { postPostScrap(reviewPost.postId, it, PostType.REVIEW) },
            )
        }
    }
}
