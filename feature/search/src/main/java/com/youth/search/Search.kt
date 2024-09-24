package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youth.search.model.SearchState

@Composable
fun Search(
    type: String,
    recentlyList: List<String>,
    onClickRecently: (String) -> Unit,
    onDeleteRecently: (String) -> Unit,
    onClickDetailPolicy: (String) -> Unit,
    onClickDetailPost: (Long) -> Unit,
    onBack: () -> Unit,
) {
    val ime = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    var text by rememberSaveable { mutableStateOf("") }
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondaryContainer),
    ) {
        SearchBar(
            text = text,
            onChangeText = { text = it },
            searchDone = {
                if (text.isNotEmpty()) {
                    navController.navigate(SearchState.SEARCH.name) {
                        restoreState = true
                        launchSingleTop = true
                    }
                    onClickRecently(text)
                }
                ime?.hide()
                focus.clearFocus()
            },
            onBack = { if (navController.currentDestination?.route == SearchState.NONE.name) onBack() else navController.popBackStack() },
            initText = { text = "" },
        )
        NavHost(
            navController = navController,
            startDestination = SearchState.NONE.name,
        ) {
            composable(
                route = SearchState.NONE.name,
            ) {
                RecentlySearchScreen(
                    recentlyList = recentlyList,
                    searchClick = { search ->
                        navController.navigate(SearchState.SEARCH.name) {
                            restoreState = true
                            launchSingleTop = true
                        }
                        text = search
                        onClickRecently(search)
                        ime?.hide()
                    },
                    onDeleteRecently = { onDeleteRecently(it) },
                )
            }

            composable(
                route = SearchState.SEARCH.name,
            ) {
                SearchResultScreen(
                    search = text,
                    type = type,
                    onClickDetailPolicy = onClickDetailPolicy,
                    onClickDetailPost = onClickDetailPost,
                )
            }
        }
    }
}
