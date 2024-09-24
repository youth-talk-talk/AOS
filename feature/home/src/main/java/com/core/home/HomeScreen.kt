package com.core.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.home.model.HomeUiState
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onClickDetailPolicy: (String) -> Unit,
    onClickSpecPolicy: (Category) -> Unit,
    onClickSearch: () -> Unit,
    homeLazyListScrollState: LazyListState,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState !is HomeUiState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        val value = uiState as HomeUiState.Success
        val allPolicies = value.allPolicies.collectAsLazyPagingItems()

        LifecycleResumeEffect(key1 = Unit) {
            viewModel.onResume()
            onPauseOrDispose {}
        }
        HomeMain(
            uiState = value,
            allPolicies = allPolicies,
            homeLazyListScrollState = homeLazyListScrollState,
            onCheck = viewModel::changeCategoryCheck,
            onClickDetailPolicy = onClickDetailPolicy,
            onClickSpecPolicy = onClickSpecPolicy,
            onClickSearch = onClickSearch,
            onClickScrap = viewModel::postScrap,
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    YongProjectTheme {
        HomeScreen(
            homeLazyListScrollState = rememberLazyListState(),
            onClickSearch = {},
            onClickDetailPolicy = {},
            onClickSpecPolicy = {},
        )
    }
}
