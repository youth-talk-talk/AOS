package com.example.policydetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.policydetail.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun PolicyDetailTopAppBar(modifier: Modifier = Modifier, isScrap: Boolean, onClickScrap: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            modifier = Modifier
                .padding(vertical = 13.dp),
            painter = painterResource(id = R.drawable.left_icon),
            contentDescription = "뒤로가기",
            tint = Color.Black,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.share_icon),
                contentDescription = "공유 아이콘",
                tint = MaterialTheme.colorScheme.onSecondary,
            )
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onClickScrap() },
                painter = if (isScrap) painterResource(id = R.drawable.bookmark_check) else painterResource(id = R.drawable.bookmark),
                contentDescription = "북마크 아이콘",
                tint = if (isScrap) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Preview
@Composable
private fun PolicyDetailTopAppBarPreview() {
    YongProjectTheme {
        PolicyDetailTopAppBar(
            isScrap = true,
            onClickScrap = {},
        )
    }
}

@Preview
@Composable
private fun PolicyDetailTopAppBarBookMarkFalsePreview() {
    YongProjectTheme {
        PolicyDetailTopAppBar(
            isScrap = false,
            onClickScrap = {},
        )
    }
}
