package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.mypage.R
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun ScrapPolicyScreen(modifier: Modifier = Modifier) {
    var size by remember {
        mutableStateOf(5)
    }

    Column(
        modifier = modifier,
    ) {
        NoBackMiddleTitleTopBar(
            title = stringResource(R.string.scrap_policy_topbar_title),
            tails = {
                Image(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) {
                        },
                    painter = painterResource(R.drawable.close),
                    contentDescription = stringResource(R.string.close),
                )
            },
        )

        if (size <= 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(2f))
                EmptyScreen(
                    modifier = Modifier.weight(5f),
                    emptyTitle = stringResource(R.string.empty_scrap_policy_title),
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                repeat(size) {
                    PolicyCard(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) {
                            size--
                        },
                        isBookMark = true,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScrapPolicyScreenPreview() {
    YongProjectTheme {
        ScrapPolicyScreen()
    }
}
