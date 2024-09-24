package com.youth.search

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.youth.app.feature.search.R

@Composable
fun SpecPolicyInfo(modifier: Modifier = Modifier, count: Int, title: String) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            )
            .padding(horizontal = 17.dp)
            .padding(top = 15.dp, bottom = 17.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "총 ${count}건의 정책이 있어요",
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        Icon(
            modifier = Modifier.padding(end = 30.dp),
            painter = painterResource(R.drawable.smile_icon),
            contentDescription = "스파일 아이콘",
            tint = MaterialTheme.colorScheme.onSecondary,
        )
    }
}
