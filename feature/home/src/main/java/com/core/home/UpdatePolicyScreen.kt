package com.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.PolicyCard
import com.youthtalk.model.Policy

@Composable
fun UpdatePolicyScreen(
    modifier: Modifier = Modifier,
    policy: Policy?,
    onClickDetailPolicy: (String) -> Unit,
    onClickScrap: (String, Boolean) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            .padding(horizontal = 17.dp),
    ) {
        policy?.let {
            PolicyCard(
                modifier = Modifier.padding(bottom = 12.dp),
                policy = policy,
                onClickDetailPolicy = onClickDetailPolicy,
                onClickScrap = onClickScrap,
            )
        }
    }
}
