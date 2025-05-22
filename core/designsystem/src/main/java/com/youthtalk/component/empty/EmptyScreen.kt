package com.youthtalk.component.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray80

@Composable
fun EmptyScreen(modifier: Modifier = Modifier, emptyTitle: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.empty),
            contentDescription = stringResource(R.string.empty_image)
        )
        Text(
            text = emptyTitle,
            style = MaterialTheme.typography.displaySmall.copy(
                color = gray80,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview
@Composable
private fun EmptyScrapPolicyScreenPreview() {
    YongProjectTheme {
        EmptyScreen(
            emptyTitle = "아직 스크랩한 정책이 없습니다."
        )
    }
}
