package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.SearchChip
import com.youthtalk.util.clickableSingle

@Composable
fun RecentlySearchScreen(recentlyList: List<String>, searchClick: (String) -> Unit, onDeleteRecently: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                modifier = Modifier
                    .padding(start = 17.dp),
                text = "최근 검색",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary,
                ),
            )
        }

        items(
            count = recentlyList.size,
            key = { index -> recentlyList[index] },
        ) { index ->
            SearchChip(
                modifier = Modifier
                    .padding(8.dp)
                    .clickableSingle { searchClick(recentlyList[index]) },
                shape = RoundedCornerShape(43.dp),
                text = recentlyList[index],
                onDeleteRecently = onDeleteRecently,
            )
        }
    }
}
