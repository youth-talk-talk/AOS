package com.example.policydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.policydetail.component.Comment
import com.example.policydetail.component.PolicyDetail
import com.example.policydetail.component.PolicyDetailTopAppBar
import com.example.policydetail.component.PolicyTitle
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50

@Composable
fun PolicyDetailScreen(policyId: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        PolicyDetailTopAppBar()

        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            item {
                PolicyTitle(
                    modifier = Modifier
                        .padding(start = 17.dp, end = 22.dp, top = 4.dp),
                )
            }
            item {
                PolicyDetail(
                    modifier = Modifier
                        .padding(top = 29.dp)
                        .padding(horizontal = 17.dp),
                )
            }

            item {
                RoundButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 17.dp,
                            vertical = 12.dp,
                        ),
                    text = "지원하기",
                    color = MaterialTheme.colorScheme.primary,
                    onClick = {},
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(start = 17.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "댓글",
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = "7",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = gray50,
                        ),
                    )
                }
            }

            items(
                count = 7,
            ) { index: Int ->
                CommentScreen(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp)
                        .shadow(3.dp),
                    isMine = (index + 1) % 6 == 0,
                )
            }
        }

        Comment(
            modifier = Modifier,
        )
    }
}

@Preview
@Composable
private fun PolicyDetailScreenPreview() {
    YongProjectTheme {
        PolicyDetailScreen(
            policyId = "1234",
        )
    }
}
