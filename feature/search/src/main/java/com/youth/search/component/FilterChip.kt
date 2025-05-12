package com.youth.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.search.R
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray80

@Composable
fun FilterChip(modifier: Modifier = Modifier, text: String, count: Int = 0, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = gray50,
                shape = RoundedCornerShape(100.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(horizontal = 11.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.displaySmall.copy(
                color = if (count == 0) gray80 else MaterialTheme.colorScheme.primary
            )
        )
        if (count != 0) {
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = text,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }

        Image(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.arrowdown),
            contentDescription = "아래 화살표",
            colorFilter = ColorFilter.tint(color = gray80)
        )
    }
}
