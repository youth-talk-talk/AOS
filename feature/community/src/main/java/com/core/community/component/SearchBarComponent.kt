package com.core.community.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.community.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray70

@Composable
fun SearchBarComponent(modifier: Modifier = Modifier, hint: String = "", onClick: () -> Unit = {}) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = gray30,
                shape = RoundedCornerShape(6.dp),
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.search),
            contentDescription = "검색",
            colorFilter = ColorFilter.tint(color = gray70),
        )

        Text(
            text = hint,
            style = MaterialTheme.typography.titleSmall.copy(
                color = gray70,
            ),
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
