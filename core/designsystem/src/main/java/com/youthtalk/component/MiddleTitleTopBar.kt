package com.youthtalk.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.util.clickableSingle

@Composable
fun MiddleTitleTopBar(modifier: Modifier = Modifier, title: String, onBack: () -> Unit, icon: (@Composable () -> Unit)? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp, vertical = 13.dp),
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickableSingle { onBack() },
            painter = painterResource(R.drawable.left_icon),
            contentDescription = "뒤로가기",
        )

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.W400,
            ),
        )

        icon?.let {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                icon()
            }
        }
    }
}

@Preview
@Composable
private fun MiddleTitleTopBarPreview() {
    YongProjectTheme {
        MiddleTitleTopBar(
            title = "계정관리",
            onBack = {},
        )
    }
}
