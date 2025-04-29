package com.youthtalk.component.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.NotificationType

@Composable
fun NotificationComponent(
    modifier: Modifier = Modifier,
    notificationTitle: String,
    notificationSubTitle: String = "",
    backgroundColor: Color = gray10,
    notificationType: NotificationType = NotificationType.SCRAP,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        when (notificationType) {
            NotificationType.SCRAP -> {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(
                            width = 1.dp,
                            color = gray40,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.policy_notification),
                        contentDescription = "정책알림이미지",
                    )
                }
            }

            NotificationType.COMMUNITY -> {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(
                            width = 1.dp,
                            color = gray40,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.message),
                        contentDescription = "정책알림이미지",
                    )
                }
            }

            NotificationType.BOOK_MARK -> {
                Image(
                    painter = painterResource(R.drawable.bookmark_notification),
                    contentDescription = "스크랩알림이미지",
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = notificationTitle,
                style = MaterialTheme.typography.displayLarge,
            )

            if (notificationSubTitle.isNotEmpty()) {
                Text(
                    text = notificationSubTitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80,
                    ),
                )
            }
        }

        Text(
            text = "2시간 전",
            style = MaterialTheme.typography.labelSmall.copy(
                color = gray80,
            ),
        )
    }
}

@Preview
@Composable
private fun PolicyNotificationPreview() {
    YongProjectTheme {
        NotificationComponent(
            notificationTitle = "스크랩 한 정책이 오늘 마감돼요!",
            notificationSubTitle = "‘사상구 면접 A to Z 운영’ 정책이 오늘 마감돼요! 지금 확인해 볼까요?",
        )
    }
}

@Preview
@Composable
private fun PolicyNotificationOnPrimaryPreview() {
    YongProjectTheme {
        NotificationComponent(
            notificationTitle = "스크랩 한 정책이 오늘 마감돼요!",
            notificationSubTitle = "‘사상구 면접 A to Z 운영’ 정책이 오늘 마감돼요! 지금 확인해 볼까요?",
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview
@Composable
private fun PolicyNotificationBookMarkOnPrimaryPreview() {
    YongProjectTheme {
        NotificationComponent(
            notificationTitle = "고민은 지원을 늦출뿐! 지금 지원해 보는건 어떠신가요?",
            notificationSubTitle = "‘행복도시 공동캠퍼스의 안정적인 정착 지원’ 정책 지금 바로 확인해 보세요.",
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            notificationType = NotificationType.BOOK_MARK,
        )
    }
}

@Preview
@Composable
private fun PolicyNotificationMessagePreview() {
    YongProjectTheme {
        NotificationComponent(
            notificationTitle = "씩씩한 청년님이 내 게시글에 댓글을 남겼어요. 지금 바로 확인해 보세요.",
            notificationSubTitle = "\"이거 저만 그런 거 아니죠...? \uD83D\uDE02\"",
            notificationType = NotificationType.COMMUNITY,
        )
    }
}

@Preview
@Composable
private fun PolicyNotificationNotSubTitleOnPrimaryMessagePreview() {
    YongProjectTheme {
        NotificationComponent(
            notificationTitle = "씩씩한 청년님이 내 댓글에 좋아요를 눌렀어요. 지금 바로 확인해 보세요.",
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            notificationType = NotificationType.COMMUNITY,
        )
    }
}
