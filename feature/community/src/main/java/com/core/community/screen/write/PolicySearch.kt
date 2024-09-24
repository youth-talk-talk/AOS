package com.core.community.screen.write

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.community.R
import com.youthtalk.model.SearchPolicy
import com.youthtalk.util.clickableSingle

@Composable
fun PolicySearch(searchPolicy: SearchPolicy?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 20.dp, start = 17.dp, end = 17.dp)
            .clickableSingle { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Text(
            modifier = Modifier.width(42.dp),
            text = "정책명",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 13.dp, vertical = 17.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = searchPolicy?.title ?: "정책명",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = searchPolicy?.let { Color.Black } ?: MaterialTheme.colorScheme.onTertiary,
                ),
            )

            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "검색",
                tint = Color.Black,
            )
        }
    }
}
