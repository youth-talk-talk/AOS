package com.youthtalk.specpolicy.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youth.app.feature.specpolicy.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun SpecPolicyTopBar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.padding(
                start = 17.dp,
                end = 12.dp,
            ),
            painter = painterResource(id = R.drawable.left_icon),
            contentDescription = "뒤로가기",
            tint = Color.Black,
        )

        Text(
            modifier = Modifier
                .weight(1f),
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
            ),
        )

        Icon(
            modifier = Modifier.padding(end = 17.dp),
            painter = painterResource(id = R.drawable.search_icon),
            contentDescription = "검색",
            tint = Color.Black,
        )
    }
}

@Preview
@Composable
private fun SpecPolicyTopBarPreview() {
    YongProjectTheme {
        SpecPolicyTopBar("일자리")
    }
}
