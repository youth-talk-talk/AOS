package com.youthtalk.component.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray

@Composable
fun FilterComponent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(10.dp),
                color = gray,
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 13.dp)
            .padding(top = 14.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "상세 조건을 입력해주세요",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.onTertiary,
            ),
        )

        Icon(
            painter = painterResource(id = R.drawable.filter_icon),
            contentDescription = "필터아이콘",
            tint = MaterialTheme.colorScheme.onTertiary,
        )
    }
}

@Preview
@Composable
private fun FilterComponentPreview() {
    YongProjectTheme {
        FilterComponent()
    }
}
