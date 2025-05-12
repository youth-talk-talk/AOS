package com.youthtalk.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.gray100

@Composable
fun NoBackMiddleTitleTopBar(modifier: Modifier = Modifier, title: String, tails: (@Composable () -> Unit)? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 20.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = gray100
            )
        )

        tails?.let {
            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                tails()
            }
        }
    }
}
