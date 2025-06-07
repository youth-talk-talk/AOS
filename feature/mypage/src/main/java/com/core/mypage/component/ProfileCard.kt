package com.core.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.youth.app.feature.mypage.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Region

@Composable
fun ProfileCard(modifier: Modifier = Modifier, user: User, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = gray10)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            user.profileImgUrl?.let { img ->
                AsyncImage(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    model = img,
                    contentDescription = "프로필 이미지"
                )
            } ?: Image(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                painter = painterResource(com.youth.app.core.designsystem.R.drawable.profile_thumnail),
                contentDescription = "기본 이미지"
            )

            Text(
                text = user.nickname,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Image(
            painter = painterResource(R.drawable.arrowright),
            contentDescription = "이동이미지"
        )
    }
}

@Preview
@Composable
private fun ProfileCardPreview() {
    YongProjectTheme {
        ProfileCard(
            user = User(
                memberId = 0,
                nickname = "놀고픈 청년",
                profileImgUrl = null,
                region = Region.ALL
            ),
            onClick = {}
        )
    }
}
