package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.core.mypage.component.AnnouncementComponent
import com.core.mypage.model.announce.AnnounceUiState
import com.core.mypage.viewmodel.AnnouncementViewModel
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Announce

@Composable
fun AnnouncementScreen(viewModel: AnnouncementViewModel = hiltViewModel(), onBack: () -> Unit, onClickAnnounceDetail: (Long) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is AnnounceUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is AnnounceUiState.Success -> {
            val value = uiState as AnnounceUiState.Success
            Announcement(
                announces = value.announces.collectAsLazyPagingItems(),
                onBack = onBack,
                onClickAnnounceDetail = onClickAnnounceDetail,
            )
        }
    }
}

@Composable
private fun Announcement(announces: LazyPagingItems<Announce>, onBack: () -> Unit, onClickAnnounceDetail: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background,
            ),
    ) {
        MiddleTitleTopBar(
            title = "공지사항",
            onBack = onBack,
        )
        HorizontalDivider()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 17.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            items(
                count = announces.itemCount,
                key = announces.itemKey { it.id },
            ) { index ->
                announces[index]?.let { announce ->
                    val update = announce.updateAt.split("T").first().replace("-", "/")
                    AnnouncementComponent(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            onClickAnnounceDetail(announce.id)
                        },
                        date = update,
                        title = announce.title,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AnnouncementScreenPreview() {
    YongProjectTheme {
        AnnouncementScreen(
            onBack = {},
            onClickAnnounceDetail = {},
        )
    }
}
