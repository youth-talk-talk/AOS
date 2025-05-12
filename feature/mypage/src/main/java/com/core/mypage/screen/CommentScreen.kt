package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.core.navigation.model.CommentType
import com.youth.app.feature.mypage.R
import com.youthtalk.component.card.CommentCard
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray40

@Composable
fun CommentScreen(modifier: Modifier = Modifier, type: CommentType) {
    var size by remember {
        mutableStateOf(15)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        NoBackMiddleTitleTopBar(
            title = when (type) {
                CommentType.MY -> "내 댓글"
                CommentType.LIKE -> "좋아요한 댓글"
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
                    text = "댓글 10",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            items(size) {
                CommentCard(
                    isMine = type == CommentType.MY
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = gray40,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommentScreenMyPreview() {
    YongProjectTheme {
        CommentScreen(
            type = CommentType.MY
        )
    }
}

@Preview
@Composable
private fun CommentScreenLikePreview() {
    YongProjectTheme {
        CommentScreen(
            type = CommentType.LIKE
        )
    }
}
