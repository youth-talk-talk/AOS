package com.core.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.home.R
import com.youthtalk.designsystem.mainHomeActionBarColor

@Composable
fun HomeAppBar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().background(color = mainHomeActionBarColor).padding(vertical = 8.dp).padding(start = 17.14.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(id = R.string.top_name),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Preview
@Composable
private fun HomeAppBarPreview() {
    HomeAppBar()
}
