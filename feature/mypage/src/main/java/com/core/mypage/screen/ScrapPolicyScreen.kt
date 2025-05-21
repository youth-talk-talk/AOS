package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.core.mypage.model.scrappolicy.ScrapPolicyUiEffect
import com.core.mypage.model.scrappolicy.ScrapPolicyUiEvent
import com.core.mypage.viewmodel.ScrapPolicyViewModel
import com.youth.app.feature.mypage.R
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.extentions.rememberLazyListState
import com.youthtalk.model.policy.Policy
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ScrapPolicyScreenRoot(viewModel: ScrapPolicyViewModel = hiltViewModel(), onBack: () -> Unit, onClickPolicyDetail: (Long) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val policies = state.policies.collectAsLazyPagingItems()
    val lazyListState = policies.rememberLazyListState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is ScrapPolicyUiEffect.ClickPolicy -> onClickPolicyDetail(it.policyId)
            }
        }
    }

    if (!state.isLoading) {
        ScrapPolicyScreen(
            policies = policies,
            lazyListState = lazyListState,
            actionEvent = viewModel::setEvent,
            onBack = onBack
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ScrapPolicyScreen(
    modifier: Modifier = Modifier,
    policies: LazyPagingItems<Policy>,
    lazyListState: LazyListState,
    actionEvent: (ScrapPolicyUiEvent) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        NoBackMiddleTitleTopBar(
            title = stringResource(R.string.scrap_policy_topbar_title),
            tails = {
                Image(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onBack()
                        },
                    painter = painterResource(R.drawable.close),
                    contentDescription = stringResource(R.string.close)
                )
            }
        )

        if (policies.itemCount == 0 && policies.loadState.refresh is LoadState.NotLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(2f))
                EmptyScreen(
                    modifier = Modifier.weight(5f),
                    emptyTitle = "아직 스크랩한 정책이 없습니다."
                )
            }
        }

        if (policies.itemCount != 0 && policies.loadState.refresh is LoadState.NotLoading) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = policies.itemCount,
                    key = policies.itemKey()
                ) {
                    policies[it]?.let { policy ->
                        PolicyCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = gray10,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = gray40,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            policy = policy,
                            onClick = { actionEvent(ScrapPolicyUiEvent.OnClickPolicy(policyId = policy.policyId)) },
                            onClickScrap = { id, scrap -> actionEvent(ScrapPolicyUiEvent.PostScrap(id, scrap)) }
                        )
                    }
                }
            }
        }

        if (policies.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun ScrapPolicyScreenPreview() {
    YongProjectTheme {
        ScrapPolicyScreen(
            policies = emptyFlow<PagingData<Policy>>().collectAsLazyPagingItems(),
            lazyListState = rememberLazyListState(),
            actionEvent = {},
            onBack = {}
        )
    }
}
