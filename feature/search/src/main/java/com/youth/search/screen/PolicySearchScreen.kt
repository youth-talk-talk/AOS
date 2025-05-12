package com.youth.search.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youth.search.component.RecentText
import com.youth.search.component.SearchBar
import com.youth.search.model.SearchState
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray80

@Composable
fun PolicySearchScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
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
                    PolicySearchResultScreen()
                }
            }
        }
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    recents: List<String>,
    onDeleteAll: () -> Unit,
    onDelete: (String) -> Unit,
    onClickItem: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 20.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "최근 검색",
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDeleteAll()
                },
            text = "전체 삭제",
            style = MaterialTheme.typography.labelSmall.copy(
                color = gray80
            )
        )
    }

    if (recents.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            recents.forEach {
                RecentText(
                    text = it,
                    onClick = { onClickItem(it) },
                    onDelete = { onDelete(it) }
                )
            }
        }
    } else {
        EmptyScreen(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            emptyTitle = "최근 검색된 내역이 없습니다."
        )
    }
}
