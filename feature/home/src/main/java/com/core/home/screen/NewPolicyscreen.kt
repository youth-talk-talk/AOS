package com.core.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.chip.RoundChip
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.Category

@Composable
fun NewPolicyScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    val categories = Category.entries.toList()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "최근 올라온 정책",
            onBack = onBack
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                count = categories.size
            ) {
                val title = categories[it].categoryName.split(" ").first()
                RoundChip(
                    text = title,
                    isSelected = it == 0
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 14.dp, bottom = 10.dp),
            color = gray30
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                count = 10
            ) {
                PolicyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = gray10,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = gray40,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewPolicyScreenPreview() {
    YongProjectTheme {
        NewPolicyScreen(
            onBack = {}
        )
    }
}
