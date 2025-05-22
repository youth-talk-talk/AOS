package com.core.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.home.model.popular.PopularPolicyUiEffect
import com.core.home.model.popular.PopularPolicyUiEvent
import com.core.home.viewmodel.PopularPolicyViewModel
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PopularPolicyScreen(
    modifier: Modifier = Modifier,
    viewModel: PopularPolicyViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onClickPolicyDetail: (Long) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is PopularPolicyUiEffect.ClickPolicy -> onClickPolicyDetail(it.postId)
            }
        }
    }

    var isRefresh by rememberSaveable {
        mutableStateOf(false)
    }
    LifecycleResumeEffect(Unit) {
        if (isRefresh) {
            viewModel.setEvent(PopularPolicyUiEvent.Refresh)
            isRefresh = false
        }
        onPauseOrDispose { isRefresh = true }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "우리지역 인기 정책",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = uiState.policies.size
            ) {
                val policy = uiState.policies[it]
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
                    onClick = { viewModel.setEvent(PopularPolicyUiEvent.OnClickPolicy(policy.policyId)) },
                    onClickScrap = { id, scrap -> viewModel.setEvent(PopularPolicyUiEvent.OnClickPolicyScrap(id, scrap)) }
                )
            }
        }
    }
}

@Preview
@Composable
fun PopularPolicyScreenPreview() {
    YongProjectTheme {
        PopularPolicyScreen(
            onBack = {},
            onClickPolicyDetail = {}
        )
    }
}
