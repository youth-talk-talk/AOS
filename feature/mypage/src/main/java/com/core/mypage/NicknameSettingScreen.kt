package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.mypage.model.home.MyPageHomeUiEffect
import com.core.mypage.model.home.MyPageHomeUiEvent
import com.core.mypage.model.home.MyPageHomeUiState
import com.core.mypage.viewmodel.MyPageHomeViewModel
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NicknameSettingScreen(viewModel: MyPageHomeViewModel = hiltViewModel(), onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest {
            when {
                it is MyPageHomeUiEffect.GoBack -> onBack()
            }
        }
    }
    when (uiState) {
        is MyPageHomeUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is MyPageHomeUiState.Success -> {
            val state = (uiState as MyPageHomeUiState.Success)
            var name by remember {
                mutableStateOf(state.user.nickname)
            }
            NicknameSetting(
                name = name,
                onValueChange = {
                    if (it.length <= 8) {
                        name = it
                    }
                },
                onBack = onBack,
                onClickApply = { viewModel.uiEvent(MyPageHomeUiEvent.ChangeNickname(name)) },
            )
        }
    }
}

@Composable
private fun NicknameSetting(
    modifier: Modifier = Modifier,
    name: String,
    onValueChange: (String) -> Unit,
    onClickApply: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        MiddleTitleTopBar(
            title = "닉네임 설정",
            onBack = onBack,
        )
        HorizontalDivider()

        Text(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 17.dp),
            text = "닉네임 설정",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp),
            value = name,
            onValueChange = onValueChange,
            singleLine = true,
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = gray,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(13.dp),
            ) {
                innerTextField()
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        val isMatches = name.matches("""[가-힣][가-힣\s]*""".toRegex())
        RoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp, vertical = 12.dp),
            text = "적용하기",
            color = if (isMatches) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer,
            enabled = isMatches,
            onClick = onClickApply,
        )
    }
}

@Preview
@Composable
private fun NicknameSettingScreenPreview() {
    YongProjectTheme {
        NicknameSettingScreen(
            onBack = {},
        )
    }
}
