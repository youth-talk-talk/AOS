package com.youthtalk.component.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40

@Composable
fun CustomCheckBox(modifier: Modifier = Modifier, isCheck: Boolean = false, onClick: () -> Unit = {}) {
    val background = if (isCheck) MaterialTheme.colorScheme.primary else gray40
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier
                .size(16.dp)
                .clickable {
                    onClick()
                },
            painter = painterResource(R.drawable.check),
            contentDescription = "체크",
            tint = gray10,
        )
    }
}

@Preview
@Composable
private fun CustomCheckBoxTruePreview() {
    YongProjectTheme {
        CustomCheckBox(
            isCheck = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun CustomCheckBoxFalsePreview() {
    YongProjectTheme {
        CustomCheckBox(
            isCheck = false,
            onClick = {},
        )
    }
}
