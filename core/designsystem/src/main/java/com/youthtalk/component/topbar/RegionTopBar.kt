package com.youthtalk.component.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.model.Region

@Composable
fun RegionTopBar(modifier: Modifier = Modifier, region: Region, onClickRegion: () -> Unit, onClickSearch: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = gray10)
            .padding(horizontal = 16.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClickRegion()
                }
        ) {
            Image(
                modifier = Modifier.padding(end = 2.dp),
                painter = painterResource(R.drawable.location),
                contentDescription = stringResource(R.string.location)
            )

            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = region.region,
                style = MaterialTheme.typography.bodyMedium
            )

            Image(
                painter = painterResource(R.drawable.arrowdown),
                contentDescription = stringResource(R.string.arrow_down)
            )
        }

        Image(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClickSearch()
                },
            painter = painterResource(R.drawable.search),
            contentDescription = stringResource(R.string.search)
        )
    }
}

@Preview
@Composable
private fun RegionTopBarPreview() {
    YongProjectTheme {
        RegionTopBar(
            region = Region.SEOUL,
            onClickRegion = {},
            onClickSearch = {}
        )
    }
}
