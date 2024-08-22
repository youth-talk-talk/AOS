package com.example.policydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.policydetail.component.PolicyDetail
import com.example.policydetail.component.PolicyDetailTopAppBar
import com.example.policydetail.component.PolicyTitle
import com.example.policydetail.model.PolicyDetailUiState
import com.youth.app.feature.policydetail.R
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.CommentTextField
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50

@Composable
fun PolicyDetailScreen(policyId: String, viewModel: PolicyDetailViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = null) {
        viewModel.getData(policyId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState !is PolicyDetailUiState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        PolicyDetailSuccessScreen(
            uiState as PolicyDetailUiState.Success,
        )
    }
}

@Composable
private fun PolicyDetailSuccessScreen(uiState: PolicyDetailUiState.Success) {
    val policyDetail = uiState.policyDetail
    val comments = uiState.comments
    val user = uiState.myInfo

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
                    topTitle = policyDetail.hostDep,
                    mainTitle = policyDetail.title,
                    titleDescription = policyDetail.introduction,
                )
            }
            item {
                PolicyDetail(
                    modifier = Modifier
                        .padding(top = 29.dp)
                        .padding(horizontal = 17.dp),
                    policyDetail = policyDetail,
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
                    text = stringResource(id = R.string.apply_btn_name),
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
                        text = comments.size.toString(),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = gray50,
                        ),
                    )
                }
            }

            items(
                count = comments.size,
            ) { index: Int ->
                CommentScreen(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp)
                        .shadow(3.dp),
                    nickname = comments[index].nickname,
                    content = comments[index].content,
                    isMine = user.nickname == comments[index].nickname,
                )
            }
        }

        CommentTextField(
            modifier = Modifier
                .imePadding(),
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
