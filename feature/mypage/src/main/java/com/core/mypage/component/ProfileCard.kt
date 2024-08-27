package com.core.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.mypage.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray
import com.youthtalk.util.clickableSingle

@Composable
fun ProfileCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(start = 17.dp, end = 18.dp)
            .padding(vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = "놀고픈 청년")
        Row(
            modifier = Modifier
                .border(width = 1.dp, color = gray, shape = RoundedCornerShape(50.dp))
                .clickableSingle { onClick() }
                .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.setting_icon),
                contentDescription = "설정 아이콘",
                tint = Color.Black,
            )

            Text(
                text = "계정관리",
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}

@Preview
@Composable
private fun ProfileCardPreview() {
    YongProjectTheme {
        ProfileCard(
            onClick = {},
        )
    }
}
