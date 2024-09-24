package com.core.community.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.core.community.screen.write.onClickNoIndicator
import com.youth.app.feature.community.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun CommunityDetailAppBar(
    modifier: Modifier = Modifier,
    category: String?,
    isMine: Boolean,
    scrap: Boolean,
    onBack: () -> Unit,
    onDeleteDialog: () -> Unit,
    onClickModifier: () -> Unit,
    onPostScrap: (Boolean) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .padding(
                horizontal = 17.dp,
                vertical = 13.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onBack() },
                painter = painterResource(id = R.drawable.left_icon),
                contentDescription = "뒤로가기",
                tint = Color.Black,
            )
            category?.let { categoryName ->
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.share_icon),
                contentDescription = "공유",
                tint = Color.Black,
            )

            Icon(
                modifier = Modifier.onClickNoIndicator { onPostScrap(scrap) },
                painter = if (scrap) painterResource(id = R.drawable.bookmark_check) else painterResource(id = R.drawable.bookmark),
                contentDescription = "북마크",
                tint = if (scrap) MaterialTheme.colorScheme.primary else Color.Black,
            )

            if (isMine) {
                Box {
                    Icon(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                expanded = true
                            },
                        painter = painterResource(id = R.drawable.more_icon),
                        contentDescription = "더보기",
                        tint = Color.Black,
                    )

                    DropdownMenu(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "수정",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                )
                            },
                            onClick = { onClickModifier() },
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "삭제",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                )
                            },
                            onClick = { onDeleteDialog() },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityDetailAppBarPreview() {
    YongProjectTheme {
        CommunityDetailAppBar(
            category = "",
            isMine = true,
            scrap = true,
            onBack = { },
            onDeleteDialog = { },
            onClickModifier = { },
        ) {
        }
    }
}
