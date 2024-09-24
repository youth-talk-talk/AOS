package com.core.community.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommunityTab(modifier: Modifier = Modifier, tabIndex: Int, tabNames: List<String>, onTabClick: (Int) -> Unit) {
    TabRow(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(top = 17.dp),
        selectedTabIndex = tabIndex,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabIndex])
                    .padding(vertical = 1.dp)
                    .offset(y = 0.5.dp)
                    .height(3.dp)
                    .background(Color.Black, shape = RoundedCornerShape(3.dp)),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        divider = {
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.onTertiary, shape = RoundedCornerShape(2.dp)),
                color = Color.Transparent,
            )
        },
    ) {
        tabNames.forEachIndexed { index, s ->
            Tab(
                selectedContentColor = MaterialTheme.colorScheme.background,
                selected = index == tabIndex,
                onClick = { onTabClick(index) },
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = s,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = if (index == tabIndex) Color.Black else MaterialTheme.colorScheme.onTertiary,
                    ),
                )
            }
        }
    }
}
