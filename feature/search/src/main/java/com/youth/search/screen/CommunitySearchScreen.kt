package com.youth.search.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.youth.search.component.SearchBar
import com.youth.search.model.SearchState
import com.youthtalk.designsystem.gray10
import com.youthtalk.model.CommunityType

@Composable
fun CommunitySearchScreen(modifier: Modifier = Modifier, onBack: () -> Unit, communityType: CommunityType) {
    var search by remember {
        mutableStateOf("")
    }
    var recents by remember {
        mutableStateOf(listOf("월제 지원", "서울시 청년보조", "학자금 대출 이자"))
    }
    var state by remember {
        mutableStateOf(SearchState.NONE)
    }

    BackHandler {
        when (state) {
            SearchState.NONE -> onBack()
            SearchState.SEARCH -> state = SearchState.NONE
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = gray10)
    ) {
        SearchBar(
            text = search,
            onClickBack = {},
            onTextChange = { search = it },
            onSearch = {
                state = SearchState.SEARCH
            },
            onClear = {
                search = ""
            }
        )

        Crossfade(
            targetState = state
        ) {
            when (it) {
                SearchState.NONE -> {
                    Column {
                        SearchScreen(
                            recents = recents,
                            onDelete = {
                                val list = recents.toMutableList()
                                list.remove(it)
                                recents = list
                            },
                            onDeleteAll = {
                                recents = mutableListOf()
                            },
                            onClickItem = {
                                state = SearchState.SEARCH
                            }
                        )
                    }
                }

                SearchState.SEARCH -> {
                    CommunitySearchResultScreen(
                        communityType = communityType
                    )
                }
            }
        }
    }
}
