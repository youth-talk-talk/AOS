package com.core.community.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.youthtalk.util.clickableSingle

@Composable
fun BoxScope.WriteButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier.Companion
            .align(Alignment.BottomCenter)
            .padding(bottom = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50.dp),
            )
            .clickableSingle {
                onClick()
            }
            .padding(
                horizontal = 17.dp,
                vertical = 13.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.write_icon),
            contentDescription = "글쓰기",
            tint = Color.Black,
        )
        Text(
            text = "글쓰기",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
