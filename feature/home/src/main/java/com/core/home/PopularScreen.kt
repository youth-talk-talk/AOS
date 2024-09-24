package com.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.PopularCard
import com.youthtalk.model.Policy

@Composable
fun PopularPolicyScreen(top5Policies: List<Policy>, onClickDetailPolicy: (String) -> Unit, onClickScrap: (String, Boolean) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2.2f)
            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            .padding(horizontal = 17.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = top5Policies,
            key = { item: Policy -> item.policyId },
        ) { policy ->
            PopularCard(
                modifier = Modifier
                    .aspectRatio(14 / 15f),
                policy = policy,
                onClickDetailPolicy = onClickDetailPolicy,
                onClickScrap = onClickScrap,
            )
        }
    }
}
