package com.core.home.screen

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.home.model.newpolicy.NewPolicyUiEffect
import com.core.home.model.newpolicy.NewPolicyUiEvent
import com.core.home.viewmodel.NewPolicyViewModel
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.chip.RoundChip
import com.youthtalk.component.dropdown.SortTypeDropDown
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.typeenum.Category
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NewPolicyScreen(
    modifier: Modifier = Modifier,
    viewModel: NewPolicyViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onClickPolicyDetail: (Long) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val categories = Category.entries.toList()
    var index by rememberSaveable {
        mutableStateOf(0)
    }
    val items = when (categories[index]) {
        Category.ALL -> uiState.newPolicies.all
        Category.DWELLING -> uiState.newPolicies.dwelling
        Category.EDUCATION -> uiState.newPolicies.education
        Category.JOB -> uiState.newPolicies.job
        Category.LIFE -> uiState.newPolicies.life
        Category.PARTICIPATION -> uiState.newPolicies.participation
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is NewPolicyUiEffect.ClickPolicy -> onClickPolicyDetail(it.policyId)
            }
        }
    }

    var isRefresh by rememberSaveable {
        mutableStateOf(false)
    }
    LifecycleResumeEffect(Unit) {
        if (isRefresh) {
            viewModel.setEvent(NewPolicyUiEvent.Refresh)
            isRefresh = false
        }
        onPauseOrDispose { isRefresh = true }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "최근 올라온 정책",
            onBack = onBack
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                count = categories.size
            ) {
                val title = categories[it].categoryName.split(" ").first()
                RoundChip(
                    text = title,
                    isSelected = it == index,
                    onClick = { index = it }
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 14.dp, bottom = 10.dp),
            color = gray30
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "총 ${DecimalFormat("#,###").format(items.size)}건",
                        style = MaterialTheme.typography.displayMedium
                    )
                    SortTypeDropDown(
                        sortType = uiState.sortType,
                        onClickSort = {
                            viewModel.setEvent(NewPolicyUiEvent.GetNewPolices(it))
                        }
                    )
                }
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(0.8f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                if (items.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize()
                        ) {
                            Spacer(modifier = Modifier.weight(2f))
                            EmptyScreen(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(5f),
                                emptyTitle = "올라온 공고가 없어요."
                            )
                        }
                    }
                } else {
                    items(
                        count = items.size
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
                            policy = items[it],
                            onClick = { viewModel.setEvent(NewPolicyUiEvent.OnClickPolicy(items[it].policyId)) },
                            onClickScrap = { id, scrap -> viewModel.setEvent(NewPolicyUiEvent.OnClickPolicyScrap(id, scrap)) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NewPolicyScreenPreview() {
    YongProjectTheme {
        NewPolicyScreen(
            onBack = {},
            onClickPolicyDetail = {}
        )
    }
}
