package com.core.mypage.component.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.mypage.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray

@Composable
fun BorderIconText(modifier: Modifier = Modifier, title: String, icon: @Composable () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 1.dp,
                color = gray,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        icon()
    }
}

@Preview
@Composable
private fun BorderIconTextNickNamePreview() {
    YongProjectTheme {
        BorderIconText(
            title = "놀고픈 청년",
            icon = {
                Image(painter = painterResource(id = R.drawable.edit_icon), contentDescription = "닉네임 변경 아이콘")
            },
        )
    }
}

@Preview
@Composable
private fun BorderIconTextRegionPreview() {
    YongProjectTheme {
        BorderIconText(
            title = "부산광역시",
            icon = {
                Image(painter = painterResource(id = R.drawable.region_setting_icon), contentDescription = "지역 변경 아이콘")
            },
        )
    }
}
