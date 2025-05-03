package com.core.community.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.youth.app.feature.community.R
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90

@Composable
fun PopularPostCard(modifier: Modifier = Modifier, header: String = "", communityTitle: String, content: String, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 20.dp),
    ) {
        if (header.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = header,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = gray80,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(
            text = communityTitle,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
            text = content,
            style = MaterialTheme.typography.labelSmall.copy(
                color = gray90,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.message),
                    contentDescription = "댓글",
                    colorFilter = ColorFilter.tint(color = gray80),
                )
                Text(
                    text = "13",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80,
                    ),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.bookmark_line),
                    contentDescription = "북마크",
                    colorFilter = ColorFilter.tint(color = gray80),
                )
                Text(
                    text = "13",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80,
                    ),
                )
            }
        }
    }
}
