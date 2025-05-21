package com.feature.policy.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feature.policy.model.recentlyview.RecentlyViewUiEffect
import com.feature.policy.model.recentlyview.RecentlyViewUiEvent
import com.feature.policy.viewmodel.RecentlyViewPolicyViewModel
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.policy.Policy
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecentlyViewPolicyScreenRoot(viewModel: RecentlyViewPolicyViewModel = hiltViewModel(), onBack: () -> Unit, onClickPolicyDetail: (Long) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isRefresh by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is RecentlyViewUiEffect.OnPolicyDetail -> onClickPolicyDetail(it.policyId)
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        if (isRefresh) {
            viewModel.setEvent(RecentlyViewUiEvent.InitData)
            isRefresh = false
        }
        onPauseOrDispose {
            isRefresh = true
        }
    }

    if (!state.isLoading) {
        RecentlyViewPolicyScreen(
            policies = state.policies,
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
fun RecentlyViewPolicyScreen(modifier: Modifier = Modifier, policies: List<Policy>, actionEvent: (RecentlyViewUiEvent) -> Unit, onBack: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "최근 본 정책",
            onBack = onBack,
            tails = {
                Text(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        actionEvent(RecentlyViewUiEvent.DeleteAll)
                    },
                    text = "전체 삭제",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80
                    )
                )
            }
        )

        if (policies.isEmpty()) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5f),
                emptyTitle = "최근 본 정책이 없어요"
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = policies.size,
                    key = { policies[it].policyId }
                ) {
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
                        policy = policies[it],
                        onClick = { actionEvent(RecentlyViewUiEvent.OnClickPolicy(policies[it].policyId)) },
                        onClickScrap = { id, scrap -> actionEvent(RecentlyViewUiEvent.PostScrapPolicy(id, scrap)) }
                    )
                }
            }
        }
    }
}
