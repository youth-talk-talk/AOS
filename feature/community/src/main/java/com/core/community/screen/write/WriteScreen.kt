package com.core.community.screen.write

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.community.R
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.CommunityType

@Composable
fun WriteScreen(modifier: Modifier = Modifier, communityType: CommunityType, onClickPolicySearch: () -> Unit, onClickPicture: () -> Unit) {
    var title by remember {
        mutableStateOf("")
    }

    var contents by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(gray10),
    ) {
        MiddleTitleTopBar(
            title = when (communityType) {
                CommunityType.REVIEW -> "후기 글쓰기"
                CommunityType.FREE -> "자유 글쓰기"
            },
            onBack = {},
            tails = {
                Text(
                    text = "등록",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray70,
                    ),
                )
            },
        )

        if (communityType == CommunityType.REVIEW) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        onClickPolicySearch()
                    }
                    .padding(horizontal = 16.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "정책 선택",
                    style = MaterialTheme.typography.titleSmall,
                )

                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.search),
                    contentDescription = "검색",
                    colorFilter = ColorFilter.tint(color = gray100),
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = gray40,
            )
        }

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = title,
            onValueChange = { title = it },
            textStyle = MaterialTheme.typography.titleMedium,
            maxLines = 1,
        ) { innerTextField ->
            Box {
                if (title.isEmpty()) {
                    Text(
                        text = "제목을 입력해주세요.",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = gray80,
                        ),
                    )
                }
                innerTextField()
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = gray40,
        )

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .weight(1f),
            value = contents,
            onValueChange = { contents = it },
            textStyle = MaterialTheme.typography.titleMedium,
            maxLines = 1,
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                if (contents.isEmpty()) {
                    Text(
                        text = when (communityType) {
                            CommunityType.REVIEW -> stringResource(R.string.review_content_hint)
                            CommunityType.FREE -> stringResource(R.string.free_content_hint)
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = gray80,
                        ),
                    )
                }
                innerTextField()
            }
        }

        HorizontalDivider(
            color = gray40,
        )

        Image(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) {
                    onClickPicture()
                },
            painter = painterResource(R.drawable.add_picture),
            contentDescription = "사진 추가",
        )
    }
}
