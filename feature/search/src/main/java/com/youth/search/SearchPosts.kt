package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.youthtalk.component.PostCard
import com.youthtalk.model.Post
import com.youthtalk.util.clickableSingle

@Composable
fun SearchPosts(
    posts: LazyPagingItems<Post>,
    count: Int,
    type: String,
    map: Map<Long, Boolean>,
    onClickDetailPost: (Long) -> Unit,
    onClickScrap: (Long, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),

    ) {
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(12.dp),
            )
            SpecPolicyInfo(
                count = count,
                title = if (type == "post") "게시글" else "후기",
            )
        }

        items(
            count = posts.itemCount,
            key = { index -> posts.peek(index)?.postId ?: 0 },
        ) { index ->
            posts[index]?.let { post ->
                val newPost = if (map.containsKey(post.postId)) {
                    post.copy(
                        scrap = map[post.postId] ?: false,
                        scraps = if (map[post.postId] == true) {
                            post.scraps + 1
                        } else {
                            post.scraps - 1
                        },
                    )
                } else {
                    post
                }

                PostCard(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp)
                        .clickableSingle { onClickDetailPost(newPost.postId) },
                    policyTitle = newPost.policyTitle,
                    title = newPost.title,
                    scraps = newPost.scraps,
                    comments = newPost.comments,
                    scrap = newPost.scrap,
                    onClickScrap = { scrap -> onClickScrap(newPost.postId, scrap) },
                )
            }
        }
    }
}
