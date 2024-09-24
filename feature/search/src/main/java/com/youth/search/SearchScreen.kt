package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youth.search.model.SearchUiEvent
import com.youth.search.model.SearchUiState
import com.youth.search.viewmodel.SearchViewModel
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun SearchScreen(
    type: String,
    viewModel: SearchViewModel = hiltViewModel(),
    onClickDetailPolicy: (String) -> Unit,
    onClickDetailPost: (Long) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is SearchUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is SearchUiState.Success -> {
            val state = uiState as SearchUiState.Success

            Search(
                type = type,
                recentlyList = state.recentList,
                onClickRecently = { viewModel.uiEvent(SearchUiEvent.ClickRecently(it)) },
                onDeleteRecently = { viewModel.uiEvent(SearchUiEvent.DeleteRecently(it)) },
                onClickDetailPolicy = onClickDetailPolicy,
                onClickDetailPost = onClickDetailPost,
                onBack = onBack,
            )
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    YongProjectTheme {
        SearchScreen(
            type = "main",
            onClickDetailPolicy = {},
            onClickDetailPost = {},
            onBack = {},
        )
    }
}
