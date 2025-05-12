package com.youthtalk.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.youth.app.core.designsystem.R
import com.youthtalk.component.tag.KeywordTag
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray60
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.Category
import com.youthtalk.model.policy.Policy
import timber.log.Timber

// TODO: 추후 Policy 기본값 제거
@Composable
fun PolicyCard(
    modifier: Modifier = Modifier,
    policy: Policy = Policy(
        policyId = 8038,
        category = Category.ALL,
        title = "solet",
        deadlineStatus = "quaeque",
        hostDep = "sententiae",
        scrapCount = 9348,
        departmentImgUrl = null,
        region = "quem",
        scrap = false
    ),
    isVisibleScrap: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeywordTag(
                text = policy.deadlineStatus.ifEmpty { "상시" },
                textColor = MaterialTheme.colorScheme.errorContainer,
                backgroundColor = MaterialTheme.colorScheme.onError
            )

            KeywordTag(
                text = policy.region
            )

            KeywordTag(
                text = policy.category.categoryName
            )

            Spacer(Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(
                        if (policy.scrap) R.drawable.bookmark_fill else R.drawable.bookmark_line
                    ),
                    contentDescription = "북마크",
                    colorFilter = if (policy.scrap) ColorFilter.tint(color = MaterialTheme.colorScheme.primary) else ColorFilter.tint(color = gray60)
                )
                Text(
                    text = "${policy.scrapCount}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray90
                    )
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = gray40,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (policy.departmentImgUrl.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.default_policy),
                        contentDescription = "정책 기본 이미지"
                    )
                } else {
                    Timber.e("policy.departmentImgUrl ${policy.departmentImgUrl}")
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(policy.departmentImgUrl)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Text(
                text = policy.title,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (isVisibleScrap) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "총",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray80
                    )
                )

                Text(
                    text = "${policy.scrapCount}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray90
                    )
                )

                Text(
                    text = "회 스크랩 됐어요!",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = gray80
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun PolicyCardPreview() {
    YongProjectTheme {
        PolicyCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = gray10,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = gray40,
                    shape = RoundedCornerShape(12.dp)
                ),
            policy = Policy(
                policyId = 8038,
                category = Category.ALL,
                title = "solet",
                deadlineStatus = "quaeque",
                hostDep = "sententiae",
                scrapCount = 9348,
                departmentImgUrl = null,
                region = "quem",
                scrap = false
            )
        )
    }
}

@Preview
@Composable
private fun PolicyCardBookmarkTruePreview() {
    YongProjectTheme {
        PolicyCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = gray10,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = gray40,
                    shape = RoundedCornerShape(12.dp)
                ),
            policy = Policy(
                policyId = 8038,
                category = Category.ALL,
                title = "solet",
                deadlineStatus = "quaeque",
                hostDep = "sententiae",
                scrapCount = 9348,
                departmentImgUrl = null,
                region = "quem",
                scrap = false
            )
        )
    }
}
