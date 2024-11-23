package com.core.community.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.core.community.component.SearchBarComponent
import com.youth.app.feature.community.R
import com.youthtalk.model.Category
import com.youthtalk.model.Post
import com.youthtalk.model.PostType
import com.youthtalk.model.ReviewPost
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList

@Composable
fun Community(
    tabIndex: Int,
    lazyListState: LazyListState,
    reviewPosts: LazyPagingItems<ReviewPost>,
    posts: LazyPagingItems<Post>,
    categories: ImmutableList<Category>,
    popularReviewPosts: ImmutableList<ReviewPost>,
    popularPosts: ImmutableList<Post>,
    changeReviewCheckBox: (Category?) -> Unit,
    onClickItem: (Long) -> Unit,
    onClickTab: (Int) -> Unit,
    onClickSearch: (String) -> Unit,
    postPostScrap: (Long, Boolean, PostType) -> Unit,
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondaryContainer),
        contentPadding = PaddingValues(bottom = 12.dp),
    ) {
        item {
            CommunityTab(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                tabIndex,
                stringArrayResource(id = R.array.tabs).toList(),
                onTabClick = onClickTab,
            )
        }

        item {
            SearchBarComponent(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 17.dp)
                    .padding(top = 24.dp)
                    .clickableSingle {
                        val type = when (tabIndex) {
                            0 -> "review"
                            else -> "post"
                        }
                        onClickSearch(type)
                    },
            )
        }

        when (tabIndex) {
            0 -> reviewPost(
                reviewCategories = categories,
                popularReviewPosts = popularReviewPosts,
                reviewPosts = reviewPosts,
                onCheck = { category ->
                    changeReviewCheckBox(category)
                },
                onClickItem = { postId -> onClickItem(postId) },
                postPostScrap = postPostScrap,
            )

            1 -> freeBoard(
                popularPosts = popularPosts,
                posts = posts,
                onClickItem = { postId -> onClickItem(postId) },
                postPostScrap = postPostScrap,
            )
        }
    }
}
