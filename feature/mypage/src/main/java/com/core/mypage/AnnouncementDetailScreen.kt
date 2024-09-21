package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.core.mypage.model.announcedetail.AnnounceDetailUiEvent
import com.core.mypage.model.announcedetail.AnnounceDetailUiState
import com.core.mypage.viewmodel.AnnouncementDetailViewModel
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.model.AnnounceDetail

@Composable
fun AnnouncementDetailScreen(id: Long, viewModel: AnnouncementDetailViewModel = hiltViewModel(), onBack: () -> Unit) {
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.uiEvent(AnnounceDetailUiEvent.GetAnnounceDetail(id))
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is AnnounceDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AnnounceDetailUiState.Success -> {
            val announceDetail = (state as AnnounceDetailUiState.Success).announceDetail

            AnnouncementDetail(
                announceDetail = announceDetail,
                onBack = onBack,
            )
        }
    }
}

@Composable
fun AnnouncementDetail(modifier: Modifier = Modifier, announceDetail: AnnounceDetail, onBack: () -> Unit) {
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
                .background(
                    MaterialTheme.colorScheme.background,
                ),
        ) {
            item {
                Text(
                    modifier = Modifier.padding(top = 13.dp, start = 17.dp),
                    text = announceDetail.title,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    modifier = Modifier.padding(horizontal = 17.dp, vertical = 12.dp),
                    text = announceDetail.content,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }

            items(
                count = announceDetail.imageList.size,
                key = { index -> announceDetail.imageList[index].id },
            ) { index ->
                AsyncImage(
                    modifier = Modifier.padding(horizontal = 17.dp).padding(bottom = 12.dp),
                    model = announceDetail.imageList[index].imgUrl,
                    contentDescription = "공지사항 이미지",
                )
            }
        }
    }
}
