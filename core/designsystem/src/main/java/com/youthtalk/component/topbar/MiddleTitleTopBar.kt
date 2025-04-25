package com.youthtalk.component.topbar

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray100
import com.youthtalk.util.clickableSingle

@Composable
fun MiddleTitleTopBar(modifier: Modifier = Modifier, title: String, onBack: () -> Unit, icon: (@Composable () -> Unit)? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 20.dp),
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickableSingle { onBack() },
            painter = painterResource(R.drawable.arrowleft),
            contentDescription = "뒤로가기",
        )

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = gray100,
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
