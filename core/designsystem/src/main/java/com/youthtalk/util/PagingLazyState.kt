package com.youthtalk.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.launch

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListState(): LazyListState {
    val state = androidx.compose.foundation.lazy.rememberLazyListState()
    val scrollIndex = rememberSaveable { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    return when (itemCount) {
        0 -> rememberSaveable(saver = LazyListState.Saver) {
            scrollIndex.intValue = state.firstVisibleItemIndex
            LazyListState(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset)
        }
        else -> {
            if (scrollIndex.intValue in 1..<itemCount) {
                LaunchedEffect("") {
                    coroutineScope.launch {
                        state.animateScrollToItem(scrollIndex.intValue, state.firstVisibleItemScrollOffset)
                        scrollIndex.intValue = 0
                    }
                }
            }
            state
        }
    }
}
