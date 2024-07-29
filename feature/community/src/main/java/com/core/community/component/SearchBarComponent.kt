package com.core.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.community.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun SearchBarComponent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(43.dp))
            .background(color = Color.White, shape = RoundedCornerShape(43.dp)),
    ) {
        Icon(
            modifier = Modifier.padding(start = 20.dp, top = 13.dp, bottom = 13.dp),
            painter = painterResource(id = R.drawable.search_icon),
            contentDescription = "검색",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview
@Composable
private fun SearchBarComponentPReview() {
    YongProjectTheme {
        SearchBarComponent()
    }
}
